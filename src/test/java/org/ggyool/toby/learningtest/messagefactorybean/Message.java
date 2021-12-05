package org.ggyool.toby.learningtest.messagefactorybean;

public class Message {

    private String text;

    private Message(String text) {
        this.text = text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }

    public String getText() {
        return text;
    }
}
