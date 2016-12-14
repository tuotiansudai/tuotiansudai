package com.tuotiansudai.service;

import com.tuotiansudai.dto.LinkExchangeDto;

import java.util.List;

public interface LinkExchangeService {

    List<LinkExchangeDto> getLinkExchangeList(String title, int index, int pageSize);

    List<LinkExchangeDto> getLinkExchangeListByAsc();
}
