package com.tuotiansudai.message.service;


import com.tuotiansudai.message.repository.model.UserMessageModel;

public interface UserMessageService {
    UserMessageModel findById(long id);
}
