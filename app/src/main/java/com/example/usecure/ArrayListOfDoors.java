package com.example.usecure;


public class ArrayListOfDoors {

    private String doorName;
    private String audioId;
    private int switchState = 0;

    public ArrayListOfDoors() {

    }

    public ArrayListOfDoors(String audioId, String doorName, int switchState) {
        this.doorName = doorName;
        this.audioId = audioId;
        this.switchState = switchState;
    }

    public String getdoorName() {
        return doorName;
    }

    public int getswitchState() {
        return switchState;
    }
}
