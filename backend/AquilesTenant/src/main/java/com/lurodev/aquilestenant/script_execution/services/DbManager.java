package com.lurodev.aquilestenant.script_execution.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class DbManager {

    public boolean createTable(String url, String dbName, String username, String password, String sqlFilePath){
        JdbcTemplate jdbcTemplate = this.createJdbcTemplateForCurrentDataSource(url, dbName, username, password);
        return this.executeSql(sqlFilePath, jdbcTemplate);
    }

    public boolean validateUserExistByEmail(@NonNull String url, @NonNull String dbName, @NonNull String username, @NonNull String password, @NonNull String tableName, @NonNull String columnName, @NonNull String email) {
        JdbcTemplate jdbcTemplate = this.createJdbcTemplateForCurrentDataSource(url, dbName, username, password);
        String sql = "SELECT COUNT(*) FROM %s WHERE %s = ?".formatted(tableName, columnName);
        int count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count > 0;
    }

    public boolean addItem(String url, String dbName, String username, String password, String sqlStatement) throws RuntimeException {
        boolean sqlCreation = false;
        JdbcTemplate jdbcTemplate = this.createJdbcTemplateForCurrentDataSource(url, dbName, username, password);

        if(sqlStatement != null){
            try {
                jdbcTemplate.update(sqlStatement);
                sqlCreation = true;
            } catch (Exception e) {
                throw new RuntimeException("Error al ejecutar la sentencia SQL: " + e.getMessage(), e);
            }
        }

        return sqlCreation;
    }

    private JdbcTemplate createJdbcTemplateForCurrentDataSource(String url, String dbName, String username, String password){
        DataSource dataSource = this.createDataSource(url, dbName, username, password);
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }

    private DataSource createDataSource(String url, String schema, String username, String password) {
        String urlNewDb = url + schema;
        DriverManagerDataSource tenantDataSource = new DriverManagerDataSource();
        tenantDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        tenantDataSource.setUrl(urlNewDb);
        tenantDataSource.setUsername(username);
        tenantDataSource.setPassword(password);
        return tenantDataSource;
    }

    private boolean executeSql(String filePath, JdbcTemplate jdbcTemplate) {
        try {
            // Lee el archivo SQL
            InputStream inputStream = new ClassPathResource(filePath).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            // Lee y concatena las líneas del archivo SQL
            StringBuilder scriptBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                scriptBuilder.append(line).append("\n");
            }
            reader.close();

            // Separa las sentencias SQL por punto y coma
            String[] sqlStatements = scriptBuilder.toString().split(";");

            // Ejecuta cada sentencia SQL
            for (String sqlStatement : sqlStatements) {
                if (!sqlStatement.trim().isEmpty()) {
                    jdbcTemplate.execute(sqlStatement);
                }
            }
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
