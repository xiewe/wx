package com.uc.service;

import java.util.List;

import com.uc.entity.IPFInfo;

public interface IPFInfoService {

    public static final String IPFINFO_KEY = "ipfinfo";

    public Boolean add(IPFInfo o);

    public Long delete(double id);

    public Long delete(IPFInfo o);

    public Long deleteAll();

    public Boolean update(IPFInfo o);

    public List<IPFInfo> findByPage(int pageSize, int pageNum);

    public List<IPFInfo> findAll();

    public IPFInfo findOne(double id);

    public Long findCount();
}
