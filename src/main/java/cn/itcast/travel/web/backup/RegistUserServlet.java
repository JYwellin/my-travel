package cn.itcast.travel.web.backup;

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

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//保证验证码只能使用一次
        if( checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(resultInfo);
            response.setContentType("application/json;charset = utf-8");
            response.getWriter().write(json);
            return;
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService = new UserServiceImpl();
        boolean flag = userService.regist(user);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setFlag(flag);
        if(!flag){
            resultInfo.setErrorMsg("用户名重复，注册失败");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultInfo);
        response.setContentType("application/json;charset = utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
