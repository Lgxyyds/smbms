package com.lgx.dao.role;

import com.lgx.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleDao {
    //获取角色列表
    public List<Role> getRoleList(Connection connection) throws Exception;
}
