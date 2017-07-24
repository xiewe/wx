package com.uc.service;

import java.util.List;

import com.uc.entity.BizTpl;

public interface BizTplService {

    public static final String BIZTPL_KEY = "biztpl";

    public Boolean add(BizTpl o);

    public Long delete(BizTpl o);

    public Long deleteAll();

    public Boolean update(BizTpl o);

    public List<BizTpl> findByPage(int pageSize, int pageNum);

    public List<BizTpl> findAll();

    public BizTpl findOne(int id);

    public Long findCount();
}
