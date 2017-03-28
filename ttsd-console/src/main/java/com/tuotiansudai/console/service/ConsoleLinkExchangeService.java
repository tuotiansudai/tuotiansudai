package com.tuotiansudai.console.service;

import com.google.common.primitives.Longs;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.LinkExchangeDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ConsoleLinkExchangeService {

    static Logger logger = Logger.getLogger(ConsoleLinkExchangeService.class);

    public static final String LINK_EXCHANGE_KEY = "console:link:list";

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public int findCountByTitle(String title) {
        if (StringUtils.isNotEmpty(title)) {
            List<LinkExchangeDto> linkExchangeDtoList = redisToLinkExchangeDtoList();
            List<LinkExchangeDto> linkExchangeDtoListByTitle = new ArrayList<>();
            for (int i = 0; i < linkExchangeDtoList.size(); i++) {
                if (linkExchangeDtoList.get(i).getTitle().contains(title)) {
                    linkExchangeDtoListByTitle.add(linkExchangeDtoList.get(i));
                }
            }
            return linkExchangeDtoListByTitle.size();

        } else {
            return redisWrapperClient.hgetValuesSeri(LINK_EXCHANGE_KEY).size();
        }
    }

    public LinkExchangeDto getLinkExchangeById(String id) {
        LinkExchangeDto linkExchangeDto = new LinkExchangeDto();
        String values = redisWrapperClient.hget(LINK_EXCHANGE_KEY, id);
        if (values != null) {
            String[] linkExchangeDtaValues = values.split("\\|");
            linkExchangeDto.setId(Long.parseLong(linkExchangeDtaValues[0]));
            linkExchangeDto.setTitle(linkExchangeDtaValues[1]);
            linkExchangeDto.setLinkUrl(linkExchangeDtaValues[2]);
            linkExchangeDto.setUpdateTime(new Date());
            linkExchangeDto.setCreatedTime(strToDate(linkExchangeDtaValues[4]));

            final int linkExchangeDtoStringOriginSize = 5;  //旧数据长度是5
            //noFollow字段是后加入的，需要判断长度用来兼容旧数据
            if (linkExchangeDtoStringOriginSize < linkExchangeDtaValues.length) {
                linkExchangeDto.setNoFollow(Boolean.valueOf(linkExchangeDtaValues[5]));
            } else {
                linkExchangeDto.setNoFollow(false);
            }
            //WebSiteTypes是后加入的字段，需要判断长度用来兼容旧数据
            if(linkExchangeDtaValues.length < 6 ){
                linkExchangeDto.setWebSiteTypes("");
            }else{
                linkExchangeDto.setWebSiteTypes(linkExchangeDtaValues[6]);
            }
        }
        return linkExchangeDto;
    }

    public void create(LinkExchangeDto linkExchangeDto) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(String.valueOf(linkExchangeDto.getId()), linkExchangeDto.convertToString());
        redisWrapperClient.hmset(LINK_EXCHANGE_KEY, map);
    }

    public void update(LinkExchangeDto linkExchangeDto) {
        String values = redisWrapperClient.hget(LINK_EXCHANGE_KEY, String.valueOf(linkExchangeDto.getId()));
        if (values != null) {
            String[] linkExchangeDtaValues = values.split("\\|");
            linkExchangeDto.setId(Long.parseLong(linkExchangeDtaValues[0]));
            linkExchangeDto.setTitle(linkExchangeDto.getTitle());
            linkExchangeDto.setLinkUrl(linkExchangeDto.getLinkUrl());
            linkExchangeDto.setUpdateTime(new Date());
            linkExchangeDto.setCreatedTime(strToDate(linkExchangeDtaValues[4]));
            redisWrapperClient.hset(LINK_EXCHANGE_KEY, String.valueOf(linkExchangeDto.getId()), linkExchangeDto.convertToString());
        }
    }

    public void delete(LinkExchangeDto linkExchangeDto) {
        redisWrapperClient.hdel(LINK_EXCHANGE_KEY, String.valueOf(linkExchangeDto.getId()));
    }

    private List<LinkExchangeDto> redisToLinkExchangeDtoList() {
        List<LinkExchangeDto> linkExchangeDtoList = new ArrayList<LinkExchangeDto>();
        Map<byte[], byte[]> linkListHkey = redisWrapperClient.hgetAllSeri(LINK_EXCHANGE_KEY);
        for (byte[] key : linkListHkey.keySet()) {
            String linkListHValue = "";
            LinkExchangeDto linkExchangeDto = new LinkExchangeDto();
            try {
                linkListHValue = new String(linkListHkey.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String[] values = linkListHValue.split("\\|");
            linkExchangeDto.setId(Long.parseLong(values[0]));
            linkExchangeDto.setTitle(values[1]);
            linkExchangeDto.setLinkUrl(values[2]);
            linkExchangeDto.setUpdateTime(strToDate(values[3]));
            final int linkExchangeDtoStringOriginSize = 5;  //旧数据长度是5
            //noFollow字段是后加入的，需要判断长度用来兼容旧数据
            if (linkExchangeDtoStringOriginSize < values.length) {
                linkExchangeDto.setNoFollow(Boolean.valueOf(values[5]));
            } else {
                linkExchangeDto.setNoFollow(false);
            }
            //WebSiteTypes是后加入的字段，需要判断长度用来兼容旧数据
            if(values.length < 6 ){
                linkExchangeDto.setWebSiteTypes("");
            }else{
                linkExchangeDto.setWebSiteTypes(values[6]);
            }
            linkExchangeDtoList.add(linkExchangeDto);
        }
        Collections.sort(linkExchangeDtoList, (o1, o2) -> Longs.compare(o2.getId(), o1.getId()));
        return linkExchangeDtoList;
    }

    private Date strToDate(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            logger.info("The date conversion errors");
        }
        return date;
    }
}
