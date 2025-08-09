package com.example.employee.task.tracker.model;

import com.example.employee.task.tracker.config.hibernate.FilterConstants;
import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
@MappedSuperclass
@FilterDef(name = FilterConstants.STATUS_FILTER, parameters = @ParamDef(name = FilterConstants.STATUS_FILTER_PARAM, type = String.class))
@Filter(name = FilterConstants.STATUS_FILTER, condition = "status = :status_prm")
public class BaseEntity <ID>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @CreatedDate
    private LocalDateTime CreateDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @CreatedDate
    private String createInfo;

    @LastModifiedBy
    private String updateInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    public void setId(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        CreateDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(String createInfo) {
        this.createInfo = createInfo;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        ACTIVE, PASSIVE
    }
}
