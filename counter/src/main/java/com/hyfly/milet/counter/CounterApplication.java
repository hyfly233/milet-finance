package com.hyfly.milet.counter;

import com.hyfly.milet.counter.config.CounterConfig;
import com.hyfly.milet.counter.util.DbUtil;
import com.hyfly.milet.counter.util.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author hyfly
 */
@SpringBootApplication
public class CounterApplication {
    @Autowired
    private DbUtil dbUtil;

    @Autowired
    private CounterConfig counterConfig;

    @PostConstruct
    private void init() {
        Uuid.getInstance().init(counterConfig.getDataCenterId(), counterConfig.getWorkerId());
    }

    public static void main(String[] args) {
        SpringApplication.run(CounterApplication.class, args);
    }

}
