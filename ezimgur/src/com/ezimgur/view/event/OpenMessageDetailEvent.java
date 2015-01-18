package com.ezimgur.view.event;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 1:12 PM
 */
public class OpenMessageDetailEvent {

    public int messageId;
    public String recipient;

    public OpenMessageDetailEvent(int id, String recipient){
        this.messageId = id;
        this.recipient = recipient;
    }
}
