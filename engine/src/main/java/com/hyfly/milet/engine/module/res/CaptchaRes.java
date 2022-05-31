package com.hyfly.milet.engine.module.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CaptchaRes {

    private String id;

    private String imageBase64;

}