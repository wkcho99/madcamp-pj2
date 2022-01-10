package com.example.week2;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String user_id;
    String name;
    Pokemon poke;
    Integer guild;
    long coin;
    long endTime;
    Integer raid_damage;
    Integer raid_times;

    public Integer getRaid_damage() {
        return raid_damage;
    }

    public void setRaid_damage(Integer raid_damage) {
        this.raid_damage = raid_damage;
    }

    public Integer getRaid_times() {
        return raid_times;
    }

    public void setRaid_times(Integer raid_times) {
        this.raid_times = raid_times;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public Integer getGuild() {
        return guild;
    }

    public void setGuild(Integer guild) {
        this.guild = guild;
    }
    public User(String user_id, String name, Pokemon poke, long coin,Integer guild,long endTime, Integer raid_damage, Integer raid_times){
        this.user_id = user_id;
        this.name = name;
        this.poke = poke;
        this.coin = coin;
        this.guild = guild;
        this.endTime = endTime;
        this.raid_damage = raid_damage;
        this.raid_times = raid_times;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pokemon getPoke() {
        return poke;
    }

    public void setPoke(Pokemon poke) {
        this.poke = poke;
    }

    public long getCoin() {
        return coin;
    }

    public void setCoin(long coin) {
        this.coin = coin;
    }

    @Override
    public String toString() {
        return "{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", poke=" + poke +
                ", coin=" + coin +
                '}';
    }

    public String toStringWithoutPoke(){
        return "{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", coin=" + coin +
                '}';
    }

}
