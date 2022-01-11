package com.example.week2;

import java.io.Serializable;

public class Skill implements Serializable {
    int id;
    String name;
    Double cool;
    Integer skillcoin;
    int level;
    int power;
    Long start;
    int damage;

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getSkillcoin() {
        return skillcoin;
    }

    public void setSkillcoin() {
        this.skillcoin =(this.id%8)*this.level;
    }

    public Skill(int id, String name, Double cool, int level, int power) {
        this.id = id;
        this.name = name;
        this.cool = cool;
        this.level = level;
        this.power = power;
        this.skillcoin = 0;
        this.start = (long)0;
        this.damage = (this.id % 8 +1) * (this.level-1) + this.power;
        setSkillcoin();
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", level=" + level +
                '}';
    }
}
