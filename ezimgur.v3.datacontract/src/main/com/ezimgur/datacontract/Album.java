package com.ezimgur.datacontract;

import java.util.Date;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:32 PM
 */
public class Album {
    public String id;
    public String title;
    public String description ;
    public String privacy;
    public String cover;
    public int order;
    public String layout;
    public Date dateCreated;
    public String link;
    public List<Image> images;


    /*

id	string	The ID for the album
title	string	The title of the album
description	string	The description of the album
privacy	string	The privacy level of the album, you can only view public if not logged in as album owner
cover	string	The Image ID for the Albums cover.
order	integer
layout	string	The view layout of the album.
datetime	integer	The the creation timestamp, epoch time.
link	string	The URL link to the album
        "data": {
        "id": "DMQBe",
        "title": "Created By API",
        "description": null,
        "privacy": "public",
        "cover": "d7z2x",
        "order": 0,
        "layout": "blog",
        "datetime": 1347058832,
        "link": "https://imgur.com/a/DMQBe"
    },
     */
}
