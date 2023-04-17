package com.lgx.dao.bill;

import com.lgx.pojo.Bill;

import java.sql.Connection;
import java.util.List;

public interface BillDao {
    //获取订单列表
    public List<Bill> getBillList(Connection connection,Bill bill) throws Exception;

    //添加订单
    public int addBill(Connection connection,Bill bill) throws Exception;

    //根据id删除订单
    public int delBillById(Connection connection,String id) throws Exception;

    //根据Id查看订单
    public Bill getBillById(Connection connection,String id) throws Exception;

    //修改订单
    public int modifyBill(Connection connection,Bill bill) throws Exception;

    public int getBillCountByProviderId(Connection con, String providerId) throws Exception;
}
