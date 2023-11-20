package com.personal.blog.controller;


import com.personal.blog.cover.BlogUserCover;
import com.personal.blog.pojo.BlogUser;
import com.personal.blog.service.IBlogUserService;
import com.personal.blog.service.impl.BlogUserServiceImpl;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogUser")
public class BlogUserController {

    @Resource
    private IBlogUserService iBlogUserService;

    @GetMapping("information")
    @ResponseBody
    public R<BlogUserCover> informationView(@Param("userId") Long userId) {
        return iBlogUserService.informationView(userId);
    }
    @PostMapping("uploadImg")
    @ResponseBody
    public R uploadImg(List<MultipartFile> file,@Param("userId") Integer userId) throws IOException {
        return iBlogUserService.uploadImg(file, userId);
    }

    @PostMapping("update")
    @ResponseBody
    public R<String> update(@RequestBody BlogUser blogUser) {
        return iBlogUserService.update(blogUser);
    }

    @GetMapping("messageAsStatus")
    @ResponseBody
    public R<HashMap<String, Boolean>> messageAsStatus(Long userId) {
        return iBlogUserService.messageAsStatus(userId);
    }
}

