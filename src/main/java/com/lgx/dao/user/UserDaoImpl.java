package com.lgx.dao.user;

import com.lgx.dao.BaseDao;
import com.lgx.pojo.User;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao{
    //获取登录用户
    public User getLoginUser(Connection connection, String userCode) throws Exception {
        //准备三个对象
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;

        //判断是否连接成功
        if (connection != null){
            String sql = "select * from smbms_user where userCode = ?";
            Object[] params = {userCode};

            rs = BaseDao.execute(connection,pstm,rs,sql,params);

            if (rs.next()){
                user =new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return user;

    }

    //修改用户登录密码
    public int updatePwd(Connection connection, int id, String password) throws Exception {

        PreparedStatement preparedStatement = null;
        int execute = 0;
        if (connection!=null){
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object[] params = {password,id};
            execute = BaseDao.execute(connection,preparedStatement,sql,params);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return execute;
    }

    //获取根据用户名或角色获取用户总数
    public int getUserCount(Connection connection, String userName, int userRole) throws Exception {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append( "select count(1) as count from smbms_user u ,smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if (userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            Object[] params = list.toArray();

            System.out.println("sql-->" + sql.toString());
            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params);
            if (resultSet.next()){
                count = resultSet.getInt("count");
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return count;
    }

    //获取用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<User>();
        if (connection!= null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if (userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();

            System.out.println("sql-->"+ sql.toString());

            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params);
            while(resultSet.next()){
                User _user = new User();
                _user.setId(resultSet.getInt("id"));
                _user.setUserCode(resultSet.getString("userCode"));
                _user.setUserName(resultSet.getString("userName"));
                _user.setGender(resultSet.getInt("gender"));
                _user.setBirthday(resultSet.getDate("birthday"));
                _user.setPhone(resultSet.getString("phone"));
                _user.setUserRole(resultSet.getInt("userRole"));
                _user.setUserRoleName(resultSet.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return userList;
    }

    public int addUser(Connection connection,User user) throws Exception {
        PreparedStatement preparedStatement = null;
        int count = 0;

        if (connection != null){
            String sql = "insert into smbms_user (userCode, userName, userPassword, userRole, " +
                    "gender, birthday, phone, address, creationDate, createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),user.getUserRole(),user.getGender(),
                    user.getBirthday(),user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};


            count = BaseDao.execute(connection, preparedStatement, sql, params);
            System.out.println("sql-->"+sql);
            BaseDao.closeResource(null,preparedStatement,null);
        }
        return count;
    }

    public int deleteUserById(Connection connection, Integer id) throws Exception {
        PreparedStatement preparedStatement = null;
        int deleteRows = 0;
        if (connection!=null){
            String sql = "delete from smbms_user where id = ?";
            Object[] params = {id};

            deleteRows = BaseDao.execute(connection, preparedStatement, sql, params);

            BaseDao.closeResource(null,preparedStatement,null);
        }
        return deleteRows;
    }

    public User getUserById(Connection connection, String id) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        User user = new User();
        if (connection!=null){
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] params = {id};

            rs = BaseDao.execute(connection, preparedStatement, rs, sql, params);
            if (rs.next()){
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            BaseDao.closeResource(null,preparedStatement,rs);
        }
        return user;
    }


    public int modify(Connection con, User user) throws Exception {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if (con!= null){
            String sql = "update smbms_user set userName=?," +
                    "gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {user.getUserName(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getUserRole(), user.getModifyBy(),
                    user.getModifyDate(), user.getId()};
            updateRows = BaseDao.execute(con, preparedStatement, sql, params);
            BaseDao.closeResource(null, preparedStatement, null);
        }
        return updateRows;
    }
}
