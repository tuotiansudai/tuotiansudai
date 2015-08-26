package com.ttsd.push.android;


import com.ttsd.push.AndroidNotification;

public class AndroidBroadcast extends AndroidNotification {
    public AndroidBroadcast() {
        try {
            this.setPredefinedKeyValue("type", "broadcast");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
