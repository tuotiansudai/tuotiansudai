package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppHelpCenterCategoryService;
import com.tuotiansudai.enums.HelpCategory;
import com.tuotiansudai.repository.mapper.HelpCenterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppHelpCenterCategoryListServiceImpl implements MobileAppHelpCenterCategoryService {

    @Autowired
    private HelpCenterMapper helpCenterMapper;

    @Override
    public BaseResponseDto<HelpCenterCategoryListResponseDataDto> generateHelpCenterCategoryList(HelpCenterCategoryRequestDto helpCenterCategoryRequestDto){

        List<String> helpCenterCategorys = helpCenterMapper.findAllHelpCenterCategory();

        List<HelpCenterCategoryResponseDataDto> helpCenterCategoryResponseDataDtoList = Lists.newArrayList();
        if(helpCenterCategorys != null) {
            for (String helpCenterCategory : helpCenterCategorys) {
                HelpCenterCategoryResponseDataDto helpCenterCategoryResponseDataDto = new HelpCenterCategoryResponseDataDto();
                helpCenterCategoryResponseDataDto.setTitle(HelpCategory.getNameByCode(helpCenterCategory.toLowerCase()));
                helpCenterCategoryResponseDataDto.setCategoryType(helpCenterCategory);
                helpCenterCategoryResponseDataDtoList.add(helpCenterCategoryResponseDataDto);
            }
        }
        HelpCenterCategoryListResponseDataDto dataDto = new HelpCenterCategoryListResponseDataDto();
        dataDto.setCategoryList(helpCenterCategoryResponseDataDtoList);
        BaseResponseDto<HelpCenterCategoryListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);
        return dto;
    }

}
