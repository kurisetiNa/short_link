package com.nageoffer.shortlink.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/** 跳转原始域名白名单配置。 */
@Data
@Component
@ConfigurationProperties(prefix = "short-link.goto-domain.white-list")
public class GotoDomainWhiteListConfiguration {

    /** 是否开启白名单校验。 */
    private Boolean enable;

    /** 白名单网站名称，用于错误提示。 */
    private String names;

    /** 允许跳转的域名。 */
    private List<String> details;
}
