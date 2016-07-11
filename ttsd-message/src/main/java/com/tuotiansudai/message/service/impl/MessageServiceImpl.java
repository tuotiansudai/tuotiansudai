package com.tuotiansudai.message.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.message.dto.MessageDto;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageStatus;
import com.tuotiansudai.message.repository.model.MessageType;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.message.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String redisMessageReceivers = "message:manual-message:receivers";

    private final static int EXPIRED_PERIOD = 30;

    @Override
    public long findMessageCount(String title, MessageStatus messageStatus, String createdBy, MessageType messageType) {
        return messageMapper.findMessageCount(title, messageStatus, createdBy, messageType);
    }

    @Override
    public List<MessageModel> findMessageList(String title, MessageStatus messageStatus, String createdBy, MessageType messageType, int index, int pageSize) {
        return messageMapper.findMessageList(title, messageStatus, createdBy, messageType, (index - 1) * pageSize, pageSize);
    }

    @Override
    public void createAndEditManualMessage(MessageDto messageDto, long importUsersId) {
        long messageId = messageDto.getId();
        if (isMessageExist(messageId)) {
            editManualMessage(messageDto, importUsersId);
        } else {
            createManualMessage(messageDto, importUsersId);
        }
    }

    @Override
    public long createImportReceivers(long oldImportUsersId, InputStream inputStream) throws IOException {
        if (redisWrapperClient.hexists(redisMessageReceivers, String.valueOf(oldImportUsersId))) {
            redisWrapperClient.hdel(redisMessageReceivers, String.valueOf(oldImportUsersId));
        }

        long importUsersId = new Date().getTime();

        List<String> importUsers = Lists.newArrayList();

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        for (int rowIndex = hssfSheet.getFirstRowNum(); rowIndex <= hssfSheet.getLastRowNum(); ++rowIndex) {
            HSSFRow hssfRow = hssfSheet.getRow(rowIndex);
            for (int cellIndex = hssfRow.getFirstCellNum(); cellIndex < hssfRow.getLastCellNum(); ++cellIndex) {
                HSSFCell hssfCell = hssfRow.getCell(cellIndex);
                String loginName = getStringFromCell(hssfCell);
                if (!StringUtils.isEmpty(loginName)) {
                    importUsers.add(loginName);
                }
            }
        }
        redisWrapperClient.hsetSeri(redisMessageReceivers, String.valueOf(importUsersId), importUsers);
        return importUsersId;
    }

    @Override
    public MessageDto getMessageByMessageId(long messageId) {
        return new MessageDto(messageMapper.findById(messageId));
    }

    @Override
    public BaseDto<BaseDataDto> rejectManualMessage(long messageId, String checkerName) {
        if (!isMessageExist(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        MessageModel messageModel = messageMapper.findById(messageId);
        if (MessageStatus.TO_APPROVE == messageModel.getStatus()) {
            messageModel.setStatus(MessageStatus.REJECTION);
            messageModel.setUpdatedTime(new Date());
            messageModel.setUpdatedBy(checkerName);
            messageMapper.update(messageModel);
            return new BaseDto<>(new BaseDataDto(true, null));
        }
        return new BaseDto<>(new BaseDataDto(false, "message state is not TO_APPROVE"));
    }

    @Override
    public BaseDto<BaseDataDto> approveManualMessage(long messageId, String checkerName) {
        if (!isMessageExist(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        MessageModel messageModel = messageMapper.findById(messageId);
        if (MessageStatus.TO_APPROVE == messageModel.getStatus()) {
            messageModel.setActivatedBy(checkerName);
            messageModel.setActivatedTime(new Date());
            messageModel.setStatus(MessageStatus.APPROVED);
            messageModel.setUpdatedTime(new Date());
            messageModel.setUpdatedBy(checkerName);
            messageMapper.update(messageModel);
            return new BaseDto<>(new BaseDataDto(true, null));
        }
        return new BaseDto<>(new BaseDataDto(false, "message state is not TO_APPROVE"));
    }

    @Override
    public BaseDto<BaseDataDto> deleteManualMessage(long messageId, String updatedBy) {
        if (!isMessageExist(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        messageMapper.deleteById(messageId, updatedBy, new Date());
        redisWrapperClient.hdelSeri(redisMessageReceivers, String.valueOf(messageId));
        return new BaseDto<>(new BaseDataDto(true, null));
    }

    @Override
    public boolean isMessageExist(long messageId) {
        return null != messageMapper.findById(messageId);
    }

    private void createManualMessage(MessageDto messageDto, long importUsersId) {
        MessageModel messageModel = new MessageModel(messageDto);

        messageModel.setType(MessageType.MANUAL);
        messageModel.setStatus(MessageStatus.TO_APPROVE);
        messageModel.setReadCount(0);
        messageModel.setExpiredTime(new DateTime().plusDays(EXPIRED_PERIOD).withTimeAtStartOfDay().toDate());
        messageModel.setCreatedTime(new Date());
        messageModel.setUpdatedTime(messageModel.getCreatedTime());

        messageMapper.create(messageModel);

        if (messageDto.getUserGroups().contains(MessageUserGroup.IMPORT_USER)) {
            String messageId = String.valueOf(messageModel.getId());
            String importUsers = redisWrapperClient.hget(redisMessageReceivers, String.valueOf(importUsersId));
            redisWrapperClient.hdelSeri(redisMessageReceivers, String.valueOf(importUsersId));
            redisWrapperClient.hsetSeri(redisMessageReceivers, messageId, importUsers);
        }
    }

    private void editManualMessage(MessageDto messageDto, long importUsersId) {
        String importUsers = redisWrapperClient.hget(redisMessageReceivers, String.valueOf(importUsersId));
        redisWrapperClient.hdelSeri(redisMessageReceivers, String.valueOf(importUsersId));

        MessageModel originMessageModel = messageMapper.findById(messageDto.getId());
        MessageModel messageModel = new MessageModel(messageDto);

        messageModel.setType(originMessageModel.getType());
        messageModel.setEventType(originMessageModel.getEventType());
        messageModel.setStatus(originMessageModel.getStatus());
        messageModel.setReadCount(originMessageModel.getReadCount());
        messageModel.setActivatedBy(originMessageModel.getActivatedBy());
        messageModel.setActivatedTime(originMessageModel.getActivatedTime());
        messageModel.setExpiredTime(originMessageModel.getExpiredTime());
        messageModel.setUpdatedTime(new Date());
        messageModel.setCreatedBy(originMessageModel.getCreatedBy());
        messageModel.setCreatedTime(originMessageModel.getCreatedTime());

        messageMapper.update(messageModel);

        if (messageDto.getUserGroups().contains(MessageUserGroup.IMPORT_USER)) {
            String messageId = String.valueOf(messageDto.getId());
            redisWrapperClient.hsetSeri(redisMessageReceivers, messageId, importUsers);
        }
    }

    private String getStringFromCell(HSSFCell hssfCell) {
        switch (hssfCell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                hssfCell.setCellType(Cell.CELL_TYPE_STRING);
                return hssfCell.getStringCellValue();
            case Cell.CELL_TYPE_STRING:
                return hssfCell.getStringCellValue();
            default:
                return "";
        }
    }
}
