# 启动

## 一、数据库及其他环境

1. 新建wschat数据库，找到项目根目录sql包下的sql文件，导入到数据库中

   ![image-20231209103351786](https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312091058024.png)

2. 修改项目根目录下的application.yml文件中的数据库配置和邮箱配置

   - application.yml指定使用哪个配置文件

     ![image-20231209103416249](https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312091058027.png)

     ![image-20231209103433227](https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312091058028.png)

   - application-dev.yml指定开发环境的配置文件

     ![image-20231209103544829](https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312091058029.png)

     修改为自己的邮箱服务，用于发送邮箱信息，可自行搜索如何获取

     ![image-20231209103640685](https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312091058030.png)

     修改账号密码

   - application-prod.yml指定生产环境的配置文件

## 二、启动项目

### 1、后端项目

1. 找到项目根目录下的pom.xml文件，右键maven->reimport
2. 找到项目根目录下的src/main/java/com/xxx/xxx/xxxApplication.java文件，右键run
3. 启动成功

### 2、前端项目

1. 找到项目根目录下的wschatfront文件夹，右键open in terminal

2. npm install

3. 修改ip地址

   ![image-20231209105558154](https://xiaolin-zi.oss-cn-guangzhou.aliyuncs.com/typora-img/202312091058031.png)

4. npm run serve
