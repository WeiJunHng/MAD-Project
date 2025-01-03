package com.example.madproject;

public class FAQ {
    private String question;
    private String answer;
    private boolean isExpanded;

    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.isExpanded = false;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
