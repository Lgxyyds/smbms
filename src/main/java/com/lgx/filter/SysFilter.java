package com.lgx.filter;


import com.lgx.pojo.User;
import com.lgx.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);

        if (user == null){
            response.sendRedirect(request.getContextPath()+"/error.jsp");
        }else {
            chain.doFilter(req,resp);
        }
    }

    public void destroy() {

    }
}
