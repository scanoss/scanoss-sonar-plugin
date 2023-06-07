package com.scanoss.plugins.sonar.model;

import com.google.gson.Gson;

import java.util.List;

public class ScanData {

    private String id;
    private List<QualityAttribute> quality;
    private List<LicenseInfo> licenses;
    private List<VulnerabilityInfo> vulnerabilities;
    private String file;
    private String file_hash;
    private String file_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<QualityAttribute> getQuality() {
        return quality;
    }

    public void setQuality(List<QualityAttribute> quality) {
        this.quality = quality;
    }

    public List<LicenseInfo> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<LicenseInfo> licenses) {
        this.licenses = licenses;
    }

    public List<VulnerabilityInfo> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<VulnerabilityInfo> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile_hash() {
        return file_hash;
    }

    public void setFile_hash(String file_hash) {
        this.file_hash = file_hash;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
