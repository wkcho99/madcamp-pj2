package com.example.week2;

import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable {
    int id;
    int level;
    int number;
    String name;
    long exp;
    ArrayList<Skill> skills;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    long hunt_reward;
    public Pokemon(int id, int level, int number, String name, long exp, ArrayList<Skill> skills) {
        this.level = level;
        this.id = id;
        this.number = number;
        this.name = name;
        this.exp = exp;
        this.skills = skills;
        this.hunt_reward = hunt_reward;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getHunt_reward() {
        return hunt_reward;
    }

    public void setHunt_reward(long hunt_reward) {
        this.hunt_reward = hunt_reward;
    }
}
