package com.hyfly.milet.counter.service;

import com.hyfly.milet.counter.module.Account;

public interface AccountService {
    /**
     * 登陆
     *
     * @param uid       uid
     * @param password  password
     * @param captcha   captcha
     * @param captchaId captchaId
     * @return account
     * @throws Exception e
     */
    Account login(long uid, String password, String captcha, String captchaId) throws Exception;

    /**
     * 缓存中是否存在已登录信息
     *
     * @param token
     * @return
     */
    boolean accountExistInCache(String token);

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    boolean logout(String token);

    boolean updatePwd(long uid, String oldPwd, String newPwd);
}