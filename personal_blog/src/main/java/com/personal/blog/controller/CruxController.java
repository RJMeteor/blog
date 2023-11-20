package com.personal.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.personal.blog.exceptions.GrantException;
import com.personal.blog.interfaces.FilterInterface;
import com.personal.blog.pojo.Login;
import com.personal.blog.service.IBlogUserService;
import com.personal.blog.utils.JWTUtil;
import com.personal.blog.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;

@Controller
@RequestMapping("crux")
public class CruxController extends FilterInterface {
    @Resource
    private IBlogUserService iBlogUserService;

    @PostMapping("login")
    @ResponseBody
    public R request(@RequestBody Login login) throws LoginException, GrantException {
        return iBlogUserService.request(login);
    }

    @PostMapping("logout")
    @ResponseBody
    public R logout(@RequestBody Login login) {
        return  iBlogUserService.logout(login);
    }

    @PostMapping("regist")
    @ResponseBody
    public R regist(@RequestBody Login login) throws LoginException, GrantException {
        return  iBlogUserService.regist(login);
    }


}
