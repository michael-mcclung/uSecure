package com.example.usecure;

import android.widget.TextView;

public class AudioFiles {

    private String nameOfTextFile;
    private String speechToText;
    private String audioId;

    public AudioFiles() {

    }

    public AudioFiles(String audioId, String nameOfTextFile, String speechToText) {
        this.nameOfTextFile = nameOfTextFile;
        this.speechToText = speechToText;
        this.audioId = audioId;
    }

    public String getNameOfTextFile() {
        return nameOfTextFile;
    }

    public String getSpeechToText() {
        return speechToText;
    }
}
