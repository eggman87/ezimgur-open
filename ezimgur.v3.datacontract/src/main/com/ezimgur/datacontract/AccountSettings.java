package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:03 PM
 */
public class AccountSettings {

    public String email;
    @SerializedName("high_quality")
    public boolean highQuality;
    @SerializedName("public_images")
    public boolean publicImages;
    @SerializedName("album_privacy")
    public boolean albumPrivacy;
    @SerializedName("active_emails")
    public List<String> activeEmails;
    @SerializedName("messaging_enabled")
    public boolean messagingEnabled;
    @SerializedName("blocked_users")
    public List<BlockedUser> blockedUsers;

    /*

E-mail	string	The users email address
high_quality	boolean	The users ability to upload higher quality images, there will be less compression
public_images	boolean	Automatically allow all images to be publicly accessible
album_privacy	boolean	Mark all newly created albums as private
pro_expiration	string	False if not a pro user, their expiration date if they are.
active_emails	Array of Strings	The email addresses that have been activated to allow uploading
messaging_enabled	boolean	If the user is accepting incoming messages or not
blocked_users	Array of objects	An array of users that have been blocked from messaging, the object is blocked_id and blocked_url.

        "data": {
        "email": "josh@imgur.com",
        "high_quality": true,
        "public_images": false,
        "album_privacy": false,
        "pro_expiration": false,
        "active_emails": [],
        "messaging_enabled": true,
        "blocked_users": [{
            "blocked_id" : 384077,
            "blocked_url": "joshTest"
        }]
    },
     */
}
