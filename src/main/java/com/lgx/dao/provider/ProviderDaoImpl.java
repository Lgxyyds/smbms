package com.lgx.dao.provider;

import com.lgx.dao.BaseDao;
import com.lgx.pojo.Provider;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProviderDaoImpl implements ProviderDao{
    //获取供应商列表
    public List<Provider> getProviderList(Connection connection, String proName, String proCode) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Provider> proList = new ArrayList<Provider>();

        if (connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select * from smbms_provider where 1=1");
            ArrayList<Object> paramsList = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(proName)){
                sql.append(" and proName like ?");
                paramsList.add("%"+proName+"%");
            }
            if (!StringUtils.isNullOrEmpty(proCode)){
                sql.append(" and proCode like ?");
                paramsList.add("%"+proCode+"%");
            }
            Object[] params = paramsList.toArray();

            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params);

            while (resultSet.next()){
                Provider provider = new Provider();
                provider.setId(resultSet.getInt("id"));
                provider.setProCode(resultSet.getString("proCode"));
                provider.setProName(resultSet.getString("proName"));
                provider.setProDesc(resultSet.getString("proDesc"));
                provider.setProContact(resultSet.getString("proContact"));
                provider.setProPhone(resultSet.getString("proPhone"));
                provider.setProAddress(resultSet.getString("proAddress"));
                provider.setProFax(resultSet.getString("proFax"));
                provider.setCreationDate(resultSet.getTimestamp("creationDate"));
                proList.add(provider);
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return proList;
    }


    //添加供应商
    public int addProvider(Connection connection, Provider provider) throws Exception {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
        if (connection!=null){
            String sql = "insert into smbms_provider (proCode,proName,proDesc," +
                    "proContact,proPhone,proAddress,proFax,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?)";
            Object[] params = {provider.getProCode(), provider.getProName(), provider.getProDesc(),
                    provider.getProContact(), provider.getProPhone(), provider.getProAddress(),
                    provider.getProFax(), provider.getCreatedBy(), provider.getCreationDate()};
            updateRows = BaseDao.execute(connection, preparedStatement, sql, params);
        }
        BaseDao.closeResource(null,preparedStatement,null);
        return updateRows;
    }

    //删除供应商
    public int deleteProviderById(Connection con, String delId) throws Exception {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if (con != null) {
            String sql = "delete from smbms_provider where id = ?";
            Object[] params = {delId};
            updateRows = BaseDao.execute(con, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return updateRows;
    }

    //根据id获取供应商
    public Provider getProviderById(Connection con, String id) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Provider provider = null;
        if (con != null) {
            String sql = "select * from smbms_provider where id = ?";
            Object[] params = {id};
            rs = BaseDao.execute(con, pstm, rs, sql, params);
            if (rs.next()) {
                provider = new Provider();
                provider.setId(rs.getInt("id"));
                provider.setProCode(rs.getString("proCode"));
                provider.setProName(rs.getString("proName"));
                provider.setProDesc(rs.getString("proDesc"));
                provider.setProContact(rs.getString("proContact"));
                provider.setProPhone(rs.getString("proPhone"));
                provider.setProAddress(rs.getString("proAddress"));
                provider.setProFax(rs.getString("proFax"));
                provider.setCreatedBy(rs.getInt("createdBy"));
                provider.setCreationDate(rs.getTimestamp("creationDate"));
                provider.setModifyBy(rs.getInt("modifyBy"));
                provider.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return provider;
    }

    //修改供应商
    public int modifyProvider(Connection connection, Provider provider) {
        PreparedStatement preparedStatement = null;
        int modifyRows = 0;

        if (connection!=null){
            String sql = "update smbms_provider set proName=?,proDesc=?,proContact=?," +
                    "proPhone=?,proAddress=?,proFax=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {provider.getProName(), provider.getProDesc(), provider.getProContact(), provider.getProPhone(), provider.getProAddress(), provider.getProFax(), provider.getModifyBy(), provider.getModifyDate(), provider.getId()};
            modifyRows = BaseDao.execute(connection, preparedStatement, sql, params);
            BaseDao.closeResource(null, preparedStatement, null);
        }
        return modifyRows;
    }
}
