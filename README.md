<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: Li Jianxiong
 * @Date: 2020-06-11 10:20:20
 * @LastEditors: Li Jianxiong
 * @LastEditTime: 2020-07-11 14:56:27
--> 
# 说明

## 写在前面的话
这个是目前正在使用的老版本，新版本在master分支上，还没写完，所以可以当它不存在，想优化接着写也可以。   
这套代码是大三写的，当时刚学后端开发，spring框架编写是点一点摸索出来的，很多东西都不符合规范，比如REST风格等，也没有太考虑性能问题，也没有加日志功能，因此不要太局限于我的代码，可以大胆的改。

## 一、总体架构

后端主要分为三个部分，javaweb服务、docker后台测评，还有数据库。

### 1.1 javaweb服务
javaweb主要采用springboot框架，响应前端请求，修改数据库，并且调用docker进行评测。  
springboot框架网上有很多教程，小学期应该学过spring和java知识，上手应该不难。需要什么需求网上都能查到。  
因为后台学生代码采用gitlab进行管理，所以调用了gitlab api接口。网上也有教程。

### 1.2 docker后台评测
这个后台评测封装为了一个docker，这个和CG系统的docker基本相同。通过向docker输入学生id、学生要评测的题目，就可以返回学生分数、评测波形等。

### 1.3 数据库
采用mysql框架，数据表下面会说
后续需要完成教师端功能，数据库需要大改。

## 二、文件说明
### 2.1 aspect
- ControllerRequestAdvice 网上查的，加上这个可以打印请求以及param，并且打印相应，便于debug。

### 2.2 Config
配置信息文件
- ExceptionController 加上他，可以捕获全局异常。
- MysqlConfig 没有他的话，后台数据库自动建库中文会乱码
- MyConfiguration 开启全局跨域

### 2.3 Controller
- ErrorController 全局异常路由，如果出现异常，则执行这个代码。
- LoginController 登录
- StudentController 学生服务，包括获取题目、进行评测等。
- TeacherController 老师服务，目前主要是出题。查看学生题目、评分等功能还没写。

### 2.4 Model
实体类
#### 2.4.1 DataModel
- assmeble_choose类，这里面我把选项和答案用字符串存的，用三个井号隔开各个选项和答案，用的时候再拆开。答案用的是0123之类的数字存的。
- user里面有用户名和id，系统里全部用的是用户名进行传输，因为国家平台也是用的用户名，正好与之对应。
- Task 题目类，这里我偷懒了，把verilog题目和汇编题目用的一个类放的，理应是用三个实体类，一个题目类，一个汇编题目类，一个verilog题目类，然后用外键关联，后面可以对这个进行调整。
#### 2.4.2 GitModel
普通的实体类
- GitFile 前端IDE需要的文件格式，里面的变量都是和ide协商好的，最好别改。（具体注释里有）
- GitFolder 没具体用处。（前端把文件夹功能删了，这个也就没用了）
- Gitproject 题目类，一道题目含有多个文件GitFile和其他信息。
- QuestionAndTask 题目和作业对应关系类。学生获取题目返回的对象，里面有个taskscore列表，存有学生这次作业下所有题目的内容和分数
- TaskFile 老师上传的文件转换为的对象
- TaskModel 老师上传的题目转换为的对象。把老师上传的正确工程文件、样例工程文件转换为对象。根据taskmodel来生成gitlab工程
- TaskSore 题目内容及其分数，学生获取题目列表时包含的对象。
- 
### 2.4.3 UtilModel
- JudgeResult 评测结果文件。和JudgeModel一样，但是现在好像用的是和JudgeModel一样。
- Result 标准返回格式，包含是否成功，返回信息等。
- ConfigJson 老师上传的config.json对应的对象
- ChooseModel 学生获取选择题时返回的对象。包括tcid，分数，题目描述，和选项列表

### 2.3 Repository
与数据库对应的持久层

### 2.4 Serviece
与数据库对应的服务层，里面都是各种数据库增删改查操作。

### 2.5 Shiro
登录保护，直接用的框架。采用国家平台使用的jwt加密方式进行。在shiroconfig中设置拦截对象。

### 2.6 Util
工具类
- Base64Convert base64加密解密类
- FileUtil 老师上传作业题目的文件服务类。
- GitProcess gitlabapi封装，提供gitlab获取工程、文件删改的工具类
- OSUtile 获取当前服务器操作系统
- ResultUtil 返回格式类。
- PathUtil 服务器内路径转换为 url路径
- ReadRoutine 运行命令并将运行结果转为map的工具类
- HttpClient http请求工具类，主要是与国家平台对接要用，需要向国家平台进行get请求。
- DateUtil string字符串转date日期工具类，前端要将日期以字符串传过来，所以用这个类进行转换。

## 三、调试方式
- mysql 主要通过navicat或者mysqlworkbench
- java 通过postman模拟发送请求
- gitlab 老师上传的题目，id保存在数据库中，题目内容比如描述和上传的文件都保存在了gitlab里。gitlab访问

## 四、坑
- gitlab 中 group必须包含字母，否则调用相关函数会报找不到group。因此和gitlab有关的，代码里都用的task_id，是字符串类型。否则用的tid，long类型。gitprocess中有个静态方法，就是task_id转tid的。
- gitlab中，删除一个project后，必须sleep 100ms左右的时间，不然gitlab缓存未清除干净，也会报错。
- 进行请求时，param长度不能过长，否则会被截断，比如token如果放在param中，就会被截断，最好放body里
- 从header中获取token时，前面会带一个“bearer”，字符串，因此要对token用空格进行分割，去后半部分，后面是token。
- 在返回响应时，我之前用的都是新建一个工具类进行，但是如果每一个响应都建一个类，会很多，而且不易调整，因此后面我都是用map存，要啥就放进map里，map再返回时会自动变为json对象，用起来很方便。
- 用gitlab时，最好不要直接用远程的，最好自己部署一个，或者找一个不用的服务器用，因为直接用系统的gitlab调试，会相互冲突。

## 五、文件储存方式
- 所有的文件都在 /home/ustbDemo文件夹下
- static是静态资源文件目录，由tomcat-8.0进行静态资源映射，可以在浏览器直接访问到
- initial中是所有初始化文件放置的位置，比如默认仿真图片等等，初始化时全部拷贝到static文件夹下。
- 所有的图片都在static下
- 老师上传的题目文件在taskfile下，每个题目都对应着其id命名的文件夹，可直接通过路径获取。


# TODO

## BUG

- 在某种情况下可能导致学生的某题目成绩信息被保存多条在数据库中，此后如果在查询成绩时可能导致同一道题查询出多个结果从而报错。
    > 解决方案
    > 1. 将查询逻辑进行更改，改为查询多条，取最大值
    > 2. 找出导致保存多次的原因，代码中已经进行过相关逻辑的判断，可能的原因为多线程之间同时存储数据，导致判断未生效，产生多条数据后导致恶性循环
- 待改进bug：若docker返回信息中有双引号，引号之类，在json格式解析时会报？？？？  不一定

## Disadvantage

- 数据库结构：
    - 密码用明文存储，
	- 两种题目用同一张表存储，对verilog题而言有太多数据项浪费（为null）
	- 对verilog题目而言，题目的图片链接以互联网访问的链接的形式存入数据库，若系统迁移，则该项数据需要手动更改
- 系统的自身架构：
	- 多个地方的配置结构都是写死在程序中（例如gitlab的ip和密钥配置、数据库ip、图片的静态加载链接中的ip设计等）
	- 出题时每个题目会保存一份题目有关的文件在本地，gitlab上也会上传一份，但是在后续很多操作中都是仅仅在本地的文件上操作，可以考虑把gitlab上的项目更好的运用起来，
	- gitlab上的项目仅仅是为了评测使用，而如果直接通过gitlab修改题目，那么和本地的题目文件中的内容不会同步
	- 当前系统的文件操作复杂，可以考虑简化并提高代码的重用性
	- 当前系统的权限过滤只考虑了用户的权限过滤，并没有区分权限的等级（例如学生老师或者管理员）
	- 当前系统的接口有些复杂和冗余，最好是进行简化和复用

## Functions to be added

- 新增系统状态，升级时提示维护界面 
- 做各种成绩调用的接口，以及存放实验报告的接口，以及生成实验报告
- 远程桌面新增：评测不显示对错信息，提示语法错误信息（CG上已完成）