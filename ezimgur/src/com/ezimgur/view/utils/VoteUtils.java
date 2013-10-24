package com.ezimgur.view.utils;

import android.content.Context;
import com.ezimgur.R;
import com.ezimgur.datacontract.VoteType;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/22/12
 * Time: 11:23 AM
 */
public class VoteUtils {

    //could just use enum.tostring instead of manually using these but want it to be flexible to API change.
    private static final String VOTE_UP = "up";
    private static final String VOTE_DOWN = "down";
    private static final String VOTE_NONE = "none";

    public static VoteType getVoteTypeFromString(String vote) {
        if (vote != null  && vote.equalsIgnoreCase(VOTE_UP))
            return VoteType.UP;
        else if (vote != null  && vote.equalsIgnoreCase(VOTE_DOWN))
            return VoteType.DOWN;
        else
            return VoteType.NONE;
    }

    public static String getUpVoteString(){
        return VOTE_UP;
    }

    public static String getDownVoteString() {
        return VOTE_DOWN;
    }

    public static int getVoteSelectedColor(Context context) {
        return context.getResources().getColor(R.color.Gold);
    }

    public static int getVoteEmptyColor(Context context) {
        return context.getResources().getColor(R.color.Gainsboro);
    }


}
