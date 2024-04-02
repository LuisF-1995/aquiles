package com.lurodev.aquilestenant.script_execution.models;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class ProcessReader implements Callable {
    private InputStream inputStream;

    public ProcessReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public Object call() throws Exception {
        return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.toList());
    }
}
