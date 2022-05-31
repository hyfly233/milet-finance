package com.hyfly.milet.gateway.service;

import com.hyfly.milet.gateway.module.res.OrderCmd;

import java.util.List;

public interface IFetchService {

    List<OrderCmd> fetchData();

}
