package com.hyfly.milet.engine.module;

import com.hyfly.milet.engine.module.res.OrderCmd;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CmdPack implements Serializable {

    private long packNo;

    private List<OrderCmd> orderCmds;

}
