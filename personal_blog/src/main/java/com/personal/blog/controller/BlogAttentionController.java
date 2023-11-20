package com.personal.blog.controller;


import com.personal.blog.cover.EventAttention;
import com.personal.blog.cover.EventLike;
import com.personal.blog.pojo.BlogAttention;
import com.personal.blog.pojo.BlogFans;
import com.personal.blog.service.IBlogAttentionService;
import com.personal.blog.service.IBlogFansService;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 关注 前端控制器
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Controller
@RequestMapping("/blogAttention")
public class BlogAttentionController {
    @Resource
    private IBlogAttentionService iBlogAttentionService;

    @GetMapping("/list")
    @ResponseBody
    public R<List<BlogAttention>> list(@RequestParam("userId")Long userId){
        return iBlogAttentionService.list(userId);
    }

    @GetMapping("attentionListAsActvie")
    @ResponseBody
    public R<List<EventAttention>> attentionListAsActvie(@RequestParam("userId")Long id){
        return  iBlogAttentionService.attentionListAsActvie(id);
    }

    @PutMapping("attention")
    @ResponseBody
    public R<String> attention(@RequestBody BlogAttention blogAttention){
        return  iBlogAttentionService.attention(blogAttention);
    }

    @DeleteMapping("deleteAsttentionListAsActive")
    @ResponseBody
    public R<String> deleteAsttentionListAsActive(@RequestParam("asttentionId")Long asttentionId){
        return  iBlogAttentionService.deleteAsttentionListAsActive(asttentionId);
    }

    @DeleteMapping("deleteAttentionAllAsActive")
    @ResponseBody
    public R<String> deleteAttentionAllAsActive(@RequestParam("userId")Long userId){
        return  iBlogAttentionService.deleteAttentionAllAsActive(userId);
    }

    @PostMapping("updateAttentionAllAsStatus")
    @ResponseBody
    public R<String> updateAttentionAllAsStatus(@RequestParam("userId")Long userId){
        return  iBlogAttentionService.updateAttentionAllAsStatus(userId);
    }
}



