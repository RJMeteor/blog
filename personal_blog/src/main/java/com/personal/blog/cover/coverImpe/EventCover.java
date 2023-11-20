package com.personal.blog.cover.coverImpe;

import com.personal.blog.cover.*;
import com.personal.blog.pojo.*;
import com.personal.blog.utils.validators.annotation.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel ="spring")
public interface EventCover {

    @Mapping(target = "privateleId",source = "id")
    @Mapping(target = "sendUser",source = "senderUser")
    @Mapping(target = "comment",source = "chattingRecordsContentMapping.chattingRecordsContent")
    EventPrivateLetter blogChattingRecordsToEventPrivateLetter(BlogChattingRecords records);
    List<EventPrivateLetter> blogChattingRecordsListToEventPrivateLetterList(List<BlogChattingRecords> records);

    @Mapping(target = "likeId",source = "id")
    @Mapping(target = "sendUser",source = "user")
    EventLike blogLikeToEventLike(BlogLike like);
    List<EventLike> blogLikeListToEventLikeList(List<BlogLike> like);


    @Mapping(target = "commentId",source = "id")
    @Mapping(target = "sendUser",source = "user")
    @Mapping(target = "comment",source = "commentContentMapping.commentContent")
    EventComment blogCommentToEventComment(BlogComment comment);
    List<EventComment> blogCommentListToEventCommentList(List<BlogComment> comment);


    @Mapping(target = "attentionId",source = "id")
    EventAttention blogAttentionToEventAttention(BlogAttention attention);
    List<EventAttention> blogAttentionListToEventAttentionList(List<BlogAttention> attention);


    @Mapping(target = "articlepushId",source = "id")
    @Mapping(target = "comment",source = "article.blogArticleContentMapping.articleContentNotHtml")
    EventArticlepush blogArticlePushToEventArticlepush(BlogArticlePush articlePush);
    List<EventArticlepush> blogArticlePushListToEventArticlepushList(List<BlogArticlePush> articlePush);



}
