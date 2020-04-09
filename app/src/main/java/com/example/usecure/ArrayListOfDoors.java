package com.example.usecure;


public class ArrayListOfDoors {

    private String doorId;
    private int switchState = 0;
    int currentSwitchState = getswitchState();

    public ArrayListOfDoors(String doorId, int switchState) {
        this.doorId = doorId;
        this.switchState = switchState;
    }

    public ArrayListOfDoors(int currentSwitchState){
        this.currentSwitchState = currentSwitchState;
    }
    public int getswitchState() {
        return switchState;
    }
}
