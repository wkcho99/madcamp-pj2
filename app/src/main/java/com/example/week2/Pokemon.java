package com.example.week2;

import java.util.ArrayList;

public class Pokemon{
    int level;
    String name[] = new String[3];
    long exp;
    ArrayList<Skill> skills;
    long hunt_reward;
    public Pokemon(int level, String[] name, long exp, ArrayList<Skill> skills) {
        this.level = level;
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

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
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

    public long getHunt_reward() {
        return hunt_reward;
    }

    public void setHunt_reward(long hunt_reward) {
        this.hunt_reward = hunt_reward;
    }
}
