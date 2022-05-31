package com.hyfly.milet.engine.module.info;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferInfo {

    private long uid;

    private String date;

    private String time;

    private String bank;

    private int type;

    private int moneyType;

    private long money;


}
