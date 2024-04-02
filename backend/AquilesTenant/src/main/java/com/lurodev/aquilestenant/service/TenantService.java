package com.lurodev.aquilestenant.service;

import com.lurodev.aquilestenant.dto.RequestResponse;
import com.lurodev.aquilestenant.dto.TenantRegisterContainer;
import com.lurodev.aquilestenant.microservice.dto.User;
import com.lurodev.aquilestenant.model.*;
import com.lurodev.aquilestenant.repository.ITenantUserRepository;
import com.lurodev.aquilestenant.repository.TenantRepository;
import com.lurodev.aquilestenant.script_execution.models.ScriptNames;
import com.lurodev.aquilestenant.script_execution.services.DbManager;
import com.lurodev.aquilestenant.script_execution.services.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TenantService implements ITenantService {
    private final TenantRepository tenantRepository;
    private final DbManager dbManager;
    private final ApplicationServiceServ applicationService;
    private final ITenantUserService tenantUserService;
    @Value("${spring.application.dbBaseUrl}")
    String dbBaseUrl;
    @Value("${spring.datasource.username}")
    String dbUserName;
    @Value("${spring.datasource.password}")
    String dbUserPassword;
    @Value("${spring.datasource.password}")
    String dbRootPassword;
    @Value("${application.name}")
    String applicationName;


    private RequestResponse validateTenantStructure(Tenant tenant){
        RequestResponse response = null;

        if(tenant.getCompanyName() == null || tenant.getCompanyName().isEmpty()){
            response = new RequestResponse(null, false, HttpStatus.BAD_REQUEST.value(), "companyName is required");
        }else if(tenant.getCompanyId() == null || tenant.getCompanyId().isEmpty() || tenant.getCompanyId().isBlank()){
            response = new RequestResponse(null, false, HttpStatus.BAD_REQUEST.value(), "companyId or NIT is required");
        }else if(tenant.getCountry() == null || tenant.getCountry().isEmpty() || tenant.getCountry().isBlank()){
            response = new RequestResponse(null, false, HttpStatus.BAD_REQUEST.value(), "country is required");
        }else if(tenant.getIsolated() == null){
            response = new RequestResponse(null, false, HttpStatus.BAD_REQUEST.value(), "isolated boolean value is required");
        }else if(tenant.getRegistrationDate() == null){
            response = new RequestResponse(null, false, HttpStatus.BAD_REQUEST.value(), "registrationDate value is required");
        }else if(tenant.getPlan().name().isEmpty() || tenant.getPlan().name().isBlank() || tenant.getPlan().name() == null){
            response = new RequestResponse(null, false, HttpStatus.BAD_REQUEST.value(), "plan value is required");
        }

        return response;
    }

    private RequestResponse verifyTenantExistence(Tenant tenant){
        Optional<Tenant> tenantByCompanyId = tenantRepository.findByCompanyId(tenant.getCompanyId());
        Optional<Tenant> tenantByCompanyName = tenantRepository.findByCompanyName(tenant.getCompanyName());


        if(tenantByCompanyId.isPresent() || tenantByCompanyName.isPresent())
            return new RequestResponse(null, false, HttpStatus.CONFLICT.value(), "Tenant already exists");
        else
            return null;
    }

    @Override
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    @Override
    public List<Tenant> getTenantsByCountry(String country) {
        return tenantRepository.findAllByCountry(country);
    }

    @Override
    public List<Tenant> getTenantsByStatus(Boolean status) {
        return tenantRepository.findAllByIsActive(status);
    }

    @Override
    public Tenant getTenantById(Integer tenantId) {
        Optional<Tenant> tenant = tenantRepository.findById(tenantId);
        return tenant.orElse(null);
    }

    @Override
    public Tenant getTenantByCompanyName(String companyName) {
        Optional<Tenant> tenant = tenantRepository.findByCompanyName(companyName);
        return tenant.orElse(null);
    }

    @Override
    public Tenant getTenantByCompanyId(String companyId) {
        Optional<Tenant> tenant = tenantRepository.findByCompanyId(companyId);
        return tenant.orElse(null);
    }

    @Override
    @Transactional
    public RequestResponse registerTenant(TenantRegisterContainer tenantRegisterContainer) {
        try {
            Tenant tenant = tenantRegisterContainer.getTenant();

            RequestResponse validateTenantRequiredFields = this.validateTenantStructure(tenant);
            if(validateTenantRequiredFields != null)
                return validateTenantRequiredFields;

            RequestResponse verifyTenantExists = this.verifyTenantExistence(tenant);
            if(verifyTenantExists != null)
                return verifyTenantExists;

            List<ApplicationService> services = applicationService.getAllDefaultServices();
            if(services.isEmpty())
                return new RequestResponse(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database not created, there are not services");

            for (ApplicationService service : services) {
                String dbAdminUser = applicationName + "_admin_" + tenant.getCompanyId();

                ApplicationService tenantService = new ApplicationService();
                tenantService.setServiceName(service.getServiceName());
                tenantService.setDbAdmin(dbAdminUser);
                tenantService.setCompanyId(tenant.getCompanyId());
                tenantService.setIsDefault(false);
                RequestResponse dbCreationResponse = applicationService.createService(tenantService);

                if(!dbCreationResponse.getSuccess())
                    return new RequestResponse(dbCreationResponse.getModel(), false, dbCreationResponse.getHttpStatus(), dbCreationResponse.getMessage());

                if(service.getServiceName().equals(ServiceNames.users.name())){
                    String dbName = applicationName + "_" + ServiceNames.users.name() + "_" + tenant.getCompanyId();
                    boolean tenantOwnerAlreadyExists = dbManager.validateUserExistByEmail(
                            dbBaseUrl + "/",
                            dbName,
                            dbAdminUser,
                            dbRootPassword,
                            "users",
                            "email",
                            tenantRegisterContainer.getOwner().getEmail()
                    );

                    if(!tenantOwnerAlreadyExists){
                        boolean tenantOwnerRegistration = this.registerTenantOwner(tenantRegisterContainer);
                        if(!tenantOwnerRegistration)
                            return new RequestResponse(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Owner not created, please verify owner info and try again");
                    }
                }
            }

            tenant.setIsActive(true);
            Tenant tenantRegister = tenantRepository.save(tenant);

            TenantUser tenantOwner = new TenantUser();
            tenantOwner.setTenant(tenantRegister);
            tenantOwner.setUsername(tenantRegisterContainer.getOwner().getEmail());
            tenantUserService.registerUser(tenantOwner);

            return new RequestResponse(tenantRegister, true, HttpStatus.OK.value(), "Tenant created");
        }catch (RuntimeException e) {
            // Si algo falla, se deshacen todas las operaciones realizadas hasta el momento
            return new RequestResponse(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    private boolean registerTenantOwner(TenantRegisterContainer tenantRegisterContainer) {
        boolean ownerRegistrationSuccess = false;
        Tenant tenant = tenantRegisterContainer.getTenant();
        User owner = tenantRegisterContainer.getOwner();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String ownerEncryptedPass = passwordEncoder.encode(owner.getPassword());
        Long ownerId = 1L;

        String dbName = applicationName + "_" + ServiceNames.users.name() + "_" + tenant.getCompanyId();
        String dbAdminUser = applicationName + "_admin_" + tenant.getCompanyId();
        String sqlCreateOwner = """
                INSERT INTO `%s`.`users`
                (dni, email, is_active, lastname, name, password)
                VALUES
                ('%s', '%s', %s, '%s', '%s', '%s');
                """
                .formatted(dbName,
                        owner.getDni(), owner.getEmail(), true, owner.getLastname(), owner.getName(), ownerEncryptedPass);

        String sqlSetTenantOwnerRol = """
                INSERT INTO `%s`.`roles`
                (rol, user_id)
                VALUES
                ('%s', %s);
                """
                .formatted(dbName,
                        UserRoles.TENANT_OWNER.name(), ownerId);

        try {
            boolean ownerCreation = dbManager.addItem(
                    dbBaseUrl + "/",
                    dbName,
                    dbAdminUser,
                    dbRootPassword,
                    sqlCreateOwner);

            boolean ownerTenantRol = dbManager.addItem(
                    dbBaseUrl + "/",
                    dbName,
                    dbAdminUser,
                    dbRootPassword,
                    sqlSetTenantOwnerRol);

            if(ownerCreation && ownerTenantRol){
                ownerRegistrationSuccess = true;
            }
        }catch (RuntimeException exception){
            return false;
        }

        return ownerRegistrationSuccess;
    }

    @Override
    public RequestResponse updateTenant(Tenant tenant) {
        Tenant tenantRegister = tenantRepository.save(tenant);
        return new RequestResponse(tenantRegister, true, HttpStatus.OK.value(), "Tenant updated");
    }

    @Override
    public RequestResponse deleteTenantById(Integer tenantId) {
        Tenant tenant = this.getTenantById(tenantId);
        RequestResponse response = new RequestResponse(
                null,
                false,
                HttpStatus.NOT_FOUND.value(),
                "El tenant con id: " + tenantId +" no fue eliminado, porque no fue encontrado"
        );

        if(tenant != null){
            response.setSuccess(true);
            response.setModel(null);
            response.setHttpStatus(HttpStatus.OK.value());
            response.setMessage("El tenant con id: " + tenantId + " fue satisfactoriamente eliminado");
        }

        return response;
    }
}
