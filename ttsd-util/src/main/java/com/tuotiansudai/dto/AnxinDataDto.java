package com.tuotiansudai.dto;

public class AnxinDataDto extends BaseDataDto {

    private String code;

    private byte[] pdf;

    public AnxinDataDto() {
    }

    public AnxinDataDto(boolean status, String message) {
        super(status, message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }
}
