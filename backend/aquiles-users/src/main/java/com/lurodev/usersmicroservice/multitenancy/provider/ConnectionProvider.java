package com.lurodev.usersmicroservice.multitenancy.provider;

import com.lurodev.usersmicroservice.microservice.client.TenantClient;
import com.lurodev.usersmicroservice.microservice.dto.AppServiceClientDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ConnectionProvider implements MultiTenantConnectionProvider<String>, HibernatePropertiesCustomizer {
    private final TenantClient tenantClient;
    @Value("${multitenancy.database.default-schema}")
    private String defaultSchema;
    @Value("${multitenancy.database.service-prefix}")
    private String dbPrefix;
    @Value("${multitenancy.database.dbBaseUrl}")
    private String dbBaseUrl;
    @Value("${spring.datasource.username}")
    private String dbRootUsername;
    @Value("${spring.datasource.password}")
    private String dbUserRootPassword;
    @Value("${services.user.name}")
    private String userServiceName;

    private DataSource setDataSource(String url, String schema, String username, String password) {
        String urlNewDb = url + schema;
        DriverManagerDataSource tenantDataSource = new DriverManagerDataSource();
        tenantDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        tenantDataSource.setUrl(urlNewDb);
        tenantDataSource.setUsername(username);
        tenantDataSource.setPassword(password);
        return tenantDataSource;
    }

    @Override
    public Connection getConnection(String tenantIdentifier) {
        String schema = tenantIdentifier;
        String dbAdminUser = dbRootUsername;

        if(!tenantIdentifier.equals(defaultSchema)){
            schema = dbPrefix + tenantIdentifier;
            AppServiceClientDTO appServiceClientDTO = tenantClient.getServiceByParams(tenantIdentifier, userServiceName);
            if(appServiceClientDTO != null)
                dbAdminUser = appServiceClientDTO.getDbAdmin();
        }

        try{
            DataSource newDataSource = this.setDataSource(dbBaseUrl, schema, dbAdminUser, dbUserRootPassword);
            Connection connection = newDataSource.getConnection();
            connection.setSchema(schema);
            return connection;
        }catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Connection getAnyConnection() {
        return getConnection(defaultSchema);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.setSchema(defaultSchema);
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
    }

    @Override
    public boolean isUnwrappableAs(@NonNull Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(@NonNull Class<T> unwrapType) {
        return null;
    }
}
