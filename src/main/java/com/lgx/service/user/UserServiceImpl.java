package com.lgx.service.user;

import com.lgx.dao.BaseDao;
import com.lgx.dao.user.UserDao;
import com.lgx.dao.user.UserDaoImpl;
import com.lgx.pojo.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService{

    //业务层都会调用Dao层，所以我们需要引入dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    //用户登录
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;

        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
            if (!user.getUserPassword().equals(password)){
                user = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            BaseDao.closeResource(connection,null,null);
        }

        return user;

    }

    //修改密码
    public Boolean updatePwd(int id, String password) {

        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePwd(connection,id,password)>0){
                flag = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    //获取用户数量
    public int getUserCount(String userName, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, userName, userRole);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            BaseDao.closeResource(connection,null,null);
        }

        return count;
    }

    //获取用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection connection =null;
        List<User> userList = null;

        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, userName, userRole, currentPageNo, pageSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            BaseDao.closeResource(connection,null,null);
        }

        return userList;
    }

    public Boolean addUser(User user) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int updateRows = userDao.addUser(connection, user);
            connection.commit();
            if (updateRows>0){
                flag = true;
                System.out.println("add succeed");
            }else {
                System.out.println("add failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("rollback");
                connection.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection,null,null);
        }

        return flag;

    }

    //删除用户
    public Boolean deleteUserById(Integer id) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            int i = userDao.deleteUserById(connection, id);
            if (i>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    public User getUserById(String id) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getUserById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    public Boolean modify(User user) {
        Connection connection =null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            int i = userDao.modify(connection, user);
            if (i>0){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }
}
