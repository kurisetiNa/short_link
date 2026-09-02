package com.nageoffer.shortlink.gateway.config;

import lombok.Data;

import java.util.List;

/** Token 过滤器配置。 */
@Data
public class Config {
    private List<String> whitePathList;
}
