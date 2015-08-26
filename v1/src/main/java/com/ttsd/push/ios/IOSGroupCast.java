package com.ttsd.push.ios;

import com.ttsd.push.IOSNotification;
import org.json.JSONException;

public class IOSGroupCast extends IOSNotification {

    public IOSGroupCast() throws JSONException {
        this.setPredefinedKeyValue("type", "groupcast");
    }
}
