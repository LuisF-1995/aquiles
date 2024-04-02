package com.lurodev.aquilestenant.service;

import com.lurodev.aquilestenant.dto.RequestResponse;
import com.lurodev.aquilestenant.model.ApplicationService;
import com.lurodev.aquilestenant.repository.IServiceRepository;
import com.lurodev.aquilestenant.script_execution.models.ScriptNames;
import com.lurodev.aquilestenant.script_execution.services.DbManager;
import com.lurodev.aquilestenant.script_execution.services.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceServ {
    private final IServiceRepository serviceRepository;
    private final ExecutionService scriptExecutionService;
    private final DbManager dbManager;
    @Value("${spring.application.dbBaseUrl}")
    String dbBaseUrl;
    @Value("${spring.datasource.password}")
    String dbRootPassword;
    @Value("${application.name}")
    String applicationName;

    public List<ApplicationService> getAllServices(){
        return serviceRepository.findAll();
    }

    public List<ApplicationService> getAllDefaultServices(){
        return serviceRepository.findAllByIsDefault(true);
    }

    public  ApplicationService getByCompanyIdAndServiceName(@NonNull String companyId, @NonNull String serviceName){
        return serviceRepository.findFirstByCompanyIdAndServiceName(companyId, serviceName);
    }

    public RequestResponse createService (ApplicationService service){
        if(this.serviceExists(service))
            return new RequestResponse(null, false, HttpStatus.CONFLICT.value(), "Service already exists");

        String dbAdminUser = applicationName + "_" + service.getServiceName() + "_default_admin";
        String dbName = applicationName + "_" + service.getServiceName() + "_default";

        if(!service.getIsDefault()){
            if(service.getDbAdmin() != null || service.getCompanyId() != null){
                dbAdminUser = service.getDbAdmin();
                dbName = applicationName + "_" + service.getServiceName() + "_" + service.getCompanyId();
            }
            else{
                return new RequestResponse(null, false, HttpStatus.BAD_REQUEST.value(), "Database not created for the service: dbAdmin or companyId values are missing");
            }
        }

        RequestResponse dbCreationResponse = scriptExecutionService.execScript(ScriptNames.createdb.name(), dbName, dbAdminUser);
        if(!dbCreationResponse.getSuccess())
            return new RequestResponse(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database not created for the service");

        String sqlFilePath = "db/migration/" + service.getServiceName() + "_tables.sql";
        boolean serviceTablesCreation = dbManager.createTable(
                dbBaseUrl + "/",
                dbName,
                dbAdminUser,
                dbRootPassword,
                sqlFilePath);
        if(!serviceTablesCreation)
            return new RequestResponse(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Tables not created for the database of the service: " + service.getServiceName());

        service.setDbAdmin(dbAdminUser);
        ApplicationService serviceSaved = serviceRepository.save(service);
        return new RequestResponse(serviceSaved, true, HttpStatus.OK.value(), "Service created");
    }

    public void update (ApplicationService service){
        serviceRepository.save(service);
    }

    private boolean serviceExists(ApplicationService service){
        ApplicationService serviceByNameAndCompanyId = serviceRepository.findFirstByCompanyIdAndServiceName(service.getCompanyId(), service.getServiceName());
        return serviceByNameAndCompanyId != null;
    }
}
