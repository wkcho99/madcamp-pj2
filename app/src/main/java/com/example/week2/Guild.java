package com.example.week2;

import java.io.Serializable;

public class Guild implements Serializable {
    String name;
    Long hp;
    Integer damage_sum;

    public Guild(String name, Long hp, Integer damage_sum) {
        this.name = name;
        this.hp = hp;
        this.damage_sum = damage_sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHp() {
        return hp;
    }

    public void setHp(Long hp) {
        this.hp = hp;
    }

    public Integer getDamage_sum() {
        return damage_sum;
    }

    public void setDamage_sum(Integer damage_sum) {
        this.damage_sum = damage_sum;
    }
}
