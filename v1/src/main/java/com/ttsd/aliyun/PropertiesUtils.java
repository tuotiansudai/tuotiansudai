package com.ttsd.aliyun;

import com.umpay.api.util.ProFileUtil;
import com.umpay.api.util.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

    public class PropertiesUtils {
        private static final String fileName = "common.properties";
        private static final String pro_url_pix = "plat.url";
        private static final String pro_url_wap_pix = "plat.wap.url";

        public PropertiesUtils() {
        }

        public static byte[] getFileByte(String pro) throws IOException {
            byte[] b = (byte[])null;
            InputStream in = null;

            try {
                Properties prop = new Properties();
                in = ProFileUtil.class.getClassLoader().getResourceAsStream("common.properties");
                if(in == null) {
                    throw new RuntimeException("没有找到配置文件common.properties");
                }

                prop.load(in);
                in.close();
                String filepath = prop.getProperty(pro);
                if(filepath == null) {
                    throw new RuntimeException("没有找到配置信息" + pro);
                }

                in = ProFileUtil.class.getClassLoader().getResourceAsStream(filepath);
                if(in == null) {
                    throw new RuntimeException("文件不存在" + filepath);
                }

                b = new byte[20480];
                in.read(b);
            } finally {
                if(in != null) {
                    in.close();
                }

            }

            return b;
        }

        public static String getPro(String pro) {
            InputStream in = null;

            String var5;
            try {
                Properties ex = new Properties();
                in = ProFileUtil.class.getClassLoader().getResourceAsStream("common.properties");
                if(in == null) {
                    throw new RuntimeException("没有找到配置文件common.properties");
                }

                ex.load(in);
                in.close();
                var5 = StringUtil.trim(ex.getProperty(pro));
            } catch (Exception var13) {
                RuntimeException rex = new RuntimeException(var13.getMessage());
                rex.setStackTrace(var13.getStackTrace());
                throw rex;
            } finally {
                if(in != null) {
                    try {
                        in.close();
                    } catch (Exception var12) {
                        RuntimeException rex1 = new RuntimeException(var12.getMessage());
                        rex1.setStackTrace(var12.getStackTrace());
                        throw rex1;
                    }
                }

            }

            return var5;
        }

        public static String getUrlPix() {
            return getPro("plat.url");
        }
    }


