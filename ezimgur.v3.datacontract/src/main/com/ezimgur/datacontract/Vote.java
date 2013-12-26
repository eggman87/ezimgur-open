package com.ezimgur.datacontract;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 8:32 PM
 */
public class Vote implements Parcelable {
    public int ups;
    public int downs;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ups);
        dest.writeInt(downs);
    }

    public static final Creator<Vote> CREATOR = new Creator<Vote>() {
        @Override
        public Vote createFromParcel(Parcel in) {
            Vote vote = new Vote();
            vote.ups = in.readInt();
            vote.downs = in.readInt();
            return vote;
        }

        @Override
        public Vote[] newArray(int size) {
            return new Vote[size];
        }
    };

}
