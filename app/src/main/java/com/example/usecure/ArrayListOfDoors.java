/*packages*/
package com.example.usecure;

/* array class used to add a new door and set the
   switch state to 0 (zero)*/
public class ArrayListOfDoors {

    /*Variables*/
    private String doorId;
    private int switchState = 0;
    int currentSwitchState = getswitchState();

    /*function used to update firebase realtime database*/
    public ArrayListOfDoors(String doorId, int switchState) {
        this.doorId = doorId;
        this.switchState = switchState;
    }

    /*used to get switch state from firebase realtime database*/
    public int getswitchState() {
        return switchState;
    }
}
