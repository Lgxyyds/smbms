package com.lgx.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.lgx.pojo.Role;
import com.lgx.pojo.User;
import com.lgx.service.role.RoleServiceImpl;
import com.lgx.service.user.UserService;
import com.lgx.service.user.UserServiceImpl;
import com.lgx.util.Constants;
import com.lgx.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("savepwd")&&method!=null){
            this.updatepwd(req,resp);
        }else if(method.equals("pwdmodify")&&method!=null){
            this.pwdModify(req,resp);
        } else if (method.equals("query")&&method!=null) {
            this.query(req,resp);
        } else if (method.equals("add")&&method!=null) {
            this.addUser(req,resp);
        } else if (method.equals("deluser")&&method!=null) {
            this.delUser(req,resp);
        } else if (method.equals("view")&&method!=null) {
            this.getUserById(req,resp,"userview.jsp");
        } else if (method.equals("modify")&&method!=null) {
            this.getUserById(req,resp,"usermodify.jsp");
        }else if (method.equals("modifyexe")&&method!=null){
            this.modify(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }


    //查询展示用户列表
    public void query(HttpServletRequest req, HttpServletResponse resp){
        //获取前端数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = null;

        //第一次请求肯定是走第一页，页面大小固定
        //设置页面大小
        int pageSize = 5;
        //当前页码
        int currentPageNo = 1;

        if (queryUserName == null){
            queryUserName = "";
        }
        if (temp != null && temp != ""){
            queryUserRole = Integer.parseInt(temp);
        }
        if (pageIndex != null){
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户总数（分页 上一页：下一页的情况）
        //总数量
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);

        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();//总共有几页

        //控制首页和尾页
        if(currentPageNo<1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }

        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);

        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //修改用户密码
    public void updatepwd(HttpServletRequest req, HttpServletResponse resp){
        //通过session获取用户id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");

        boolean flag = false;
        if (o!=null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(),newpassword);
            if (flag){
                req.setAttribute("message","修改密码成功，请退出，重新登录");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message","修改密码失败");
            }
        }else {
            req.setAttribute("message","新密码有问题");
        }
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //验证用户旧密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        Map<String, String> resultmap = new HashMap<String, String>();

        if (o==null){
            resultmap.put("result","sessionerror");
        }else if (StringUtils.isNullOrEmpty(oldpassword)){
            resultmap.put("result","error");
        }else {
            String userPassword = ((User) o).getUserPassword();
            if (userPassword.equals(oldpassword)){
                resultmap.put("result","true");
            }else {
                resultmap.put("result","false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            /*
             * resultMap = ["result","sessionerror","result",error]
             * josn格式={key,value
             */
            //writer.write(JSONArray.toJSONString(resultMap));
            writer.write(JSONArray.toJSONString(resultmap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //添加用户
    public void addUser(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        UserServiceImpl userService = new UserServiceImpl();
        Boolean flag = userService.addUser(user);
        System.out.println(flag);
        if (flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else {
            req.getRequestDispatcher("useradd.jsp").forward(req,resp);
        }
    }

    //删除用户
    public void delUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uid = req.getParameter("uid");
        Integer delId = 0;

        delId = Integer.parseInt(uid);

        HashMap<String,String> resultMap = new HashMap<String,String>();

        if (delId<=0){
            resultMap.put("delResult","notexist");
        }else {
            UserServiceImpl userService = new UserServiceImpl();
            Boolean flag = userService.deleteUserById(delId);
            if (flag){
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult","false");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }
    
    //查看用户
    public void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        if (!StringUtils.isNullOrEmpty(uid)){
            UserServiceImpl userService = new UserServiceImpl();
            User user = userService.getUserById(uid);
            req.setAttribute("user",user);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }

    //修改用户
    public void modify(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String uid = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setId(Integer.parseInt(uid));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyDate(new Date());
        user.setModifyBy(((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        UserServiceImpl userService = new UserServiceImpl();
        Boolean flag = userService.modify(user);
        if (flag){
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        }else {
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }
    }
}
