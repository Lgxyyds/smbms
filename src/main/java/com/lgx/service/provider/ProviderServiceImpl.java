package com.lgx.service.provider;

import com.lgx.dao.BaseDao;
import com.lgx.dao.bill.BillDao;
import com.lgx.dao.bill.BillDaoImpl;
import com.lgx.dao.provider.ProviderDao;
import com.lgx.dao.provider.ProviderDaoImpl;
import com.lgx.pojo.Provider;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProviderServiceImpl implements ProviderService{
    private ProviderDao providerDao;
    private BillDao billDao;
    public ProviderServiceImpl() {
        providerDao = new ProviderDaoImpl();
        billDao = new BillDaoImpl();
    }

    //获取供应商列表
    public List<Provider> getProviderList(String proName, String proCode) {
        Connection connection =null;
        List<Provider> providerList = null;
        try {
            connection = BaseDao.getConnection();
            providerList = providerDao.getProviderList(connection, proName, proCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return providerList;
    }

    //添加供应商
    public Boolean addProvider(Provider provider) {
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            int i = providerDao.addProvider(connection, provider);
            if (i>0){
                flag = true;
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    //通过proId删除Provider 返回订单列表
    public int deleteProviderById(String delId) {
        Connection con = null;
        int billCount = -1;
        try {
            con = BaseDao.getConnection();
            con.setAutoCommit(false);
            billCount = billDao.getBillCountByProviderId(con, delId);
            if (billCount == 0) {
                providerDao.deleteProviderById(con, delId);
            }
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            billCount = -1;
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(con, null, null);
        }
        return billCount;
    }

    public Provider getProviderById(String id) {
        Connection con = null;
        Provider provider = null;
        try {
            con = BaseDao.getConnection();
            provider = providerDao.getProviderById(con, id);
        } catch (Exception e) {
            e.printStackTrace();
            provider = null;
        } finally {
            BaseDao.closeResource(con, null, null);
        }
        return provider;
    }

    //修改供应商
    public boolean modifyProvider(Provider provider) {
        Connection connection = null;
        boolean flag = false;

        connection = BaseDao.getConnection();
        int i = providerDao.modifyProvider(connection,provider);
        if (i>0){
            flag = true;
        }
        BaseDao.closeResource(connection,null,null);
        return flag;
    }
}
