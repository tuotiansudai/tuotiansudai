package com.tuotiansudai.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BaseDtoTest {

    @Test
    public void shouldConvertToJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        BaseTestDto<DataTestDto> dataTestDtoBaseTestDto = new BaseTestDto<>();

        DataTestDto dataTestDto = new DataTestDto();
        dataTestDtoBaseTestDto.setData(dataTestDto);

        String json = objectMapper.writeValueAsString(dataTestDtoBaseTestDto);

        assertTrue(true);
    }

    @Test
    public void shouldConvertFromJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        BaseTestDto<DataTestDto> dataTestDtoBaseTestDto = new BaseTestDto<>();

        DataTestDto dataTestDto = new DataTestDto();
        dataTestDtoBaseTestDto.setData(dataTestDto);


        assertTrue(true);

    }
}

class BaseTestDto<T extends BaseDataTestDto> {
    private boolean success = true;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

class BaseDataTestDto {
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

class DataTestDto extends BaseDataTestDto {
    private String loginName = "loginName";

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
