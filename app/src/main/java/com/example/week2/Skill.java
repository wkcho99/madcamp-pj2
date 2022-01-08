package com.example.week2;

public class Skill {
    String name;
    Double cool;
    int level;

    public Skill(String name, Double cool, int level, int power) {
        this.name = name;
        this.cool = cool;
        this.level = level;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCool() {
        return cool;
    }

    public void setCool(Double cool) {
        this.cool = cool;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    int power;
}
