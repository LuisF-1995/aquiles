package com.lurodev.aquilestenant.script_execution.services;

import com.lurodev.aquilestenant.dto.RequestResponse;
import com.lurodev.aquilestenant.script_execution.models.ProcessReader;
import com.lurodev.aquilestenant.script_execution.models.ScriptNames;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ExecutionService {

    private ProcessBuilder createProcessBuilderByScriptName(@NonNull String scriptName, @NonNull String dbName, @NonNull String dbUser){
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        ProcessBuilder processBuilder = new ProcessBuilder();

        if(scriptName.equals(ScriptNames.script.name())){
            if(isWindows){
                processBuilder.command(System.getProperty("user.dir") + "\\scripts\\" + scriptName + ".bat");
            }
            else {
                processBuilder.command("sh", System.getProperty("user.dir") + "/scripts/" + scriptName + ".sh");
            }
        }
        else if (scriptName.equals(ScriptNames.createdb.name())) {
            if (dbName == null || dbUser == null || dbName.isEmpty() || dbUser.isEmpty()) {
                System.out.println("Uso: java Main falta <nombre_basedatos> <usuario>");
                System.exit(1);
            }
            else{
                if(isWindows){
                    processBuilder.command(System.getProperty("user.dir") + "\\scripts\\" + scriptName + ".bat", dbName, dbUser);
                }
                else {
                    processBuilder.command("sh", System.getProperty("user.dir") + "/scripts/" + scriptName + ".sh", dbName, dbUser);
                }
            }
        }
        else if (scriptName.equals(ScriptNames.create_tenant_api.name())){
            if (dbName == null || dbUser == null || dbName.isEmpty() || dbUser.isEmpty()) {
                System.out.println("Uso: java Main falta información");
                System.exit(1);
            }
            else{
                if(isWindows){
                processBuilder.command(System.getProperty("user.dir") + "\\scripts\\" + scriptName + ".bat", dbName, dbUser);
                }
                else {
                    processBuilder.command("sh", System.getProperty("user.dir") + "/scripts/" + scriptName + ".sh", dbName, dbUser);
                }
            }
        }

        return processBuilder;
    }

    public RequestResponse execScript(@NonNull String scriptName, @NonNull String dbName, @NonNull String dbUser){
        RequestResponse executionResponse = new RequestResponse(null, false, HttpStatus.NOT_IMPLEMENTED.value(), "No se pudo crear la base de datos");
        ProcessBuilder processBuilder = createProcessBuilderByScriptName(scriptName, dbName, dbUser);
        ExecutorService pool = Executors.newSingleThreadExecutor();

        try {
            Process process = processBuilder.start();
            ProcessReader task = new ProcessReader(process.getInputStream());
            Future<List<String>> future = pool.submit(task);

            List<String> results = future.get();

            String message = "";
            for (String response : results) {
                message = response;
            }
            int exitCode = process.waitFor();

            executionResponse.setModel(null);
            executionResponse.setSuccess(message.equals("true"));
            executionResponse.setHttpStatus(HttpStatus.OK.value());
            executionResponse.setMessage(message);
        }
        catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            pool.shutdown();
        }

        return executionResponse;
    }
}
