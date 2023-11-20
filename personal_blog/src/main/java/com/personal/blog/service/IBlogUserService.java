package com.personal.blog.service;

import com.personal.blog.cover.BlogUserCover;
import com.personal.blog.exceptions.GrantException;
import com.personal.blog.pojo.BlogUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.personal.blog.pojo.Login;
import com.personal.blog.utils.R;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
public interface IBlogUserService extends IService<BlogUser> {
    public R request(Login login) throws LoginException, GrantException;

    public R logout(Login login);

    public R regist(Login login) throws LoginException, GrantException;

    public R<BlogUserCover> informationView(Long userId);

    public R uploadImg(List<MultipartFile> file,Integer userId) throws IOException;

    public R<String> update(BlogUser blogUser);

    public R<HashMap<String, Boolean>> messageAsStatus(Long userId);
}
