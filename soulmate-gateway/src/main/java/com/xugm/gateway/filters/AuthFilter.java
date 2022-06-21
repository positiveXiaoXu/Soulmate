package com.xugm.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xugm.commons.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Value("${gateway.excludedUrls}")
    private List<String> excludedUrls; //需要配置不校验的连接

    //过滤器核心业务代码
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1、排除不需要权限检验的连接
        String path = exchange.getRequest().getURI().getPath(); //当前请求连接
        if(excludedUrls.contains(path)) {
            return chain.filter(exchange);
        }
        //2、获取token并校验 (xxxxxx , Bearer xxxxx)
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(!StringUtils.isEmpty(token)) {
           token = token.replaceAll("Bearer ","");
        }
        boolean verifyToken = JwtUtils.verifyToken(token);
        //3、如果检验失败，相应错误状态：401
        if(!verifyToken) {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("errCode", 401);
            responseData.put("errMessage", "用户未登录");
            return responseError(exchange.getResponse(),responseData);
        }
        return chain.filter(exchange);
    }

    //响应错误数据
    private Mono<Void> responseError(ServerHttpResponse response, Map<String, Object> responseData){
        // 将信息转换为 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] data = new byte[0];
        try {
            data = objectMapper.writeValueAsBytes(responseData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 输出错误信息到页面
        DataBuffer buffer = response.bufferFactory().wrap(data);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    //配置执行顺序
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
