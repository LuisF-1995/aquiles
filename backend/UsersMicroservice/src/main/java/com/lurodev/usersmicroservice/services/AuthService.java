package com.lurodev.usersmicroservice.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lurodev.usersmicroservice.microservice.dto.TenantUserClientDTO;
import com.lurodev.usersmicroservice.models.dto.RequestResponse;
import com.lurodev.usersmicroservice.models.dto.UserAuthenticated;
import com.lurodev.usersmicroservice.models.dto.UserLogin;
import com.lurodev.usersmicroservice.models.User;
import com.lurodev.usersmicroservice.multitenancy.context.TenantContext;
import com.lurodev.usersmicroservice.repository.UserRepository;
import com.lurodev.usersmicroservice.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService{
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;

    private List<TenantUserClientDTO> getAuthTenantCompanyIdByUsername(@NonNull String username){
        List<TenantUserClientDTO> tenantUser = userService.getTenantUserByUsername(username);
        return userService.getTenantUserByUsername(username);
    }
    private String getParameterFromJson(String jsonString, String parameter) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return jsonNode.get(parameter).asText();
    }
    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    @Override
    public RequestResponse authenticateUser(UserLogin userLogin) {
        List<TenantUserClientDTO> tenantUser = this.getAuthTenantCompanyIdByUsername(userLogin.getEmail());

        if(tenantUser == null)
            return new RequestResponse(null, false, HttpStatus.NOT_FOUND.value(), "There's no tenant tied to: " + userLogin.getEmail());
        else if (tenantUser.isEmpty())
            return new RequestResponse(null, false, HttpStatus.NOT_FOUND.value(), "TenantUser not found for: " + userLogin.getEmail());
        else if (tenantUser.size() == 1) {
            String companyId = tenantUser.get(0).getTenant().getCompanyId();
            TenantContext.setTenantId(companyId);
            jwtService.setRequestTenantId(companyId);
        }else if (tenantUser.size() > 1){
            return new RequestResponse(tenantUser, false, HttpStatus.MULTIPLE_CHOICES.value(), "You are in more than 1 tenant: please select the required tenant");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.getEmail(),
                        userLogin.getPassword()
                )
        );

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);

        UserAuthenticated userAuthenticated = new UserAuthenticated(user.getId(), TenantContext.getTenantId() ,jwtToken);
        return new RequestResponse(userAuthenticated, true, HttpStatus.OK.value(), "Authentication success");
    }

    @Override
    public Boolean validateToken(@NonNull String tenantId, String token, Long userId) {
        boolean isValid = false;
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()){
            isValid = jwtService.isValidToken(token, user.get(), tenantId);
        }

        return isValid;
    }

    @Override
    public Boolean sameTenantId(Object object, Short tokenTenantId) {
        boolean isSameTenantId = false;

        try {
            Method getterMethod = object.getClass().getMethod("getTenantId");
            Short tenantIdExtracted = (Short) getterMethod.invoke(object);
            isSameTenantId = tenantIdExtracted.equals(tokenTenantId);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        return isSameTenantId;
    }
}
