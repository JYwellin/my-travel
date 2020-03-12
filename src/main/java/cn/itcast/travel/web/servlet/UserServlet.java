package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    private UserService userService= new UserServiceImpl();
    //注册
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//保证验证码只能使用一次
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            writeValue(resultInfo,response);
            return;
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        boolean flag = userService.regist(user);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(flag);
        if (!flag) {
            resultInfo.setErrorMsg("用户名重复，注册失败");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultInfo);
        response.setContentType("application/json;charset = utf-8");
        response.getWriter().write(json);
    }

    //登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        User u = userService.login(user);
        //判断用户对象是否为null
        ResultInfo resultInfo = new ResultInfo();
        if(u == null){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名或密码错误");
        }else if (u.getStatus().equals("N")){
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("该账号尚未激活");
        }else{
            resultInfo.setFlag(true);
            request.getSession().setAttribute("user",u);
        }
        writeValue(resultInfo,response);
    }

    //查找用户
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");
        writeValue(user,response);
    }

    //退出
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+"/login.html");
    }

    //激活
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != ""){
            boolean flag = userService.active(code);
            String msg = null;
            if(flag){
                msg = "激活成功，请<a href ='login.html'>登录<a>";
            }else{
                msg = "激活失败，请联系管理员";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

}
