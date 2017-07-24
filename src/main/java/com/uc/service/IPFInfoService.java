package com.uc.service;

import java.util.List;

import com.uc.entity.IPFInfo;

public interface IPFInfoService {

    public static final String OPTPL_KEY = "optpl";

    public Boolean add(IPFInfo o);

    public Long delete(IPFInfo o);

    public Long deleteAll();

    public Boolean update(IPFInfo o);

    public List<IPFInfo> findByPage(int pageSize, int pageNum);

    public List<IPFInfo> findAll();

    public IPFInfo findOne(int id);

    public Long findCount();
}
