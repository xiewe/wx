package com.uc.service;

import java.util.List;

import com.uc.entity.OPTpl;

public interface OPTplService {

    public static final String OPTPL_KEY = "optpl";

    public Boolean add(OPTpl o);

    public Long delete(double id);

    public Long delete(OPTpl o);

    public Long deleteAll();

    public Boolean update(OPTpl o);

    public List<OPTpl> findByPage(int pageSize, int pageNum);

    public List<OPTpl> findAll();

    public OPTpl findOne(double id);

    public Long findCount();
}
