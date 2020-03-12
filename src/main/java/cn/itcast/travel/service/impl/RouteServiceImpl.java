package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装pageBean并返回
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        int totalCount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        int start = (currentPage -1) * pageSize;
        List<Route> list = routeDao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);
        int totalpage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalpage);
        return pb;
    }

    @Override
    public Route findOne(String rid) {
        Route route = routeDao.findOne(Integer.parseInt(rid));
        List<RouteImg> routeImgs = routeImgDao.findByRid(Integer.parseInt(rid));
        route.setRouteImgList(routeImgs);
        Seller seller = sellerDao.findByid(route.getSid());
        route.setSeller(seller);
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }
}
