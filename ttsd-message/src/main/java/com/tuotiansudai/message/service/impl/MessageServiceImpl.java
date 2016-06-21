package com.tuotiansudai.message.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;


    final private static String redisMessageReceivers = "message:manual-message:receivers";
    final private int expiredPeriod = 30;


    @Override
    public long findMessageCount(String title, MessageStatus messageStatus, String createdBy, MessageType messageType) {
        return messageMapper.findMessageCount(title, messageStatus, createdBy, messageType);
    }

    public List<MessageModel> findMessageList(String title, MessageStatus messageStatus, String createdBy, MessageType messageType, int index, int pageSize) {
        return messageMapper.findMessageList(title, messageStatus, createdBy, messageType, (index - 1) * pageSize, pageSize);
    }

    @Override
    public void createAndEditManualMessage(MessageDto messageDto, long importUsersId) {
        long messageId = messageDto.getId();
        if (messageExisted(messageId)) {
            editManualMessage(messageDto, importUsersId);
        } else {
            createManualMessage(messageDto, importUsersId);
        }
    }

    private void createManualMessage(MessageDto messageDto, long importUsersId) {
        messageDto.setType(MessageType.MANUAL);
        messageDto.setReadCount(0);
        messageDto.setStatus(MessageStatus.TO_APPROVE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, expiredPeriod);
        messageDto.setExpiredTime(calendar.getTime());

        MessageModel messageModel = new MessageModel(messageDto);
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

        MessageDto originMessageDto = getMessageByMessageId(messageDto.getId());
        originMessageDto.setTitle(messageDto.getTitle());
        originMessageDto.setTemplate(messageDto.getTemplate());
        originMessageDto.setUserGroups(messageDto.getUserGroups());
        originMessageDto.setChannels(messageDto.getChannels());

        messageMapper.update(new MessageModel(originMessageDto));

        if (messageDto.getUserGroups().contains(MessageUserGroup.IMPORT_USER)) {
            String messageId = String.valueOf(originMessageDto.getId());
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

    @Override
    public long createImportReceivers(long oldImportUsersId, InputStream inputStream) throws IOException {
        if (redisWrapperClient.hexists(redisMessageReceivers, String.valueOf(oldImportUsersId))) {
            redisWrapperClient.hdel(redisMessageReceivers, String.valueOf(oldImportUsersId));
        }

        long importUsersId = new Date().getTime();

        List<String> importUsers = new ArrayList<>();

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

    public BaseDto<BaseDataDto> rejectManualMessage(long messageId) {
        if (!messageExisted(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        MessageDto messageDto = getMessageByMessageId(messageId);
        if (MessageStatus.TO_APPROVE.equals(messageDto.getStatus())) {
            messageDto.setStatus(MessageStatus.REJECTION);
            messageMapper.update(new MessageModel(messageDto));
            return new BaseDto<>(new BaseDataDto(true));
        }
        return new BaseDto<>(new BaseDataDto(false, "message state is not TO_APPROVE"));
    }

    public BaseDto<BaseDataDto> approveManualMessage(long messageId) {
        if (!messageExisted(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        MessageDto messageDto = getMessageByMessageId(messageId);
        if (MessageStatus.TO_APPROVE.equals(messageDto.getStatus())) {
            messageDto.setStatus(MessageStatus.APPROVED);
            messageMapper.update(new MessageModel(messageDto));
            return new BaseDto<>(new BaseDataDto(true));
        }
        return new BaseDto<>(new BaseDataDto(false, "message state is not TO_APPROVE"));
    }

    public BaseDto<BaseDataDto> deleteManualMessage(long messageId) {
        if (!messageExisted(messageId)) {
            return new BaseDto<>(new BaseDataDto(false, "messageId not existed!"));
        }
        messageMapper.deleteById(messageId);
        redisWrapperClient.hdelSeri(redisMessageReceivers, String.valueOf(messageId));
        return new BaseDto<>(new BaseDataDto(true));
    }

    @Override
    public boolean messageExisted(long messageId) {
        return null != messageMapper.findById(messageId);
    }
}
