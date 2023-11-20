package com.personal.blog.service.impl;

import com.personal.blog.pojo.BlogFans;
import com.personal.blog.mapper.BlogFansMapper;
import com.personal.blog.service.IBlogFansService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.utils.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 粉丝 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogFansServiceImpl extends ServiceImpl<BlogFansMapper, BlogFans> implements IBlogFansService {
    @Resource
    private BlogFansMapper blogFansMapper;

    @Override
    public R<List<BlogFans>> list(Long userId) {
        List<BlogFans> blogFans = blogFansMapper.fansList(userId);
        R<List<BlogFans>> r = new R<List<BlogFans>>();
        r.success(blogFans);
        return r;
    }
}
