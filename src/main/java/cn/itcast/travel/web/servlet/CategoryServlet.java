package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import cn.itcast.travel.util.JedisUtil;
import cn.itcast.travel.web.servlet.BaseServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    CategoryService categoryService = new CategoryServiceImpl();
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> findall = categoryService.findall();
        writeValue(findall,response);
    }
}
