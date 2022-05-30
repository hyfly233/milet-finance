package com.hyfly.milet.counter.controller;

import com.hyfly.milet.counter.cache.RedisStringCache;
import com.hyfly.milet.counter.enums.CacheType;
import com.hyfly.milet.counter.module.Account;
import com.hyfly.milet.counter.module.res.CaptchaRes;
import com.hyfly.milet.counter.module.res.CounterRes;
import com.hyfly.milet.counter.service.AccountService;
import com.hyfly.milet.counter.util.Captcha;
import com.hyfly.milet.counter.util.Uuid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @Autowired
    private AccountService accountService;

    @RequestMapping("/captcha")
    public CounterRes captcha() throws Exception {
        //1.生成验证码 120 40 4个字符 噪点+线条
        Captcha captcha = new Captcha(120, 40, 4, 10);

        //2.将验证码<ID,验证码数值>放入缓存
        String uuid = String.valueOf(Uuid.getInstance().getUUID());
        RedisStringCache.cache(uuid, captcha.getCode(), CacheType.CAPTCHA);

        //3.使用base64编码图片，并返回给前台
        //uuid,base64
        CaptchaRes res = new CaptchaRes(uuid, captcha.getBase64ByteStr());
        return new CounterRes(res);
    }

    @RequestMapping("/userlogin")
    public CounterRes login(@RequestParam long uid, @RequestParam String password, @RequestParam String captcha, @RequestParam String captchaId) throws Exception {
        Account account = accountService.login(uid, password, captcha, captchaId);

        if (account == null) {
            return new CounterRes(CounterRes.FAIL, "用户名密码/验证码错误，登录失败", null);
        } else {
            return new CounterRes(account);
        }
    }

    @RequestMapping("/loginfail")
    public CounterRes loginFail() {
        return new CounterRes(CounterRes.RE_LOGIN, "请重新登陆", null);
    }

    @RequestMapping("/logout")
    public CounterRes logout(@RequestParam String token) {
        accountService.logout(token);
        return new CounterRes(CounterRes.SUCCESS, "退出成功", null);
    }

    @RequestMapping("pwdupdate")
    public CounterRes pwdUpdate(@RequestParam int uid, @RequestParam String oldpwd, @RequestParam String newpwd) {
        boolean res = accountService.updatePwd(uid, oldpwd, newpwd);
        if (res) {
            return new CounterRes(CounterRes.SUCCESS, "密码更新成功", null);
        } else {
            return new CounterRes(CounterRes.FAIL, "密码更新失败", null);
        }
    }
}
