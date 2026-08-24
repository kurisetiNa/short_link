package com.nageoffer.shortlink.admin.common.biz.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.nageoffer.shortlink.admin.common.convention.exception.ClientException;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.StringRedisTemplate;


import java.io.IOException;

import java.util.List;
import java.util.Objects;

import static com.nageoffer.shortlink.admin.common.serialize.enums.UserErrorCodeEnum.USER_TOKEN_FAIL;

/**
 * 用户信息传输过滤器
 *
 */

@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;
    private static final List<String> IGNORE_URI= Lists.newArrayList(
            "/api/shortlink/admin/v1/user/login",
            "/api/shortlink/admin/v1/user/has-username",
            "/api/shortlink/v1/admin/title"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI=httpServletRequest.getRequestURI();
        if(!IGNORE_URI.contains(requestURI)){
            String method=httpServletRequest.getMethod();
            if(!(Objects.equals(requestURI,"/api/shortlink/admin/v1/user")&&Objects.equals(method,"POST"))){
                String username = httpServletRequest.getHeader("username");
                String token = httpServletRequest.getHeader("token");
                if(!StrUtil.isAllNotBlank(username,token)){
                    returnJson((HttpServletResponse) servletResponse,
                            JSON.toJSONString(Results.failure(new ClientException(USER_TOKEN_FAIL))));
                    return;
                }
                Object userInfoJsonStr ;
                try {
                    userInfoJsonStr= stringRedisTemplate.opsForHash().get("login_" + username, token);
                    if(userInfoJsonStr==null){
                        throw new ClientException(USER_TOKEN_FAIL);
                    }
                    UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonStr.toString(), UserInfoDTO.class);
                    UserContext.setUser(userInfoDTO);
                }catch (Exception ex){
                    returnJson((HttpServletResponse) servletResponse,
                            JSON.toJSONString(Results.failure(new ClientException(USER_TOKEN_FAIL))));
                    return;
                }

            }
        }


        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }

    /**
     * 将统一响应结果以 JSON 格式写回客户端。
     */
    private void returnJson(HttpServletResponse response, String json) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
