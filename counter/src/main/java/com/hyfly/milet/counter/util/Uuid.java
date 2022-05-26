package com.hyfly.milet.counter.util;

public class Uuid {

    private static Uuid ourInstance = new Uuid();

    public static Uuid getInstance() {
        return ourInstance;
    }

    private Uuid() {
    }

    public void init(long centerId, long workerId) {
        idWorker = new SnowflakeIdWorker(workerId, centerId);
    }

    private SnowflakeIdWorker idWorker;

    public long getUUID() {
        return idWorker.nextId();
    }


}