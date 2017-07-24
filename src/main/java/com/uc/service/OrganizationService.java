package com.uc.service;

import java.util.List;

import com.uc.entity.Organization;

public interface OrganizationService {

    public static final String ORGANIZATION_KEY = "org";

    public Boolean add(Organization o);

    public Long delete(double id);

    public Long delete(Organization o);

    public Long deleteAll();

    public Boolean update(Organization o);

    public List<Organization> findByPage(int pageSize, int pageNum);

    public List<Organization> findAll();

    public Organization findOne(double id);

    public Long findCount();
}
