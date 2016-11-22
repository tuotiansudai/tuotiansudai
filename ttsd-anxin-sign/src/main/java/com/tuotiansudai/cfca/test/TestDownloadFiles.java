package com.tuotiansudai.cfca.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cfca.sadk.algorithm.common.PKIException;
import com.tuotiansudai.cfca.connector.HttpConnector;
import com.tuotiansudai.cfca.constant.Request;
import com.tuotiansudai.cfca.util.TimeUtil;

public class TestDownloadFiles {
    public static void main(String[] args) throws PKIException, FileNotFoundException {
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.init();

        String contractNos = "MM20160519000000002@MM20160519000000003@MM20160519000000005";
        byte[] fileBtye = httpConnector.getFile("platId/" + Request.PLAT_ID + "/contractNos/" + contractNos + "/batchDownloading");
        if (fileBtye == null || fileBtye.length == 0) {
            return;
        }

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String filePath = "./file";
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + TimeUtil.getCurrentTime(TimeUtil.FORMAT_14) + ".zip");
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(fileBtye);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
