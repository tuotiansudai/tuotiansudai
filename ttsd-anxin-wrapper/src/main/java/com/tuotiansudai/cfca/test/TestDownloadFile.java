package com.tuotiansudai.cfca.test;

import com.tuotiansudai.cfca.connector.AnxinClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestDownloadFile {
    public static void main(String[] args) {
        AnxinClient anxinClient = AnxinClient.getClient();

        String contractNo = "MM20160721000000041";
        byte[] fileBtye = anxinClient.getContractFile(contractNo);
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
            file = new File(filePath + File.separator + contractNo + ".pdf");
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
