package com.tuotiansudai.util;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

    static Logger logger = Logger.getLogger(SerializeUtil.class);

    public static byte[] serialize(Object object) {
        try {
            //序列化
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            //反序列化
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}
