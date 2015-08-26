package com.ttsd.push.ios;

import com.ttsd.push.IOSNotification;
import org.json.JSONException;

public class IOSUnicast extends IOSNotification {

    public IOSUnicast() throws JSONException {
        this.setPredefinedKeyValue("type", "unicast");
    }
}
