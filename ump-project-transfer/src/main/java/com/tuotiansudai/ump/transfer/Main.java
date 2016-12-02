package com.tuotiansudai.ump.transfer;

import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Objects;

public class Main {

    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws FileNotFoundException {
        ProjectSearch projectSearch = new ProjectSearch();
        ProjectTransfer projectTransfer = new ProjectTransfer();
//        try {
//            String result = projectSearch.search("23575243358288");
//            if (result != null) {
//                projectTransfer.transfer("23575243358288", result);
//            }
//        } catch (IOException | RetDataException | ReqDataException e) {
//            logger.error(e.getLocalizedMessage(), e);
//        }

        try (BufferedReader br = new BufferedReader(new FileReader("/Users/xdgao/Downloads/loan-list"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String result = projectSearch.search(line);
                if (result != null && !"0".equals(result)) {
                    projectTransfer.transfer(line, result);
                }
            }
        } catch (IOException | RetDataException | ReqDataException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
