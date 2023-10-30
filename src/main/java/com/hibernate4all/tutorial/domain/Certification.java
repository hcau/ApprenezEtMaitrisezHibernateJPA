package com.hibernate4all.tutorial.domain;

public enum Certification{

    TOUS_PUBLIC(1, "Tous public"),
    INTERDIT_MOINS_12(2, "Interdit au moins de 12 ans"),
    INTERDIT_MOINS_16(3, "Interdit au moins de 16 ans"),
    INTERDIT_MOINS_18(4, "Interdit au moins de 18 ans");

    private Integer key;
    private String description;



    Certification(Integer key, String description) {
        this.key = key;
        this.description = description;
    }


    public Integer getKey() {
        return key;
    }
    public void setKey(Integer key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}