package com.example.wads;

public class ComplaintModel {
    private String content;
    private String fromName;
    private String aboutEmail;

    public ComplaintModel(){}

    public ComplaintModel(String content, String fromName, String aboutEmail) {
        this.content = content;
        this.fromName = fromName;
        this.aboutEmail = aboutEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String report) {
        this.content = report;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getAboutEmail() {
        return aboutEmail;
    }

    public void setAboutEmail(String aboutEmail) {
        this.aboutEmail = aboutEmail;
    }
}
