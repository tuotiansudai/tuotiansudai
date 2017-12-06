package com.tuotiansudai.dto.request;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserRestQueryDto implements Serializable {
    private Integer page;
    private Integer pageSize;
    private String[] sort;
    private String[] fields;

    private String email;
    private Role role;
    private UserStatus status;
    private String[] channels;
    private String referrer;
    private String identityNumber;
    private String mobileLike;
    private Date registerTimeGte;
    private Date registerTimeLte;
    private Boolean hasReferrer;

    public UserRestQueryDto(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public UserRestQueryDto() {
        this(1, 10);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getSort() {
        return sort;
    }

    public void setSort(String... sort) {
        this.sort = sort;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String... fields) {
        this.fields = fields;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String... channels) {
        this.channels = channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels.toArray(new String[]{});
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getMobileLike() {
        return mobileLike;
    }

    public void setMobileLike(String mobileLike) {
        this.mobileLike = mobileLike;
    }

    public Date getRegisterTimeGte() {
        return registerTimeGte;
    }

    public void setRegisterTimeGte(Date registerTimeGte) {
        this.registerTimeGte = registerTimeGte;
    }

    public Date getRegisterTimeLte() {
        return registerTimeLte;
    }

    public void setRegisterTimeLte(Date registerTimeLte) {
        this.registerTimeLte = registerTimeLte;
    }

    public Boolean getHasReferrer() {
        return hasReferrer;
    }

    public void setHasReferrer(Boolean hasReferrer) {
        this.hasReferrer = hasReferrer;
    }
}

