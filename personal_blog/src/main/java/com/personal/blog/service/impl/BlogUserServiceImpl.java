package com.personal.blog.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.personal.blog.config.OssConfig;
import com.personal.blog.cover.BlogUserCover;
import com.personal.blog.cover.EventPrivateLetter;
import com.personal.blog.exceptions.GrantException;
import com.personal.blog.interfaces.FilterInterface;
import com.personal.blog.pojo.*;
import com.personal.blog.mapper.BlogUserMapper;
import com.personal.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personal.blog.utils.JWTUtil;
import com.personal.blog.utils.R;
import org.mapstruct.ap.internal.model.assignment.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.ArrayUtils;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.aliyun.oss.event.ProgressEventType.TRANSFER_COMPLETED_EVENT;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Service
public class BlogUserServiceImpl extends ServiceImpl<BlogUserMapper, BlogUser> implements IBlogUserService {
    @Resource
    private BlogUserMapper blogUserMapper;
    @Resource
    private IBlogUserService iBlogUserService;

    @Resource
    private IBlogFavoriteService iBlogFavoriteService;

    @Resource
    private IBlogAttentionService iBlogAttentionService;
    @Resource
    private IBlogChattingRecordsService iBlogChattingRecordsService;
    @Resource
    private IBlogLikeService iBlogLikeService;
    @Resource
    private IBlogCommentService iBlogCommentService;
    @Resource
    private IBlogArticlePushService iBlogArticlePushService;
    @Resource
    private IBlogArticleService iBlogArticleService;
    @Resource
    private OSS ossClient;
    @Resource
    private OssConfig ossConfig;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public R request(Login login) throws LoginException, GrantException {
        LambdaQueryWrapper<BlogUser> blogUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogUserLambdaQueryWrapper.eq(BlogUser::getUserPhone, login.getPhone())
                .eq(BlogUser::getUserPass, login.getPassword());
        BlogUser blogUser = this.baseMapper.selectOne(blogUserLambdaQueryWrapper);
        R r = new R();
        if (ObjectUtils.isEmpty(blogUser)) {
            r.error("没有该用户");
        } else {
            String encodeKey = new StringBuffer(blogUser.getUserPhone()).append(blogUser.getUserPass()).toString();
            String encode = jwtUtil.encodeToken(encodeKey);
            FilterInterface.userToken.remove(encodeKey);
            FilterInterface.userToken.put(encodeKey, encode);
            blogUser.setToken(encode);
            r.success(blogUser);
        }
        return r;
    }

    @Override
    public R logout(Login login) {

        return null;
    }

    @Override
    public R regist(Login login) throws LoginException, GrantException {
        LambdaQueryWrapper<BlogUser> blogUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogUserLambdaQueryWrapper.eq(BlogUser::getUserPhone, login.getPhone());
        BlogUser blogUser = this.baseMapper.selectOne(blogUserLambdaQueryWrapper);
        R r = new R();
        if (!ObjectUtils.isEmpty(blogUser)) {
            r.error("该用户已被注册");
        } else {
            BlogUser inserUserInfo = new BlogUser();
            inserUserInfo.setUserIntro("无");
            inserUserInfo.setUserName(DigestUtils.md5DigestAsHex(new String(login.getPhone() + login.getPassword()).getBytes()).substring(0, 12));
            inserUserInfo.setUserPass(login.getPassword());
            inserUserInfo.setUserPhone(login.getPhone());

            int insert = blogUserMapper.insertUserInfo(inserUserInfo);

            BlogFavorite blogFavorite = new BlogFavorite();
            blogFavorite.setFavoriteName("默认收藏夹");
            blogFavorite.setArticleId(0L);
            blogFavorite.setUserId(inserUserInfo.getId());
            iBlogFavoriteService.save(blogFavorite);
            if (insert > 0) {
                r.success("注册成功");
            }
        }
        return r;
    }

    @Override
    public R<BlogUserCover> informationView(Long userId) {
        BlogUserCover blogUserCover = blogUserMapper.informationView(userId);
        R<BlogUserCover> r = new R<BlogUserCover>();
        r.success(blogUserCover);
        return r;
    }

    @Override
    public R uploadImg(List<MultipartFile> file, Integer userId) throws IOException {
        Iterator<MultipartFile> iterator = file.iterator();
        R stringR = new R();
        ArrayList<String> strings = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(file.size());
        while (iterator.hasNext()) {
            MultipartFile next = iterator.next();
            InputStream inputStream = next.getInputStream();
            String originalFilename = next.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.indexOf("."));

            String filename = String.valueOf(UUID.randomUUID());
            String uploadFileUrl = new StringBuffer(filename).append(ext).toString();
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    uploadFileUrl,
                    inputStream);
            ProgressListener progressListener = new ProgressListener() {
                // 处理上传进度事件
                @Override
                public void progressChanged(ProgressEvent progressEvent) {
                    if (progressEvent.getEventType() == TRANSFER_COMPLETED_EVENT) {
                        // 判断上传结果状态码，200表示上传成功
                        countDownLatch.countDown();
                        strings.add(OssConfig.bucket + uploadFileUrl);
                        BlogUser blogUser = new BlogUser();
                        blogUser.setUserImage(OssConfig.bucket + uploadFileUrl);
                        LambdaUpdateWrapper<BlogUser> blogUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        blogUserLambdaUpdateWrapper.eq(BlogUser::getId, userId)
                                .set(BlogUser::getUserImage, OssConfig.bucket + uploadFileUrl);
                        iBlogUserService.update(blogUserLambdaUpdateWrapper);
                    }
                }
            };
            putObjectRequest.setProgressListener(progressListener);
            // 上传文件
            ossClient.putObject(putObjectRequest);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!ObjectUtils.isEmpty(strings)) {
            stringR.success(strings);
        } else {
            stringR.error("上传失败");
        }
        return stringR;
    }

    @Override
    public R<String> update(BlogUser blogUser) {
        int i = this.baseMapper.updateById(blogUser);
        R<String> stringR = new R<>();
        if (i > 0) {
            stringR.success("修改成功");
        } else {
            stringR.error("修改失败");
        }
        return stringR;
    }


    /*
     * 消息中心侧边栏的红点
     * */
    @Override
    public R<HashMap<String, Boolean>> messageAsStatus(Long userId) {
        HashMap<String, Boolean> hashMap = new HashMap<>();

        LambdaQueryWrapper<BlogAttention> attention = new LambdaQueryWrapper<BlogAttention>()
                .eq(BlogAttention::getStated, 0)
                .eq(BlogAttention::getActive, 0)
                .eq(BlogAttention::getUserId, userId);
        hashMap.put("attention", !ObjectUtils.isEmpty(iBlogAttentionService.getBaseMapper().selectList(attention)));

//        LambdaQueryWrapper<BlogChattingRecords> chat = new LambdaQueryWrapper<BlogChattingRecords>()
//                .eq(BlogChattingRecords::getReadeStatus, 0)
//                .eq(BlogChattingRecords::getActive, 0)
//                .eq(BlogChattingRecords::getAcceptorUserId, userId);
        R<List<EventPrivateLetter>> listR = iBlogChattingRecordsService.chatListAsActive(userId);
        boolean flag = false;
        for (EventPrivateLetter datum : listR.getData()) {
            if (userId.equals(datum.getAcceptorUser().getId())) {
                flag = flag || (datum.getReadeStatus() == 0 ? true : false);
            }
        }
//        hashMap.put("privateletter", !ObjectUtils.isEmpty(iBlogChattingRecordsService.getBaseMapper().selectList(chat)));
        hashMap.put("privateletter", flag);

//        LambdaQueryWrapper<BlogArticle> eq = new LambdaQueryWrapper<BlogArticle>()
//                .select(BlogArticle::getId)
//                .eq(BlogArticle::getUserId, userId);
//        List<Object> objects = iBlogArticleService.listObjs(eq);
        LambdaQueryWrapper<BlogComment> comment = new LambdaQueryWrapper<BlogComment>()
                .eq(BlogComment::getStated, 0)
                .eq(BlogComment::getActive, 0)
//                .in(BlogComment::getArticleId, objects)
                .eq(BlogComment::getPersonId, userId);
        hashMap.put("comment", !ObjectUtils.isEmpty(iBlogCommentService.getBaseMapper().selectList(comment)));

        LambdaQueryWrapper<BlogLike> like = new LambdaQueryWrapper<BlogLike>()
                .eq(BlogLike::getStated, 0)
                .eq(BlogLike::getActive, 0)
                .eq(BlogLike::getLikeBrowseLimiter, 0)
                .eq(BlogLike::getPersonId, userId)
                .not((re) -> {
                    re.eq(BlogLike::getUserId, userId);
                });
        hashMap.put("like", !ObjectUtils.isEmpty(iBlogLikeService.getBaseMapper().selectList(like)));

        LambdaQueryWrapper<BlogArticlePush> articlePush = new LambdaQueryWrapper<BlogArticlePush>()
                .eq(BlogArticlePush::getReadeStatus, 0)
                .eq(BlogArticlePush::getAcceptUserId, userId);
        hashMap.put("articlepush", !ObjectUtils.isEmpty(iBlogArticlePushService.getBaseMapper().selectList(articlePush)));

        R<HashMap<String, Boolean>> hashMapR = new R<>();
        hashMapR.success(hashMap);
        return hashMapR;
    }


}
