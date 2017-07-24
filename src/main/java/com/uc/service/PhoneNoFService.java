package com.uc.service;

import java.util.List;

import com.uc.entity.PhoneNoFInfo;

public interface PhoneNoFService {

    public static final String PHONENOF_KEY = "phonenof";

    public Boolean add(PhoneNoFInfo o);

    public Long delete(double id);

    public Long delete(PhoneNoFInfo o);

    public Long deleteAll();

    public Boolean update(PhoneNoFInfo o);

    public List<PhoneNoFInfo> findByPage(int pageSize, int pageNum);

    public List<PhoneNoFInfo> findAll();

    public PhoneNoFInfo findOne(double id);

    public Long findCount();
}
