package ru.test.gitusersclient;

import java.util.List;
import java.util.Map;


public class MessageEvent{
    public  List<User> mUserlist;

    public MessageEvent(List<User> mUserlist) {
        this.mUserlist = mUserlist;
    }


    public List<User> getResult() {
        return mUserlist;
    }
}
