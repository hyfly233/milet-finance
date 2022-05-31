package com.hyfly.milet.seq.module;

import com.hyfly.milet.seq.module.res.OrderCmd;
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
