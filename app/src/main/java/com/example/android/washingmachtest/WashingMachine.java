package com.example.android.washingmachtest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anuj on 10/1/17.
 */

public class WashingMachine implements Parcelable {

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<WashingMachine> CREATOR = new Parcelable.Creator<WashingMachine>() {
        public WashingMachine createFromParcel(Parcel in) {
            return new WashingMachine(in);
        }

        public WashingMachine[] newArray(int size) {
            return new WashingMachine[size];
        }
    };
    private int mWashingMachineId;
    private boolean mState;
    private long mTimeLeft;
    private long mUserId;
    private String mUserName;
    private String mUserRoom;

    WashingMachine(int washingMachineId, boolean state) {
        mWashingMachineId = washingMachineId;
        mState = state;
    }

    WashingMachine(int washingMachineId, boolean state, long timeLeft, long UserId, String userName, String userRoom) {
        mWashingMachineId = washingMachineId;
        mState = state;
        mTimeLeft = timeLeft;
        mUserName = userName;
        mUserRoom = userRoom;
        mUserId = UserId;
    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private WashingMachine(Parcel in) {
        mWashingMachineId = in.readInt();
        mState = in.readByte() != 0;
        mTimeLeft = in.readLong();
        mUserName = in.readString();
        mUserRoom = in.readString();
    }

    public int getWashingMachineId() {
        return mWashingMachineId;
    }

    public boolean getState() {
        return mState;
    }

    public long getTimeLeft() {
        return mTimeLeft;
    }

    public long getmUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserRoom() {
        return mUserRoom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mWashingMachineId);
        out.writeByte((byte) (mState ? 1 : 0));
        out.writeLong(mTimeLeft);
        out.writeString(mUserName);
        out.writeString(mUserRoom);

    }
}
