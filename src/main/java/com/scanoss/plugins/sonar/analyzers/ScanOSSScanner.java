package com.scanoss.plugins.sonar.analyzers;


import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScanOSSScanner {

    private static final Logger LOGGER = Loggers.get(ScanOSSScanner.class);
    private String command = "scanoss-py";
    private Boolean useDocker = false;

    public ScanOSSScanner(String containerImage){
        if(containerImage != null && !containerImage.isEmpty()){
            this.command = "docker run --rm -v %:/app -w /app " + containerImage;
            this.useDocker = true;
        }
    }

    public String runScan(String path, String url, String key) throws RuntimeException {
        LOGGER.info("[SCANOSS] Scanning path " + path + "...");
        ProcessBuilder processBuilder = createProcessBuilder(path, url, key);
        processBuilder.redirectErrorStream(true);
        Process process = null;
        String output = null;
        try {
            process = processBuilder.start();
            List<String> processOutput = readProcessOutput(process.getInputStream());
            LOGGER.debug("[SCANOSS] " + Arrays.toString(processOutput.toArray()));
            int jsonStartPosition = processOutput.indexOf("{");
            List<String> results;
            if(jsonStartPosition < 0) {
                results = processOutput;
                LOGGER.error("[SCANOSS] " + String.join("",results));
                return null;
            }
            results = processOutput.subList(jsonStartPosition, processOutput.size());
            int exitCode = process.waitFor();
            output = String.join("", results);
            LOGGER.info("[SCANOSS] Exit code: " + String.valueOf(exitCode));
            LOGGER.debug(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return output;
    }

    private ProcessBuilder createProcessBuilder(String path, String url, String key){
        List<String> pbCommand = new ArrayList<String>();
        String command = this.command;
        if (this.useDocker) {
            command = command.replace("%", path);
            path = ".";
        }
        pbCommand.addAll(Arrays.asList(command.split(" ")));
        pbCommand.add("scan");

        if (url != null && !url.isEmpty()) {
            pbCommand.add("--apiurl");
            pbCommand.add(url);
        }

        if (key != null && !key.isEmpty()) {
            pbCommand.add("--key");
            pbCommand.add(key);
        }

        pbCommand.add(path);

        return new ProcessBuilder(pbCommand);
    }

    private static List<String> readProcessOutput(InputStream inputStream) {
        List<String> outputLines = new ArrayList<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                outputLines.add(line);
            }
        } catch (IOException e) {
            // Handle any exceptions that may occur during reading
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Handle any exceptions that may occur during closing the reader
                    e.printStackTrace();
                }
            }
        }

        return outputLines;
    }

}
