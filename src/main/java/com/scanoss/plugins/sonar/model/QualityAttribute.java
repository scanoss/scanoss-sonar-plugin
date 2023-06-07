package com.scanoss.plugins.sonar.model;

public class QualityAttribute {
    private String score;
    private String source;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Float getScoreAsFloat(){
        String scoreString = this.getScore();
        if(scoreString == null || scoreString.isEmpty()){
            return null;
        }
        String[] scoreFields = scoreString.split("/");
        if(scoreFields.length != 2){
            return null;
        }
        Integer score = Integer.valueOf(scoreFields[0]);
        Integer total = Integer.valueOf(scoreFields[1]);
        return  (float) score / (float) total * 10;
    }
}
