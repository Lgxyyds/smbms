package com.lgx.service.provider;

import com.lgx.pojo.Provider;

import java.util.List;

public interface ProviderService {
    //获取供应商列表
    public List<Provider> getProviderList(String proName,String proCode);

    //添加供应商
    public Boolean addProvider(Provider provider);

    //通过proId删除Provider
    int deleteProviderById(String delId);

    //通过proId获取Provider
    Provider getProviderById(String id);

    //修改供应商
    public boolean modifyProvider(Provider provider);
}
