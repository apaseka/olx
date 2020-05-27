package com.example.olx.model;

public class ParsingRequest {

    private String url;
    private int numberOfLoops;
    private int delayMinutes;
    private int loop=1;

    public ParsingRequest() {
    }

    public ParsingRequest(String url, int numberOfLoops, int delayMinutes) {
        this.url = url;
        this.numberOfLoops = numberOfLoops;
        this.delayMinutes = delayMinutes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumberOfLoops() {
        return numberOfLoops;
    }

    public void setNumberOfLoops(int numberOfLoops) {
        this.numberOfLoops = numberOfLoops;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(int delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }
}
