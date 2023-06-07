package com.scanoss.plugins.sonar.model;

import java.util.List;
import java.util.Map;

public class ScanResult {

    Map<String, List<ScanData>> files;

    public Map<String, List<ScanData>> getFiles() {
        return files;
    }

    public void setFiles(Map<String, List<ScanData>> files) {
        this.files = files;
    }
}
