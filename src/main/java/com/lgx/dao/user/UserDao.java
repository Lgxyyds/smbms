package com.lgx.dao.user;

import com.lgx.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {

    //获取登录用户
    public User getLoginUser(Connection connection,String userCode) throws Exception;

    //修改用户登录密码
    public int updatePwd(Connection connection,int id,String password) throws Exception;

    //获取根据用户名或角色获取用户总数
    public int getUserCount(Connection connection,String userName,int userRole) throws Exception;

    //获取用户列表
    public List<User> getUserList(Connection connection,String userName,int userRole,int currentPageNo,int pageSize) throws Exception;

    //添加用户
    public int addUser(Connection connection,User user) throws Exception;

    //删除用户
    public int deleteUserById(Connection connection,Integer id) throws Exception;

    //根据id查看用户
    public User getUserById (Connection connection,String id) throws Exception;

    //修改用户
    public int modify(Connection con, User user) throws Exception;
}
