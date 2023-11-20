package com.personal.blog.utils;


public enum EventMetadata {
    Articlepush("articlepush"),
    Attention("attention"),
    Comment("comment"),
    Like("like"),
    Privateletter("privateletter");
    private String name;

    public String getName() {
        return name;
    }

    EventMetadata(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    }
