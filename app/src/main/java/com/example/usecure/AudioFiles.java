/*packages*/
package com.example.usecure;

/*class used to update audio recordings on firebase realtime database*/
public class AudioFiles {

    /*variables*/
    private String nameOfTextFile, speechToText, audioId;

    /*function used to update firebase realtime database*/
    public AudioFiles(String audioId, String nameOfTextFile, String speechToText) {
        this.nameOfTextFile = nameOfTextFile;
        this.speechToText = speechToText;
        this.audioId = audioId;
    }
}
