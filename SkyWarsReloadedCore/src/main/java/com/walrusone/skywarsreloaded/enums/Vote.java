package com.walrusone.skywarsreloaded.enums;

import java.util.ArrayList;
import java.util.Random;

public enum Vote {
    TIMERANDOM,
    TIMEDAWN,
    TIMENOON,
    TIMEDUSK,
    TIMEMIDNIGHT,
    WEATHERRANDOM,
    WEATHERSUN,
    WEATHERRAIN,
    WEATHERSNOW,
    WEATHERTHUNDER,
    CHESTRANDOM,
    CHESTBASIC,
    CHESTNORMAL,
    CHESTOP,
    CHESTSCAVENGER,
    MODIFIERRANDOM,
    MODIFIERSPEED,
    MODIFIERJUMP,
    MODIFIERSTRENGTH,
    MODIFIERNONE,
    HEALTHRANDOM,
    HEALTHFIVE,
    HEALTHTEN,
    HEALTHFIFTEEN,
    HEALTHTWENTY;

    public static Vote getRandom(String type) {
        ArrayList<Vote> list = new ArrayList<>();
        if (type.equalsIgnoreCase("time")) {
            list.add(Vote.TIMEDUSK);
            list.add(Vote.TIMEDAWN);
            list.add(Vote.TIMENOON);
            list.add(Vote.TIMEMIDNIGHT);
        } else if (type.equalsIgnoreCase("weather")) {
            list.add(Vote.WEATHERSUN);
            list.add(Vote.WEATHERRAIN);
            list.add(Vote.WEATHERSNOW);
            list.add(Vote.WEATHERTHUNDER);
        } else if (type.equalsIgnoreCase("chest")) {
            list.add(Vote.CHESTBASIC);
            list.add(Vote.CHESTNORMAL);
            list.add(Vote.CHESTOP);
            list.add(Vote.CHESTSCAVENGER);
        } else if (type.equalsIgnoreCase("modifier")) {
            list.add(Vote.MODIFIERSPEED);
            list.add(Vote.MODIFIERJUMP);
            list.add(Vote.MODIFIERSTRENGTH);
            list.add(Vote.MODIFIERNONE);
        } else if (type.equalsIgnoreCase("health")) {
            list.add(Vote.HEALTHFIVE);
            list.add(Vote.HEALTHTEN);
            list.add(Vote.HEALTHFIFTEEN);
            list.add(Vote.HEALTHTWENTY);
        }
        return list.get(new Random().nextInt(4));
    }

    public static Vote getByValue(String name, Vote defValue) {
        try {
            return Vote.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return defValue;
        }
    }
}