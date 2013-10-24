package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 8:27 PM
 */
public class Notification {
    public int id;
    @SerializedName("account_id")
    public int accountId;
    public boolean viewed;
    public NotificationContent content;

    /*

id	string	The ID for the notification
account_id	integer	The Account ID for the notification
viewed	boolean	Has the user viewed the image yet?
content	Mixed	This can be any other model, currently only using comments and messages

        "data": {
        "replies": [{
            "id": 4511,
            "account_id": 384077,
            "viewed": false,
            "content": {
                "id": 2324,
                "hash": "kLKA6",
                "caption": "new notification, check out bug with messages in a minute.",
                "author": "joshTest",
                "author_id": 384077,
                "ups": 1,
                "downs": 0,
                "points": 1,
                "datetime": "2012-09-24 22:00:20",
                "parent_id": 2322,
                "deleted": false
            }
        }],
        "messages": [{
            "id": 4523,
            "account_id": 384077,
            "viewed": false,
            "content": {
                "id": 620,
                "from": "josh",
                "account_id": 384077,
                "recipient_account_id": 384077,
                "subject": null,
                "body": "Hello, World",
                "timestamp": "5 days ago",
                "parent_id": 619
            }
        }]
    },
     */
}
