package com.esoft.archer.user.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

/**
 * UserInfoLog entity. 管理员操作用户信息的日志
 */
@Entity
@Table(name = "user_info_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class UserInfoLog implements java.io.Serializable {
    // Fields

    private String id;
    private Date operateTime;
    private String ip;
    //private String operateType;
    private String objId;
    private String description;
    private String userId;
    private Boolean success;

    // Constructors

    /**
     * default constructor
     */
    public UserInfoLog() {
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "user_id", nullable = false, length = 32)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "operate_time", nullable = false, length = 19)
    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Column(name = "ip", length = 70)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    //@Column(name = "operate_type", length = 32)
    //public String getOperateType() {
    //    return operateType;
    //}

    //public void setOperateType(String operateType) {
    //    this.operateType = operateType;
    //}

    @Column(name = "obj_id", length = 100)
    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    @Column(name = "description", length = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "is_success",columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}