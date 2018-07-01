package com.tuotiansudai.fudian.strategy;


import com.tuotiansudai.fudian.download.DownloadFilesMatch;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DownloadFileMatchDtoParser {

    private static Logger logger = LoggerFactory.getLogger(DownloadFileMatchDtoParser.class);

    @SuppressWarnings(value = "unchecked")
    public static <T extends DownloadFilesMatch> List<T> parse(Class<T> dto, List<String> paramsList) {
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(paramsList)) {
            return list;
        }
        try {
            Map<Integer, String> match = dto.newInstance().match();
            for (String params : paramsList) {
                String[] param = params.split("\\|");
                Map<String, String> map = IntStream.range(0, param.length)
                        .boxed()
                        .collect(Collectors.toMap(match::get, index -> param[index]));
                list.add(new ObjectMapper().convertValue(map, dto));
            }
        }catch (InstantiationException | IllegalAccessException e) {
            logger.error("[match download file] failed to reflection", e);
        }
        return list;
    }
}
