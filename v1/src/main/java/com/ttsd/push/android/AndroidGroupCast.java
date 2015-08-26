package com.ttsd.push.android;

import com.ttsd.push.AndroidNotification;

public class AndroidGroupCast extends AndroidNotification {
    public AndroidGroupCast() {
        try {
            this.setPredefinedKeyValue("type", "groupcast");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
