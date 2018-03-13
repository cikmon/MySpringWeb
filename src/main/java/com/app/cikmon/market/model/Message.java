package com.app.cikmon.market.model;

/**
 * Simple JavaBean domain object that represents a Massage.
 *
 * @author cikmon
 * @version 1.0
 */

public class Message {
    private String type;
    private String message;

    public Message(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
