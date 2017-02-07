package com.tuotiansudai.util;

import org.apache.log4j.Logger;

import java.io.*;

public class SerializeUtil {

    private final static Logger logger = Logger.getLogger(SerializeUtil.class);

    public static byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            //序列化
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex) {
                    logger.error(ex.getLocalizedMessage(), ex);
                }
            }
            try {
                bos.close();
            } catch (IOException ex) {
                logger.error(ex.getLocalizedMessage(), ex);
            }
        }
        return null;
    }

    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        ObjectInputStream ois = null;
        try {
            //反序列化
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
        return null;
    }
}
