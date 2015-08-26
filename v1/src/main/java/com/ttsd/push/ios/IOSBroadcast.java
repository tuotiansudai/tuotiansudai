package com.ttsd.push.ios;

import com.ttsd.push.IOSNotification;
import org.json.JSONException;

public class IOSBroadcast extends IOSNotification {

    public IOSBroadcast() throws JSONException {
        this.setPredefinedKeyValue("type", "broadcast");
    }
}
