package com.lgx.service.bill;

import com.lgx.pojo.Bill;

import java.util.List;

public interface BillService {
    //获取订单列表
    public List<Bill> getBillList(Bill bill);

    //添加订单
    public Boolean addBill(Bill bill);

    //根据id删除订单
    public Boolean delBill(String id);

    //根据id查看订单
    public Bill getBillById(String id);

    //修改订单
    public boolean modeifyBill(Bill bill);
}
