package com.tuotiansudai.ask.service;

import com.google.common.primitives.Longs;
import com.tuotiansudai.ask.dto.LinkExchangeDto;
import com.tuotiansudai.client.RedisWrapperClient;
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
public class ExchangeLinkService {

    private static Logger logger = Logger.getLogger(ExchangeLinkService.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String LINK_EXCHANGE_KEY = "console:link:list";

    private Date strToDate(String strDate) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            logger.debug("The date conversion errors");
        }
        return date;
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
            linkExchangeDtoList.add(linkExchangeDto);
        }
        Collections.sort(linkExchangeDtoList, new Comparator<LinkExchangeDto>() {
            @Override
            public int compare(LinkExchangeDto o1, LinkExchangeDto o2) {
                return Longs.compare(o2.getId(), o1.getId());
            }
        });
        return linkExchangeDtoList;
    }

    private List<LinkExchangeDto> getLinkExchangeList(String title, int index, int pageSize) {
        List<LinkExchangeDto> linkExchangeDtoList = this.redisToLinkExchangeDtoList();
        List<LinkExchangeDto> linkExchangeDtoListByTitle = new ArrayList<LinkExchangeDto>();
        List<LinkExchangeDto> linkExchangeDtoListFinal = new ArrayList<LinkExchangeDto>();
        for (int i = 0; i < linkExchangeDtoList.size(); i++) {
            if (title != null && linkExchangeDtoList.get(i).getTitle().contains(title)) {
                linkExchangeDtoListByTitle.add(linkExchangeDtoList.get(i));
            }
        }
        linkExchangeDtoListFinal.addAll(StringUtils.isNotEmpty(title) ? linkExchangeDtoListByTitle : linkExchangeDtoList);
        int toIndex = (index + pageSize) > linkExchangeDtoListFinal.size() ? linkExchangeDtoListFinal.size() : index + pageSize;
        return (index == 0 && pageSize == 0) ? linkExchangeDtoListFinal : linkExchangeDtoListFinal.subList(index, toIndex);
    }

    public List<LinkExchangeDto> getLinkExchangeListByAsc() {
        List<LinkExchangeDto> linkExchangeDtos = this.getLinkExchangeList(null, 0, 0);
        Collections.reverse(linkExchangeDtos);
        return linkExchangeDtos;
    }
}
