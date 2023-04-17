package com.lgx.servlet.bill;

import com.alibaba.fastjson.JSONArray;
import com.lgx.pojo.Bill;
import com.lgx.pojo.Provider;
import com.lgx.pojo.User;
import com.lgx.service.bill.BillServiceImpl;
import com.lgx.service.provider.ProviderServiceImpl;
import com.lgx.util.Constants;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("query")&&method!=null){
            this.getBillList(req,resp);
        }else if (method.equals("add")&&method!=null){
            this.addBill(req,resp);
        } else if (method.equals("delbill")&&method!=null) {
            this.delBill(req,resp);
        } else if (method.equals("view")&&method!=null) {
            this.getBillById(req,resp,"billview.jsp");
        } else if (method.equals("modify")&&method!=null) {
            this.getBillById(req,resp,"billmodify.jsp");
        } else if (method.equals("modifysave")&&method!=null) {
            this.modifyBill(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //展示表单列表
    public void getBillList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Provider> providerlist = new ArrayList<Provider>();
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        providerlist = providerService.getProviderList("", "");
        req.setAttribute("providerList", providerlist);
        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        if (StringUtils.isNullOrEmpty(queryProductName)) {
            queryProductName = "";
        }
        List<Bill> billList = new ArrayList<Bill>();
        BillServiceImpl billService = new BillServiceImpl();
        Bill bill = new Bill();
        if (StringUtils.isNullOrEmpty(queryIsPayment)) {
            bill.setIsPayment(0);
        } else {
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }

        if (StringUtils.isNullOrEmpty(queryProviderId)) {
            bill.setProviderId(0);
        } else {
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        bill.setProductName(queryProductName);
        billList = billService.getBillList(bill);
        req.setAttribute("billList", billList);
        req.setAttribute("queryProductName", queryProductName);
        req.setAttribute("queryProviderId", queryProviderId);
        req.setAttribute("queryIsPayment", queryIsPayment);
        req.getRequestDispatcher("billlist.jsp").forward(req, resp);
    }

    //添加订单
    public void addBill(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setCreatedBy(((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());

        boolean flag = false;
        BillServiceImpl billService = new BillServiceImpl();
        flag = billService.addBill(bill);
        if (flag) {
            resp.sendRedirect(req.getContextPath() + "/jsp/bill.do?method=query");
        } else {
            req.getRequestDispatcher("billadd.jsp").forward(req, resp);
        }
    }

    //删除订单
    public  void delBill(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String billid = req.getParameter("billid");
        System.out.println(billid);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(billid)){
            BillServiceImpl billService = new BillServiceImpl();
            boolean flag = billService.delBill(billid);
            if (flag){
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult","false");
            }
        }else {
            resultMap.put("delResult","notexist");
        }

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    //查看订单
    public void getBillById(HttpServletRequest req, HttpServletResponse resp,String url) throws ServletException, IOException {
        String billid = req.getParameter("billid");

        if (!StringUtils.isNullOrEmpty(billid)){
            BillServiceImpl billService = new BillServiceImpl();
            Bill bill = billService.getBillById(billid);
            req.setAttribute("bill",bill);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }

    //修改订单
    public void modifyBill(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("id");
        String productName = req.getParameter("productName");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");

        Bill bill = new Bill();
        bill.setId(Integer.valueOf(id));
        bill.setProductName(productName);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));

        bill.setModifyBy(((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());

        boolean flag = false;
        BillServiceImpl billService = new BillServiceImpl();
        flag = billService.modeifyBill(bill);

        if (flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else {
            req.getRequestDispatcher("billmodify.jsp").forward(req,resp);
        }
    }
}
