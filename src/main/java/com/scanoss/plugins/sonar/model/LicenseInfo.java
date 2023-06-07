package com.scanoss.plugins.sonar.model;

public class LicenseInfo {

    private String name;
    private String copyleft;
    private String patent_hints;
    private String source;
    private String incompatible_with;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCopyleft() {
        return copyleft;
    }

    public void setCopyleft(String copyleft) {
        this.copyleft = copyleft;
    }

    public Boolean isCopyleft(){
        String copyleftValue = this.getCopyleft();
        if(copyleftValue == null)
            return false;
        return copyleftValue.toLowerCase().equals("yes");
    }

    public String getPatent_hints() {
        return patent_hints;
    }

    public void setPatent_hints(String patent_hints) {
        this.patent_hints = patent_hints;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIncompatible_with() {
        return incompatible_with;
    }

    public void setIncompatible_with(String incompatible_with) {
        this.incompatible_with = incompatible_with;
    }
}
