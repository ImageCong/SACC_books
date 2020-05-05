# SACC_books
SACC-分布式图书推荐系统  
  
## 模块简介  
>SACC_commons  服务提供包，用于解耦  
>SACC_login 登录模块，提供登陆服务  
>SACC_display 展示模块，项目的核心功能，进行数据展示和数据集生成  
>SACC_recommend 推荐模块，采用mahout进行图书推荐  
>SACC_gateway 网关，接受请求  
  
## 数据库文件(mysql 8.0)  
>sacc_book.sql  
>共四张表  
  
## 日志信息  
>log文件夹下存放了各模块日志，命名方式与模块名相同  
  
## 配置信息
> 各模块配置在yml文件中
> zookeeper地址和数据库密码已抹去，使用本机的即可
> 若要进行模块测试可以注释掉@EnableDubbo注解，否则连接远端zk可能耗时过长

