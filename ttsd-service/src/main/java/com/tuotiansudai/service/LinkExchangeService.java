package com.tuotiansudai.service;

import com.tuotiansudai.dto.LinkExchangeDto;

import java.util.List;

public interface LinkExchangeService {

    int findCountByTitle(String title);

    LinkExchangeDto getLinkExchangeById(String id);

    void create(LinkExchangeDto linkExchangeDto);

    void update(LinkExchangeDto linkExchangeDto);

    void delete(LinkExchangeDto linkExchangeDto);

    List<LinkExchangeDto> getLinkExchangeList(String title, int index, int pageSize);

    List<LinkExchangeDto> getLinkExchangeListByAsc();
}
