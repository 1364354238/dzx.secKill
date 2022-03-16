# SpringMVC优化

> 实现WebMvcConfigurer接口，提前处理request


```Java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UserArgumentResolver argumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(argumentResolver);
    }
}
```


## ArgumentResolvers

> 自定义方法参数处理器


```Java
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?>clazz=methodParameter.getParameterType();
        return clazz == User.class; //true会执行resolveArgument方法
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request=nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        if(StringUtils.isEmpty(ticket)){
            return null;
        }
        return userService.getUserByCookie(ticket,request, response );
    }
}
```


## 优化后controller

```Java
@RequestMapping("/toList")
public String toList(Model model,User user){
        model.addAttribute("user", user);
        return "goodsList";
}
```




