package com.ttsd.push.android;

import com.ttsd.push.AndroidNotification;

public class AndroidUnicast extends AndroidNotification {
    public AndroidUnicast() {
        try {
            this.setPredefinedKeyValue("type", "unicast");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}