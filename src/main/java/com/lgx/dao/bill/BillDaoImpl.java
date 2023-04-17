package com.lgx.dao.bill;

import com.lgx.dao.BaseDao;
import com.lgx.pojo.Bill;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImpl implements BillDao{
    //获取订单列表
    public List<Bill> getBillList(Connection con, Bill bill) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Bill> billList = new ArrayList<Bill>();
        if (con != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select b.*,p.proName as providerName from smbms_bill b, smbms_provider p where b.providerId = p.id");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(bill.getProductName())) {
                sql.append(" and productName like ?");
                list.add("%" + bill.getProductName() + "%");
            }
            if (bill.getProviderId() > 0) {
                sql.append(" and providerId = ?");
                list.add(bill.getProviderId());
            }
            if (bill.getIsPayment() > 0) {
                sql.append(" and isPayment = ?");
                list.add(bill.getIsPayment());
            }
            Object[] params = list.toArray();
            rs = BaseDao.execute(con, pstm, rs, sql.toString(), params);
            while (rs.next()) {
                Bill _bill = new Bill();
                _bill.setId(rs.getInt("id"));
                _bill.setBillCode(rs.getString("billCode"));
                _bill.setProductName(rs.getString("productName"));
                _bill.setProductDesc(rs.getString("productDesc"));
                _bill.setProductUnit(rs.getString("productUnit"));
                _bill.setProductCount(rs.getBigDecimal("productCount"));
                _bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                _bill.setIsPayment(rs.getInt("isPayment"));
                _bill.setProviderId(rs.getInt("providerId"));
                _bill.setProviderName(rs.getString("providerName"));
                _bill.setCreationDate(rs.getTimestamp("creationDate"));
                _bill.setCreatedBy(rs.getInt("createdBy"));
                billList.add(_bill);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return billList;
    }

    //添加订单
    public int addBill(Connection connection, Bill bill) throws Exception {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if (connection != null) {
            String sql = "insert into smbms_bill (billCode,productName,productDesc," +
                    "productUnit,productCount,totalPrice,isPayment,providerId,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {bill.getBillCode(), bill.getProductName(), bill.getProductDesc(),
                    bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                    bill.getProviderId(), bill.getCreatedBy(), bill.getCreationDate()};
            updateRows = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return updateRows;
    }

    //根据id删除订单
    public int delBillById(Connection connection, String id) throws Exception {
        PreparedStatement preparedStatement = null;
        int delRows = 0;

        if (connection!=null){
            String sql = "delete from smbms_bill where id = ?";
            Object[] params = {id};
            delRows = BaseDao.execute(connection, preparedStatement, sql, params);
        }
        BaseDao.closeResource(null,preparedStatement,null);
        return delRows;
    }

    //根据Id查看订单
    public Bill getBillById(Connection connection, String id) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Bill bill = null;
        if (connection!=null){
            String sql = "select b.*,p.proName as providerName from smbms_bill b, smbms_provider p " +
                    "where b.providerId = p.id and b.id=?";
            Object[] params = {id};

            resultSet = BaseDao.execute(connection, preparedStatement, resultSet, sql, params);
            if (resultSet.next()){
                bill = new Bill();
                bill.setId(resultSet.getInt("id"));
                bill.setBillCode(resultSet.getString("billCode"));
                bill.setProductName(resultSet.getString("productName"));
                bill.setProductDesc(resultSet.getString("productDesc"));
                bill.setProductUnit(resultSet.getString("productUnit"));
                bill.setProductCount(resultSet.getBigDecimal("productCount"));
                bill.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                bill.setIsPayment(resultSet.getInt("isPayment"));
                bill.setProviderId(resultSet.getInt("providerId"));
                bill.setProviderName(resultSet.getString("providerName"));
                bill.setModifyBy(resultSet.getInt("modifyBy"));
                bill.setModifyDate(resultSet.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null,preparedStatement,resultSet);
        }
        return bill;
    }

    //修改订单
    public int modifyBill(Connection connection, Bill bill) throws Exception {
        PreparedStatement preparedStatement = null;
        int updateRows = 0;
         if (connection!=null){
             String sql = "update smbms_bill set productName=?," +
                     "productDesc=?,productUnit=?,productCount=?,totalPrice=?," +
                     "isPayment=?,providerId=?,modifyBy=?,modifyDate=? where id = ? ";
             Object[] params = {bill.getProductName(), bill.getProductDesc(),
                     bill.getProductUnit(), bill.getProductCount(), bill.getTotalPrice(), bill.getIsPayment(),
                     bill.getProviderId(), bill.getModifyBy(), bill.getModifyDate(), bill.getId()};
             updateRows = BaseDao.execute(connection, preparedStatement, sql, params);
             BaseDao.closeResource(null,preparedStatement,null);
         }
         return updateRows;
    }

    //根据供应商ID查询订单数量
    public int getBillCountByProviderId(Connection con, String providerId) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int billCount = 0;
        if (con != null) {
            String sql = "select count(1) as billCount from smbms_bill where providerId = ?";
            Object[] params = {providerId};
            rs = BaseDao.execute(con, pstm, rs, sql, params);
            if (rs.next()) {
                billCount = rs.getInt("billCount");
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return billCount;
    }

}
