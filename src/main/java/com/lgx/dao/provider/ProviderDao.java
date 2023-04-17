package com.lgx.dao.provider;

import com.lgx.pojo.Provider;

import java.sql.Connection;
import java.util.List;

public interface ProviderDao {

    //获取供应商列表
    public List<Provider> getProviderList(Connection connection,String proName,String proCode) throws Exception;

    //添加供应商
    public int addProvider(Connection connection,Provider provider) throws Exception;

    //通过proId删除Provider
    int deleteProviderById(Connection con, String delId) throws Exception;

    //通过proId获取Provider
    public Provider getProviderById(Connection con, String id) throws Exception;

    //修改供应商
    public int modifyProvider(Connection connection, Provider provider);
}
