package com.nageoffer.shortlink.project.service;


import org.springframework.web.bind.annotation.RequestParam;

/**
 * Url标题接口层
 */
public interface UrlTitleService {

    /**
     * 根据Url获取标题
     * @param url 目标网站地址
     * @return 网站标题
     */
    String getTitleByUrl(String url);
}
