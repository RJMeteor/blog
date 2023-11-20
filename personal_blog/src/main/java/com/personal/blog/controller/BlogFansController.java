package com.personal.blog.controller;


import com.personal.blog.pojo.BlogFans;
import com.personal.blog.service.IBlogFansService;
import com.personal.blog.utils.R;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 粉丝 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogFans")
public class BlogFansController {

    @Resource
    private IBlogFansService iBlogFansService;

    @GetMapping("/list")
    @ResponseBody
    public R<List<BlogFans>> list(@RequestParam("userId")Long userId){
        return iBlogFansService.list(userId);
    }
}

