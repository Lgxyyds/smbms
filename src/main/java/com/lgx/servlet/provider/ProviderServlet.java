package com.lgx.servlet.provider;

import com.alibaba.fastjson.JSONArray;
import com.lgx.pojo.Provider;
import com.lgx.pojo.User;
import com.lgx.service.provider.ProviderServiceImpl;
import com.lgx.util.Constants;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("query")&&method!=null){
            this.query(req,resp);
        } else if (method.equals("add")&&method!=null) {
            this.addProvider(req,resp);
        } else if (method.equals("delprovider")&&method!=null) {
            this.delProvider(req,resp);
        } else if (method.equals("view")&&method!=null) {
            this.getPorviderById(req,resp,"providerview.jsp");
        } else if (method.equals("modify")&&method!=null) {
            this.getPorviderById(req,resp,"providermodify.jsp");
        } else if (method.equals("modifysave")&&method!=null) {
            this.modifyProvider(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //展示供应商列表
    public void query(HttpServletRequest req, HttpServletResponse resp){
        String queryProName = req.getParameter("queryProName");
        String queryProCode = req.getParameter("queryProCode");

        ProviderServiceImpl providerService = new ProviderServiceImpl();
        List<Provider> providerList = providerService.getProviderList(queryProName, queryProCode);

        req.setAttribute("providerList",providerList);
        req.setAttribute("queryProName",queryProName);
        req.setAttribute("queryProCode",queryProCode);

        try {
            req.getRequestDispatcher("providerlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //添加供应商
    public void addProvider(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        System.out.println(proName);
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");

        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());

        ProviderServiceImpl providerService = new ProviderServiceImpl();
        Boolean flag = providerService.addProvider(provider);
        if (flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else {
            req.getRequestDispatcher("provideradd.jsp").forward(req,resp);
        }
    }

    //删除供应商
    public void delProvider(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("proid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)) {
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            int flag = providerService.deleteProviderById(id);
            if (flag == 0) {
                //删除成功
                resultMap.put("delResult", "true");
            } else if (flag == -1) {
                //删除失败
                resultMap.put("delResult", "false");
            } else if (flag > 0) {
                //该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    //根据id获取供应商
    public void getPorviderById(HttpServletRequest request, HttpServletResponse response,String url) throws ServletException, IOException {
        String id = request.getParameter("proid");
        String porname = request.getParameter("porname");
        if (!StringUtils.isNullOrEmpty(id)) {
            ProviderServiceImpl providerService = new ProviderServiceImpl();
            Provider provider = null;
            provider = providerService.getProviderById(id);
            request.setAttribute("provider", provider);
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    //根据id修改供应商
    public void modifyProvider(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String proCode = request.getParameter("proCode");
        String proName = request.getParameter("proName");
        System.out.println(proName);
        String proContact = request.getParameter("proContact");
        String proPhone = request.getParameter("proPhone");
        String proAddress = request.getParameter("proAddress");
        String proFax = request.getParameter("proFax");
        String proDesc = request.getParameter("proDesc");
        String id = request.getParameter("proid");
        Provider provider = new Provider();
        provider.setId(Integer.valueOf(id));
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setModifyBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        System.out.println(provider.toString());
        ProviderServiceImpl providerService = new ProviderServiceImpl();
        boolean flag = providerService.modifyProvider(provider);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/jsp/provider.do?method=query");
        }else {
            request.getRequestDispatcher("providermodify.jsp").forward(request,response);
        }
    }
}
