package com.tuotiansudai.message.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.jpush.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.dto.MessageItemDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by gengbeijun on 16/5/23.
 */
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public BasePaginationDataDto<MessageItemDto> findMessagePagination(String title, MessageStatus messageStatus, String createBy, int index, int pageSize) {


        List<MessageModel> items = Lists.newArrayList();

        long count = 0; //messageMapper.findById(1);

        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            items = pointBillMapper.findPointBillPagination(loginName, (index - 1) * pageSize, pageSize, startTime, endTime, businessType);
        }
        List<PointBillPaginationItemDataDto> records = Lists.transform(items, new Function<PointBillModel, PointBillPaginationItemDataDto>() {
            @Override
            public PointBillPaginationItemDataDto apply(PointBillModel view) {
                return new PointBillPaginationItemDataDto(view);
            }
        });

        BasePaginationDataDto<PointBillPaginationItemDataDto> dto = new BasePaginationDataDto<PointBillPaginationItemDataDto>(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }

    @Override
    public long findMessageCount(String title, MessageStatus messageStatus, String createBy) {
        return 0;
    }
}
