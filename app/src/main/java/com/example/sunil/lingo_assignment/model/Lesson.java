
package com.example.sunil.lingo_assignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lesson {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("conceptName")
    @Expose
    private String conceptName;
    @SerializedName("pronunciation")
    @Expose
    private String pronunciation;
    @SerializedName("targetScript")
    @Expose
    private String targetScript;
    @SerializedName("audio_url")
    @Expose
    private String audioUrl;

    /**
     * No args constructor for use in serialization
     *
     */
    public Lesson() {
    }

    /**
     *
     * @param audioUrl
     * @param pronunciation
     * @param targetScript
     * @param type
     * @param conceptName
     */
    public Lesson(String type, String conceptName, String pronunciation, String targetScript, String audioUrl) {
        super();
        this.type = type;
        this.conceptName = conceptName;
        this.pronunciation = pronunciation;
        this.targetScript = targetScript;
        this.audioUrl = audioUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getTargetScript() {
        return targetScript;
    }

    public void setTargetScript(String targetScript) {
        this.targetScript = targetScript;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

}
