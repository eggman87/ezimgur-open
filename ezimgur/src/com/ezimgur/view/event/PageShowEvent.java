package com.ezimgur.view.event;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/26/13
 * Time: 2:47 PM
 */
public class PageShowEvent {
    public int position;
    public String id;

    public PageShowEvent(int position, String id) {
        this.position = position;
        this.id = id;
    }
}
