package com.tuotiansudai.api.util;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppVersionUtil {
    @Value(value = "${mobile.app.version}")
    private  String serverAppVersion;

    public  boolean  isRightAppVersion(String clientAppVersion){
        List<String> clientAppVersionList = Lists.newArrayList(clientAppVersion.split("\\."));
        List<String> serverAppVersionList= Lists.newArrayList(serverAppVersion.split("\\."));
        int maxLength = Math.max(clientAppVersionList.size(), serverAppVersionList.size());

        int clientWeight = 0;
        int serverWeight = 0;

        for(int i = 0;i < clientAppVersionList.size() ; i++){
            clientWeight += Integer.parseInt(clientAppVersionList.get(i)) * Math.pow(10,maxLength-i);
        }
        for(int i = 0;i < serverAppVersionList.size() ; i++){
            serverWeight += Integer.parseInt(serverAppVersionList.get(i)) * Math.pow(10,maxLength-i);
        }

        if(clientWeight < serverWeight){
            return false;
        }
        return true;
    }

}
