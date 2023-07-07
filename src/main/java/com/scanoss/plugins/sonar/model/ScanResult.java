package com.scanoss.plugins.sonar.model;

import com.scanoss.dto.ScanFileDetails;

import java.util.List;
import java.util.Map;

/**
 * Scan result container class
 */
public class ScanResult {

    /**
     * Map of files and scan results
     */
    Map<String, List<ScanFileDetails>> files;

    /**
     * Creates an empty ScanResult.
     */
    public ScanResult(){

    }

    /**
     * Scan Result map getter
     * @return files map
     */
    public Map<String, List<ScanFileDetails>> getFiles() {
        return files;
    }

    /**
     * Scan Result map setter
     * @param files Result map
     */
    public void setFiles(Map<String, List<ScanFileDetails>> files) {
        this.files = files;
    }
}
