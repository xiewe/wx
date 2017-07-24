package com.uc.service;

import java.util.List;

import com.uc.entity.IMSIInfo;

public interface IMSIInfoService {

    public static final String IMSIINFO_KEY = "imsiinfo";

    public Boolean add(IMSIInfo o);

    public Long delete(IMSIInfo o);

    public Long deleteAll();

    public Boolean update(IMSIInfo o);

    public List<IMSIInfo> findByPage(int pageSize, int pageNum);

    public List<IMSIInfo> findAll();

    public IMSIInfo findOne(int id);

    public Long findCount();
}
