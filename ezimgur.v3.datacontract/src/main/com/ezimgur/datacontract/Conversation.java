package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mharris on 1/16/15.
 *
 */
public class Conversation {
    public int id;
    @SerializedName("last_message_preview")
    public String lastMessagePreview;
    public long datetime;
    @SerializedName("with_account_id")
    public int withAccountId;
    @SerializedName("with_account")
    public String withAccountUsername;
    @SerializedName("message_count")
    public int messageCount;
    public List<Message> messages;
    public boolean done;
    public int page;
}
