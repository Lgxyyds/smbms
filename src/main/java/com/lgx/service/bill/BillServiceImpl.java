package com.lgx.service.bill;

import com.lgx.dao.BaseDao;
import com.lgx.dao.bill.BillDao;
import com.lgx.dao.bill.BillDaoImpl;
import com.lgx.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BillServiceImpl implements BillService{

    private BillDao billDao;

    public BillServiceImpl() {
        billDao = new BillDaoImpl();
    }

    //获取订单列表
    public List<Bill> getBillList(Bill bill) {
        Connection con = null;
        List<Bill> billList = null;
        System.out.println("query productName ---- > " + bill.getProductName());
        System.out.println("query providerId ---- > " + bill.getProviderId());
        System.out.println("query isPayment ---- > " + bill.getIsPayment());
        try {
            con = BaseDao.getConnection();
            billList = billDao.getBillList(con, bill);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(con, null, null);
        }
        return billList;
    }

    public Boolean addBill(Bill bill) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            if (billDao.addBill(connection,bill)>0){
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

    //根据id删除订单
    public Boolean delBill(String id) {
        Connection connection= null;
        boolean flag = false;

        try {
            connection = BaseDao.getConnection();
            if (billDao.delBillById(connection,id)>0){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    //根据id查看订单
    public Bill getBillById(String id) {
        Connection connection = null;
        Bill bill = null;
        try {
            connection = BaseDao.getConnection();
            bill = billDao.getBillById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
            bill = null;
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return bill;

    }

    //修改订单
    public boolean modeifyBill(Bill bill) {
        Connection connection = null;
        boolean flag = false;

        try {
            connection =BaseDao.getConnection();
            int i = billDao.modifyBill(connection, bill);
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
