package com.uc.service;

import java.util.List;

import com.uc.entity.APNTpl;

public interface APNTplService {

    public static final String APNTPL_KEY = "apntpl";

    public Boolean add(APNTpl o);

    public Long delete(double id);

    public Long delete(APNTpl o);

    public Long deleteAll();

    public Boolean update(APNTpl o);

    public List<APNTpl> findByPage(int pageSize, int pageNum);

    public List<APNTpl> findAll();

    public APNTpl findOne(double id);

    public Long findCount();
}
