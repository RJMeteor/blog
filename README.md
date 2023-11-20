## 技术点

- Sping MVC
- SpringBoot
- Mybatis Plus
- WebSocket
- SSE
- JWT
- OSS
- Vue3
- Vite
- md-editor-v3
- sass
- Vue Router
- Naive UI
- TypeScript
- Axios

## 功能组织结构

![image-20231120200210275](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/image-20231120200143334.png)



1. **用户管理功能**

- 用户注册：提供用户注册的功能，包括输入用户名、密码等信息进行注册。

- 用户登录：提供用户登录功能，验证用户名和密码，登录后可访问个人信息和撰写博客。

- 个人信息管理：用户可以编辑个人资料，如头像、昵称、个人简介等。

- 消息中心：浏览和回复系统和用户的消息推送

- 文章收藏：浏览每个收藏夹的文章，对收藏夹的新建、修改、删除和对文章的删除。

- 关注：用户浏览和删除自己关注的用户

-  粉丝：用户浏览和删除粉丝用户

2. **博客管理功能**

- 发布博客：博主可以创建并发布博客文章，包括标题、内容、标签等信息。

-  编辑博客：博主可以编辑已发布的博客文章，修改标题、内容、标签等信息。

- 删除博客：博主可以删除已发布的博客文章，删除后将不再显示在博客列表中。

- 浏览博客：读者浏览博客的内容，请求添加浏览数

- 收藏博客：读者浏览并收藏博客到收藏夹

- 点赞博客：读者浏览并点赞博客，推送点赞消息给作者

3. **评论管理功能**

- 发表评论：读者可以在博客文章下方发表评论，包括评论内容、昵称等信息，推送消息给文章的作者。

-  回复评论：博主和其他读者可以针对某条评论进行回复，建立评论的交流与互动。

4. **用户互动功能**

- 点赞消息推送：获取读者点赞的文章和读者信息。

- 关注消息推送：获取读者关注的信息。

- 私信消息推送：获取读者私信博主的聊天消息推送。

- 评论消息推送：获取读者评论博主文章的消息和读者的信息。

- 文章消息推送：读者关注的博主，及时获取博主发布的新博客文章。

## 数据库逻辑设计

![image-20231120200210275](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/image-20231120200210275.png)

## 功能界面预览

![](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-11-20%20204404.png)

![](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-11-20%20204711.png)

![](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-11-20%20204800.png)

![image-20231120200529741](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/image-20231120200529741.png)

![image-20231120200517987](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/image-20231120200517987.png)

![image-20231120200508515](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/image-20231120200508515.png)

![](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-11-20%20205757.png)

![image-20231120200602981](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/image-20231120200602981.png)

![image-20231120200617947](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/image-20231120200617947.png)

![image-20231120200632944](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/image-20231120200632944.png)



![](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-11-20%20204924.png)

![](https://renjia-oss.oss-cn-chengdu.aliyuncs.com/github/blog/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202023-11-20%20204944.png)