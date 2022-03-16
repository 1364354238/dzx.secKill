# 分布式session问题

> 之前的代码在我们之后一台应用系统，所有操作都在一台Tomcat上，没有什么问题。当我们部署多台系统，配合Nginx的时候会出现用户登录的问题


> 由于 Nginx 使用默认负载均衡策略（轮询），请求将会按照时间顺序逐一分发到后端应用上。
也就是说刚开始我们在 Tomcat1 登录之后，用户信息放在 Tomcat1 的 Session 里。过了一会，请求
又被 Nginx 分发到了 Tomcat2 上，这时 Tomcat2 上 Session 里还没有用户信息，于是又要登录。


## 解决方案

### Session复制

- 优点：无需修改代码，只需要修改Tomcat配置
- 缺点：Session同步传输占用内网带宽；多台Tomcat同步性能指数级下降；Session占用内存，无法有效水平扩展

### 前端存储

- 优点：不占用服务端内存
- 缺点：存在安全风险；数据大小受cookie限制；占用外网带宽

### Session粘滞

后端集中存储

- 优点：安全，容易水平扩展
- 缺点：增加复杂度，需要修改代码

## 实现

### spring-session-data-redis

> 配置好以后，session就会存到Redis中，直接从Redis中拿session


#### pom

```XML
  <!-- spring data redis 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- commons-pool2 对象池依赖 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
        <!-- spring-session 依赖 ，session存入Redis中-->
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency> 
```


#### yml

```YAML
  redis:
    timeout: 10000 #连接超时时间
    host: 127.0.0.1 #服务器地址
    port: 6379 #端口号
    database: 0 #数据库
    password: ****x
    lettuce:
      pool:
        max-active: 1024 #最大连接数，默认8
        max-wait: 10000ms #最大连接阻塞等待时间，默认-1
        max-idle: 100 #最大空闲连接，默认8
        min-idle: 5 #最小空闲连接，默认0
```


### 直接存用户

> 将用户信息存到Redis中


```XML
  <!-- spring data redis 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- commons-pool2 对象池依赖 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>

```


#### config

```Java
@Configuration
public class RedisConfig {

    @Resource
    RedisConnectionFactory connectionFactory;
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//key序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//value序列器
        redisTemplate.setValueSerializer(new
                GenericJackson2JsonRedisSerializer());
//Hash类型 key序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//Hash类型 value序列器
        redisTemplate.setHashValueSerializer(new
                GenericJackson2JsonRedisSerializer());
        System.out.println("connection"+connectionFactory);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
```


#### service

```Java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 登录功能
     * @return
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String passwd = loginVo.getPassword();
        String mobile = loginVo.getMobile();
//        //参数校验，修改后采用注解的形式
//        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(passwd)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isPhoneLegal(mobile)){
//            return RespBean.error(RespBeanEnum.MOBILE_ERRO);
//        }
        User user = userMapper.selectById(mobile);
        if(null==user){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!MD5Util.inputPassToDBPass(passwd,mobile).equals(user.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
//        生成cookie
        String ticket = UUIDUtil.uuid();
//      将用户信息存入到Redis中
        redisTemplate.opsForValue().set("user:" + ticket, JsonUtil.objectToString(user));
//        request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(RespBeanEnum.Success);
    }

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(null==userTicket){
            return null;
        }
        String userJson= (String) redisTemplate.opsForValue().get("user:" + userTicket);
        if(null==userJson){
            return null;
        }
        User user = JsonUtil.jsonToObject(userJson, User.class);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }
}
```




