package org.ballad.common.springbootweb.redis;

import lombok.*;

@Setter
@Getter
@Data
public class StoObj {
    String s;
    Double i;

    public StoObj(String s, Double i) {
        this.s = s;
        this.i = i;
    }
}
