package cn.itcast.travel.service;

public interface FavoriteSerivce {
    public boolean isFavorite(String rid, int uid);

    void add(String rid, int uid);
}
