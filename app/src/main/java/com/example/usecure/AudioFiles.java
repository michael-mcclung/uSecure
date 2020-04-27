/*packages*/
package com.example.usecure;

/*class used to update audio recordings on firebase realtime database*/
public class AudioFiles {

    /*variables*/
    private String speechToText, audioId;

    /*function used to update firebase realtime database*/
    public AudioFiles(String audioId, String speechToText) {

        getAudioId();
        setAudioId(audioId);

        getSpeechToText();
        setSpeechToText(speechToText);
    }

    /*getters and setters*/
    public String getSpeechToText() {
        return speechToText;
    }

    public void setSpeechToText(String speechToText) {
        this.speechToText = speechToText;
    }

    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }
}
