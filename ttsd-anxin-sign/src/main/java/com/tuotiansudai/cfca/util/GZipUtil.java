package com.tuotiansudai.cfca.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtil {
    public static final int BUFFER = 1024;

    public static byte[] compress(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GZIPOutputStream gos = new GZIPOutputStream(os);

        int read;
        int count = 0;
        byte[] data = new byte[BUFFER];
        while ((read = is.read(data)) != -1) {
            gos.write(data, 0, read);
            count += read;
        }
        System.out.println("count:" + count);
        gos.finish();
        gos.flush();
        gos.close();

        return os.toByteArray();
    }
}
