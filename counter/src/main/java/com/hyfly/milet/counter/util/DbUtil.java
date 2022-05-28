package com.hyfly.milet.counter.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hyfly.milet.counter.cache.RedisStringCache;
import com.hyfly.milet.counter.enums.CacheType;
import com.hyfly.milet.counter.enums.OrderStatus;
import com.hyfly.milet.counter.module.Account;
import com.hyfly.milet.counter.module.MatchData;
import com.hyfly.milet.counter.module.info.OrderInfo;
import com.hyfly.milet.counter.module.info.PosiInfo;
import com.hyfly.milet.counter.module.info.TradeInfo;
import com.hyfly.milet.counter.module.res.OrderCmd;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class DbUtil {
    private static DbUtil dbUtil = null;

    private DbUtil() {
    }

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @PostConstruct
    public void init() {
        dbUtil = new DbUtil();
        dbUtil.setSqlSessionTemplate(this.sqlSessionTemplate);
    }

    private void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    private SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }


    public static Account queryAccount(long uid, String password) {
        return dbUtil.getSqlSessionTemplate().selectOne(
                "userMapper.queryAccount",
                ImmutableMap.of("UId", uid, "Password", password)
        );
    }

    public static void updateLoginTime(long uid, String nowDate,
                                       String nowTime) {
        dbUtil.getSqlSessionTemplate().update(
                "userMapper.updateAccountLoginTime",
                ImmutableMap.of(
                        "UId", uid,
                        "ModifyDate", nowDate,
                        "ModifyTime", nowTime
                )
        );
    }

    public static int updatePwd(long uid, String oldPwd,
                                String newPwd) {
        return dbUtil.getSqlSessionTemplate().update(
                "userMapper.updatePwd",
                ImmutableMap.of("UId", uid,
                        "NewPwd", newPwd,
                        "OldPwd", oldPwd));
    }

    public static long getBalance(long uid) {
        Long res = dbUtil.getSqlSessionTemplate()
                .selectOne("orderMapper.queryBalance",
                        ImmutableMap.of("UId", uid));
        if (res == null) {
            return -1;
        } else {
            return res;
        }
    }

    public static List<PosiInfo> getPosiList(long uid) {
        //查缓存
        String suid = Long.toString(uid);
        String posiS = RedisStringCache.get(suid, CacheType.POSI);
        if (StringUtils.isEmpty(posiS)) {
            //未查到 查库
            List<PosiInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryPosi",
                    ImmutableMap.of("UId", uid)
            );
            List<PosiInfo> result = CollectionUtils.isEmpty(tmp) ? Lists.newArrayList() : tmp;
            //更新缓存
            RedisStringCache.cache(suid, JsonUtil.toJson(result), CacheType.POSI);
            return result;
        } else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(posiS, PosiInfo.class);
        }
    }

    public static List<OrderInfo> getOrderList(long uid) {
        //查缓存
        String suid = Long.toString(uid);
        String orderS = RedisStringCache.get(suid, CacheType.ORDER);
        if (StringUtils.isEmpty(orderS)) {
            //未查到 查库
            List<OrderInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryOrder",
                    ImmutableMap.of("UId", uid)
            );
            List<OrderInfo> result =
                    CollectionUtils.isEmpty(tmp) ? Lists.newArrayList()
                            : tmp;
            //更新缓存
            RedisStringCache.cache(suid, JsonUtil.toJson(result), CacheType.ORDER);
            return result;
        } else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(orderS, OrderInfo.class);
        }
    }

    public static List<TradeInfo> getTradeList(long uid) {
        //查缓存
        String suid = Long.toString(uid);
        String tradeS = RedisStringCache.get(suid, CacheType.TRADE);
        if (StringUtils.isEmpty(tradeS)) {
            //未查到 查库
            List<TradeInfo> tmp = dbUtil.getSqlSessionTemplate().selectList(
                    "orderMapper.queryTrade",
                    ImmutableMap.of("UId", uid)
            );
            List<TradeInfo> result =
                    CollectionUtils.isEmpty(tmp) ? Lists.newArrayList()
                            : tmp;
            //更新缓存
            RedisStringCache.cache(suid, JsonUtil.toJson(result), CacheType.TRADE);
            return result;
        } else {
            //查到 命中缓存
            return JsonUtil.fromJsonArr(tradeS, TradeInfo.class);
        }
    }

    public static int saveOrder(OrderCmd orderCmd) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("UId", orderCmd.uid);
        param.put("Code", orderCmd.code);
        param.put("Direction", orderCmd.direction.getDirection());
        param.put("Type", orderCmd.orderType.getType());
        param.put("Price", orderCmd.price);
        param.put("OCount", orderCmd.volume);
        param.put("TCount", 0);
        param.put("Status", OrderStatus.NOT_SET.getCode());

        param.put("Data", TimeformatUtil.yyyyMMdd(orderCmd.timestamp));
        param.put("Time", TimeformatUtil.hhMMss(orderCmd.timestamp));

        int count = dbUtil.getSqlSessionTemplate().insert(
                "orderMapper.saveOrder", param
        );
        //判断是否成功
        if (count > 0) {
            return Integer.parseInt(param.get("ID").toString());
        } else {
            return -1;
        }
    }

    public static List<Map<String, Object>> queryAllSotckInfo() {
        return dbUtil.getSqlSessionTemplate()
                .selectList("stockMapper.queryStock");
    }

    public static void minusPosi(long uid, int code, long volume, long price) {
        addPosi(uid, code, -volume, price);
    }

    public static PosiInfo getPosi(long uid, int code) {
        return dbUtil.getSqlSessionTemplate().selectOne("orderMapper.queryPosi",
                ImmutableMap.of("UId", uid, "Code", code));
    }

    public static void addPosi(long uid, int code, long volume, long price) {
        //持仓是否存在
        PosiInfo posiInfo = getPosi(uid, code);
        if (posiInfo == null) {
            //新增一条持仓
            insertPosi(uid, code, volume, price);
        } else {
            //修改持仓
            posiInfo.setCount(posiInfo.getCount() + volume);
            posiInfo.setCost(posiInfo.getCost() + price * volume);
//            if(posiInfo.getCount() == 0){
//                deletePosi(posi);
//            }else {
            updatePosi(posiInfo);
//            }

        }
    }

    private static void updatePosi(PosiInfo posiInfo) {
        dbUtil.getSqlSessionTemplate().insert("orderMapper.updatePosi",
                ImmutableMap.of("UId", posiInfo.getUid(),
                        "Code", posiInfo.getCode(),
                        "Count", posiInfo.getCount(),
                        "Cost", posiInfo.getCost())
        );
    }

    private static void insertPosi(long uid, int code, long volume, long price) {
        dbUtil.getSqlSessionTemplate().insert("orderMapper.insertPosi",
                ImmutableMap.of("UId", uid,
                        "Code", code,
                        "Count", volume,
                        "Cost", volume * price)
        );
    }

    public static void updateOrder(long uid, int oid, OrderStatus status) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("Id", oid);
        param.put("Status", status.getCode());
        dbUtil.getSqlSessionTemplate().update("orderMapper.updateOrder", param);

        RedisStringCache.remove(Long.toString(uid), CacheType.ORDER);
    }


    public static void saveTrade(int counterOId, MatchData md, OrderCmd orderCmd) {
        if (orderCmd == null) {
            return;
        }
        Map<String, Object> param = Maps.newHashMap();
        param.put("Id", md.tid);
        param.put("UId", orderCmd.uid);
        param.put("Code", orderCmd.code);
        param.put("Direction", orderCmd.direction.getDirection());
        param.put("Price", md.price);
        param.put("TCount", md.volume);
        param.put("OId", counterOId);//高位为counterid)
        param.put("Date", TimeformatUtil.yyyyMMdd(md.timestamp));
        param.put("Time", TimeformatUtil.hhMMss(md.timestamp));
        dbUtil.getSqlSessionTemplate().insert("orderMapper.saveTrade", param);

        //更新缓存
        RedisStringCache.remove(Long.toString(orderCmd.uid), CacheType.TRADE);

    }

    public static void minusBalance(long uid, long balance) {
        addBalance(uid, -balance);
    }

    public static void addBalance(long uid, long balance) {
        dbUtil.getSqlSessionTemplate().update("orderMapper.updateBalance",
                ImmutableMap.of("UId", uid, "Balance", balance));
    }
}
