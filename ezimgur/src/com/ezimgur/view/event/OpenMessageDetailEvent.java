package com.ezimgur.view.event;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 1:12 PM
 */
public class OpenMessageDetailEvent {

    public int messageId;

    public OpenMessageDetailEvent(int id){
        this.messageId = id;
    }
}
