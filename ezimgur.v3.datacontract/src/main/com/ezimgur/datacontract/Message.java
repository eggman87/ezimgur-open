package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 8:15 PM
 */
public class Message extends NotificationContent {

    public int id;
    public String from;
    @SerializedName("account_id")
    public int accountId;
    @SerializedName("sender_id")
    public int senderId;
    public String body;
    @SerializedName("conversation_id")
    public int conversationId;
    public long datetime;

}
