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
    @SerializedName("recipient_account_id")
    public int recipientAccountId;
    public String subject;
    public String body;
    @SerializedName("timestamp")
    public String timeStamp;
    @SerializedName("parent_id")
    public int parentId;

    /*

id	string	The ID for the message
from	string	Account Username of person sending the message
account_id	integer	The account ID of the person sending the message
recipient_account_id	integer	The account id of the person whom received the message
subject	string	The subject of the message.
body	string	The text of body of the message
timestamp	string	The formatted time of the message from now.
parent_id	integer	Parent ID is the message id first message in the thread

        "data": {
        "id": 614,
        "from": "joshTest",
        "account_id": 384077,
        "recipient_account_id": 384077,
        "subject": null,
        "body": "Hello, World",
        "timestamp": "5 days ago",
        "parent_id": 614
    },
     */
}
