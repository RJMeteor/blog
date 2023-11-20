package com.personal.blog.service;

import com.personal.blog.pojo.BlogFans;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.utils.R;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 粉丝 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogFansService extends IService<BlogFans> {
    R<List<BlogFans>> list(Long userId);
}
