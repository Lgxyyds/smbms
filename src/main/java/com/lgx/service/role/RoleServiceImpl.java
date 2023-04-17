package com.lgx.service.role;

import com.lgx.dao.BaseDao;
import com.lgx.dao.role.RoleDao;
import com.lgx.dao.role.RoleDaoImpl;
import com.lgx.pojo.Role;

import java.sql.Connection;
import java.util.List;

public class RoleServiceImpl implements RoleService{

    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        Connection connection = null;
        connection = BaseDao.getConnection();

        List<Role> roleList = null;
        try {
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        BaseDao.closeResource(connection,null,null);

        return roleList;
    }
}
