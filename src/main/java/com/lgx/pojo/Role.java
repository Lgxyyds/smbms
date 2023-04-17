package com.lgx.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Role {
    private Integer id;//id
    private String roleCode;//角色编码
    private String roleName;//角色名字
    private Integer createdBy;//创建者
    private Date creationDate;//创建日期
    private Integer modifyBY;//更新者
    private Date modifyDate;//更新日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getModifyBY() {
        return modifyBY;
    }

    public void setModifyBY(Integer modifyBY) {
        this.modifyBY = modifyBY;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
