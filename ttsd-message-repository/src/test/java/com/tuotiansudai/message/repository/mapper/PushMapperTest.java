package com.tuotiansudai.message.repository.mapper;

import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.repository.model.PushModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class PushMapperTest extends BaseMapperTest {

    @Autowired
    private PushMapper pushMapper;

    @Test
    public void shouldCreatePush() {
        PushModel pushModel = new PushModel("user", PushType.BIRTHDAY, PushSource.ALL, "content", AppUrl.MESSAGE_CENTER_LIST);
        pushMapper.create(pushModel);

        assertNotNull(pushMapper.findById(pushModel.getId()));
    }
}
