package com.lgx.service.user;

import com.lgx.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserService {
    //用户登录
    public User login(String userCode, String password);

    //根据用户id修改密码
    public Boolean updatePwd(int id,String password);

    ////获取根据用户名或角色获取用户总数
    public int getUserCount(String userName,int userRole);

    //获取用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    //添加用户
    public Boolean addUser(User user);

    //删除用户
    public Boolean deleteUserById(Integer id);

    //查看用户
    public User getUserById(String id);

    //修改用户
    public Boolean modify(User user);
}
