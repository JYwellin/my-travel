package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteSerivce;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteSerivceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.ResponseCache;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteSerivce favoriteSerivce = new FavoriteSerivceImpl();

    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        if (rname != null){
            rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
        }

        int cid = 0;
        if(cidStr !=null && cidStr.length() > 0  && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }

        int currentPage;
        if(currentPageStr !=null && currentPageStr.length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else{
            currentPage = 1;
        }

        int pageSize;
        if(pageSizeStr !=null && pageSizeStr.length() > 0){
            pageSize = Integer.parseInt(cidStr);
        }else{
            pageSize = 5;
        }

        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);
//        response.setContentType("text/html;charset=utf-8");
//        response.getWriter().write(pb.toString());
        writeValue(pb,response);
    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        Route route = routeService.findOne(rid);
        writeValue(route,response);
    }

    //判断当前登录用户是否收藏过该线路
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User)request.getSession().getAttribute("user");
        int uid;
        if(user == null){
            uid = 0;
        }
        else{
            uid = user.getUid();
        }
        boolean flag = favoriteSerivce.isFavorite(rid, uid);
        writeValue(flag,response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User)request.getSession().getAttribute("user");
        if(user == null) return;
        int uid = user.getUid();
        favoriteSerivce.add(rid, uid);
    }
}
