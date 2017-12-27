package com.tuotiansudai.point.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.exception.ChannelPointDataValidationException;
import com.tuotiansudai.point.repository.dto.ChannelPointDetailDto;
import com.tuotiansudai.point.repository.dto.ChannelPointDetailPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.ChannelPointPaginationItemDataDto;
import com.tuotiansudai.point.repository.mapper.ChannelPointDetailMapper;
import com.tuotiansudai.point.repository.mapper.ChannelPointMapper;
import com.tuotiansudai.point.repository.model.ChannelPointDetailModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelPointServiceImpl {
    static Logger logger = Logger.getLogger(ChannelPointServiceImpl.class);
    @Autowired
    private ChannelPointMapper channelPointMapper;
    @Autowired
    private ChannelPointDetailMapper channelPointDetailMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;


    public BasePaginationDataDto<ChannelPointPaginationItemDataDto> getChannelPointList(int index, int pageSize) {
        long count = channelPointMapper.findCountByPagination();
        List<ChannelPointPaginationItemDataDto> itemDataDtos = channelPointMapper.findByPagination(PaginationUtil.calculateOffset(index, pageSize, count), pageSize)
                .stream()
                .map(channelPointModel -> new ChannelPointPaginationItemDataDto(channelPointModel))
                .collect(Collectors.toList());

        BasePaginationDataDto paginationDataDto = new BasePaginationDataDto(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, itemDataDtos);
        paginationDataDto.setStatus(true);
        return paginationDataDto;

    }

    public BasePaginationDataDto<ChannelPointDetailPaginationItemDataDto> getChannelPointDetailList(long channelPointId, String channel, String userNameOrMobile, Boolean success, int index, int pageSize) {
        long count = channelPointDetailMapper.findCountByPagination(channelPointId, channel, userNameOrMobile, success);
        List<ChannelPointDetailPaginationItemDataDto> itemDatas = channelPointDetailMapper.findByPagination(channelPointId, channel, userNameOrMobile, success,
                PaginationUtil.calculateOffset(index, pageSize, count), pageSize)
                .stream().map(channelPointDetailModel -> new ChannelPointDetailPaginationItemDataDto(channelPointDetailModel))
                .collect(Collectors.toList());

        BasePaginationDataDto paginationDataDto = new BasePaginationDataDto(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, itemDatas);
        paginationDataDto.setStatus(true);
        return paginationDataDto;
    }

    public long getSumTotalPoint() {
        return channelPointMapper.findSumTotalPoint();
    }

    public long getSumHeadCount() {
        return channelPointMapper.findSumHeadCount();
    }

    public List<String> findAllChannel() {
        return channelPointDetailMapper.findAllChannel();
    }

    public void checkFileName(MultipartFile multipartFile) throws ChannelPointDataValidationException {
        if (null == multipartFile) {
            throw new ChannelPointDataValidationException("请上传文件！");
        }
        if (!multipartFile.getOriginalFilename().endsWith(".csv")) {
            throw new ChannelPointDataValidationException("上传失败!文件必须是csv格式");
        }

        String originalFileName = multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().indexOf(".csv"));

        if (channelPointMapper.findBySerialNo(originalFileName) != null) {
            throw new ChannelPointDataValidationException(String.format("%s文件已经成功导入,请知晓!", originalFileName));
        }


    }

    public BaseDataDto importChannelPoint(InputStream inputStream) throws Exception {
        List<ChannelPointDetailDto> details = Lists.newArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line;
            while (null != (line = bufferedReader.readLine())) {
                String[] data = line.split(",");
                details.add(new ChannelPointDetailDto(null, data[0], data[1], data[2], Long.parseLong(data[3]), "成功".equals(data[4]) ? true : false));
            }
            if (details.size() > 1000) {
                return new BaseDataDto(false, "每次数据应该小于1000条");
            }
            details.stream().filter(dto -> checkChannelPointDetailDto(dto));



        } catch (ArrayIndexOutOfBoundsException e) {
            return new BaseDataDto(false, "上传失败！文件中部分数据格式不规范");
        } catch (IOException e) {
            return new BaseDataDto(false, "上传失败!文件内容读取错误");
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }

    private boolean checkChannelPointDetailDto(ChannelPointDetailDto channelPointDetailDto) {
        if (channelPointDetailDto.isSuccess()) {
            channelPointDetailDto.setSuccess(false);
            channelPointDetailDto.setRemark("已经导入成功!");
            return false;
        }
        UserModel userModel = userMapper.findByMobile(channelPointDetailDto.getMobile());
        if (userModel == null || !userModel.getUserName().equals(channelPointDetailDto.getUserName())) {
            channelPointDetailDto.setRemark("手机号与用户姓名不匹配");
            return false;
        }
        AccountModel accountModel = accountMapper.findByMobile(channelPointDetailDto.getMobile());
        if (accountModel == null) {
            channelPointDetailDto.setRemark("用户没有进行实名认证!");
            return false;
        }
//        List<ChannelPointDetailModel> models = channelPointDetailMapper.findSuccessByMobile(channelPointDetailDto.getMobile());
//        if (!models.stream().allMatch(model -> model.getChannel().equals(channelPointDetailDto.getChannel()))) {
//            channelPointDetailDto.setRemark("导入积分渠道与已存在的积分渠道不一致!");
//            return false;
//        }
        channelPointDetailDto.setLoginName(userModel.getLoginName());
        return true;

    }


}


