package com.uc.service;

import java.util.List;

import com.uc.entity.APNGroupTpl;

public interface APNGroupTplService {

    public static final String APNGRPTPL_KEY = "apngrptpl";

    public Boolean add(APNGroupTpl o);

    public Long delete(APNGroupTpl o);

    public Long deleteAll();

    public Boolean update(APNGroupTpl o);

    public List<APNGroupTpl> findByPage(int pageSize, int pageNum);

    public List<APNGroupTpl> findAll();

    public APNGroupTpl findOne(int id);

    public Long findCount();
}
