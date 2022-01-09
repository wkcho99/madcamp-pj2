package com.example.week2;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String user_id;
    String name;
    Pokemon poke;
    long coin;

    public User(String user_id, String name, Pokemon poke, long coin){
        this.user_id = user_id;
        this.name = name;
        this.poke = poke;
        this.coin = coin;
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
