package com.tuotiansudai.service.impl;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.LinkExchangeDto;
import com.tuotiansudai.service.LinkExchangeService;
import com.tuotiansudai.task.TaskConstant;
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
public class LinkExchangeServiceImpl implements LinkExchangeService {
    
    static Logger logger = Logger.getLogger(LinkExchangeServiceImpl.class);

    public static final String LINK_EXCHANGE_KEY = "console:link:list";

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public int findCountByTitle(String title){
        if(StringUtils.isNotEmpty(title)){
            List<LinkExchangeDto> linkExchangeDtoList = redisToLinkExchangeDtoList();
            List<LinkExchangeDto> linkExchangeDtoListByTitle = new ArrayList<LinkExchangeDto>();
            for(int i =0; i <linkExchangeDtoList.size();i++ ){
                if(title != null && linkExchangeDtoList.get(i).getTitle().contains(title)){
                    linkExchangeDtoListByTitle.add(linkExchangeDtoList.get(i));
                }
            }
            return linkExchangeDtoListByTitle.size();

        }else{
            return redisWrapperClient.hgetValuesSeri(LINK_EXCHANGE_KEY).size();
        }
    }

    @Override
    public LinkExchangeDto getLinkExchangeById(String id) {
        LinkExchangeDto linkExchangeDto = new LinkExchangeDto();
        String values = redisWrapperClient.hget(LINK_EXCHANGE_KEY, id);
        if(values != null){
            String[] linkExchangeDtaValues = values.split("\\|");
            linkExchangeDto.setId(Long.parseLong(linkExchangeDtaValues[0]));
            linkExchangeDto.setTitle(linkExchangeDtaValues[1]);
            linkExchangeDto.setLinkUrl(linkExchangeDtaValues[2]);
            linkExchangeDto.setUpdateTime(new Date());
            linkExchangeDto.setCreatedTime(strToDate(linkExchangeDtaValues[4]));
        }
        return linkExchangeDto;
    }

    @Override
    public void create(LinkExchangeDto linkExchangeDto) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(String.valueOf(linkExchangeDto.getId()), linkExchangeDto.convertToString());
        redisWrapperClient.hmset(LINK_EXCHANGE_KEY, map);
    }

    @Override
    public void update(LinkExchangeDto linkExchangeDto) {
        String values = redisWrapperClient.hget(LINK_EXCHANGE_KEY,String.valueOf(linkExchangeDto.getId()));
        if(values != null) {
            String[] linkExchangeDtaValues = values.split("\\|");
            linkExchangeDto.setId(Long.parseLong(linkExchangeDtaValues[0]));
            linkExchangeDto.setTitle(linkExchangeDto.getTitle());
            linkExchangeDto.setLinkUrl(linkExchangeDto.getLinkUrl());
            linkExchangeDto.setUpdateTime(new Date());
            linkExchangeDto.setCreatedTime(strToDate(linkExchangeDtaValues[4]));
            redisWrapperClient.hset(LINK_EXCHANGE_KEY, String.valueOf(linkExchangeDto.getId()), linkExchangeDto.convertToString());
        }
    }

    @Override
    public void delete(LinkExchangeDto linkExchangeDto) {
        redisWrapperClient.hdel(LINK_EXCHANGE_KEY, String.valueOf(linkExchangeDto.getId()));
    }

    @Override
    public List<LinkExchangeDto> getLinkExchangeList(String title, int index, int pageSize) {
        List<LinkExchangeDto> linkExchangeDtoList = this.redisToLinkExchangeDtoList();
        List<LinkExchangeDto> linkExchangeDtoListByTitle = new ArrayList<LinkExchangeDto>();
        List<LinkExchangeDto> linkExchangeDtoListFinal = new ArrayList<LinkExchangeDto>();
        for(int i =0; i <linkExchangeDtoList.size();i++ ){
            if(title != null && linkExchangeDtoList.get(i).getTitle().contains(title)){
                linkExchangeDtoListByTitle.add(linkExchangeDtoList.get(i));
            }
        }
        linkExchangeDtoListFinal.addAll(StringUtils.isNotEmpty(title)?linkExchangeDtoListByTitle:linkExchangeDtoList);
        int toIndex = (index + pageSize) > linkExchangeDtoListFinal.size()?linkExchangeDtoListFinal.size():index + pageSize;
        return (index==0 && pageSize==0)?linkExchangeDtoListFinal:linkExchangeDtoListFinal.subList(index,toIndex);
    }

    @Override
    public List<LinkExchangeDto> getLinkExchangeListByAsc(){
        List<LinkExchangeDto> linkExchangeDtos = this.getLinkExchangeList(null,0,0);
        Collections.reverse(linkExchangeDtos);
        return linkExchangeDtos;
    }
    private  List<LinkExchangeDto> redisToLinkExchangeDtoList(){
        List<LinkExchangeDto> linkExchangeDtoList = new ArrayList<LinkExchangeDto>();
        Map<byte[],byte[]> linkListHkey = redisWrapperClient.hgetAllSeri(LINK_EXCHANGE_KEY);
        for (byte[] key : linkListHkey.keySet()) {
            String linkListHValue = "";
            LinkExchangeDto linkExchangeDto = new LinkExchangeDto();
            try {
                linkListHValue = new String(linkListHkey.get(key),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String[] values = linkListHValue.split("\\|");
            linkExchangeDto.setId(Long.parseLong(values[0]));
            linkExchangeDto.setTitle(values[1]);
            linkExchangeDto.setLinkUrl(values[2]);
            linkExchangeDto.setUpdateTime(strToDate(values[3]));
            linkExchangeDtoList.add(linkExchangeDto);
        }
        Collections.sort(linkExchangeDtoList, new Comparator<LinkExchangeDto>(){
            @Override
            public int compare(LinkExchangeDto o1, LinkExchangeDto o2) {
                return Longs.compare(o2.getId(), o1.getId());
            }
        });
        return linkExchangeDtoList;
    }

    private Date strToDate(String strDate){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            logger.debug("The date conversion errors");
        }
        return date;
    }
}
