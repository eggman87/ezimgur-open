package com.ezimgur.view.adapter;

import com.ezimgur.datacontract.Message;

import java.util.Comparator;

/**
 * Created by mharris on 1/17/15.
 *
 */
public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message lhs, Message rhs) {
        if (lhs.datetime > rhs.datetime)
            return -1;
        else if (lhs.datetime < rhs.datetime)
            return 1;
        else
            return 0;
    }
}
