package com.tuotiansudai.point.repository.dto;

import com.tuotiansudai.dto.BasePaginationDataDto;

import java.util.List;

/**
 * Created by gengbeijun on 16/2/27.
 */
public class PointBillPaginationDataDto extends BasePaginationDataDto<PointBillPaginationItemDataDto> {
    public PointBillPaginationDataDto(int index, int pageSize, long count, List<PointBillPaginationItemDataDto> records) {
        super(index, pageSize, count, records);
    }

}
