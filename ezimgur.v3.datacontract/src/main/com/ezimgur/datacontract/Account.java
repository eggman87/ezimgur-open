package com.ezimgur.datacontract;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:01 PM
 */
public class Account {

    public int id;
    public String url;
    public String bio;
    public float reputation;
    public String created;
/*
id	integer	The account id for the username requested.
url	String	The account username, will be the same as requested in the URL
bio	String	A basic description the user has filled out
reputation	Float	The reputation for the account, in it's numerical format.
created	String	The epoch time of account creation
*/
}
