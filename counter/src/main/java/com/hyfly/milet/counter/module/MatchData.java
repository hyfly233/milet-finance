package com.hyfly.milet.counter.module;

import com.hyfly.milet.counter.enums.OrderStatus;
import lombok.Builder;

import java.io.Serializable;

@Builder
public class MatchData implements Serializable {

    public long timestamp;

    public short mid;

    public long oid;

    public OrderStatus status;

    public long tid;


    /**
     * 撤单数量 成交数量
     */
    public long volume;

    public long price;

}