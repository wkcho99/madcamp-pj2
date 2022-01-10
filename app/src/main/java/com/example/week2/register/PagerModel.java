package com.example.week2.register;

import com.example.week2.R;

public class PagerModel {

    int id;
    String name;
    int number;

    public PagerModel(int id, String name, int number){
        this.id = id;
        this.name = name;
        this.number = number;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
