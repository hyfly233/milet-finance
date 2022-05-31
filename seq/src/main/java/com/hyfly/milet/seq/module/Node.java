package com.hyfly.milet.seq.module;

import com.alipay.sofa.jraft.rhea.LeaderStateListener;
import com.alipay.sofa.jraft.rhea.client.DefaultRheaKVStore;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
public class Node {

    @NonNull
    private final RheaKVStoreOptions options;

    @Getter
    private RheaKVStore rheaKvStore;

    private final AtomicLong leaderTerm = new AtomicLong(-1);

    /**
     * 是否leader节点
     *
     * @return boolean
     */
    public boolean isLeader() {
        return leaderTerm.get() > 0;
    }

    /**
     * 停止Node
     */
    public void stop() {
        rheaKvStore.shutdown();
    }

    /**
     * 启动Node
     */
    public void start() {
        //初始化kvStore
        rheaKvStore = new DefaultRheaKVStore();
        rheaKvStore.init(this.options);
        //监听节点状态
        rheaKvStore.addLeaderStateListener(-1, new LeaderStateListener() {
            @Override
            public void onLeaderStart(long newTerm) {
                log.info("node become leader");
                leaderTerm.set(newTerm);
            }

            @Override
            public void onLeaderStop(long oldTerm) {
                leaderTerm.set(-1);
            }
        });
    }
}
