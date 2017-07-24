package com.uc.service;

import java.util.List;

import com.uc.entity.BlackWhiteList;

public interface BlackWhiteListService {

    public static final String BWLIST_KEY = "bwlist";

    public Boolean add(BlackWhiteList o);

    public Long delete(BlackWhiteList o);

    public Long deleteAll();

    public Boolean update(BlackWhiteList o);

    public List<BlackWhiteList> findByPage(int pageSize, int pageNum);

    public List<BlackWhiteList> findAll();

    public BlackWhiteList findOne(int id);

    public Long findCount();
}
