package com.scanoss.plugins.sonar.analyzers;


import com.scanoss.Scanner;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SCANOSS Scanner
 * <p>
 * Performs the scan of files using the scanoss.java library
 * </p>
 */
public class ScanOSSScanner {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Loggers.get(ScanOSSScanner.class);

    /**
     * Private constructor to allow only statics
     */
    private ScanOSSScanner() {
        // only statics
    }

    /**
     * Scan files in the given path against the given API endpoint url
     * @param path folder to scan
     * @param url Scan API endpoint
     * @param key Scan API access token (optional)
     * @return Scan result (in JSON format)
     * @throws RuntimeException Scanning went wrong
     */
    public static List<String> runScan(String path, String url, String key) throws RuntimeException {
        LOGGER.info("[SCANOSS] Scanning path " + path + "...");
        Scanner scanner = Scanner.builder().url(url).apiKey(key).build();
        List<String> output = scanner.scanFolder(path);
        LOGGER.info("[SCANOSS] Scan finished");
        LOGGER.debug("[SCANOSS] " + Arrays.toString(output.toArray()));
        return output;

    }

}
