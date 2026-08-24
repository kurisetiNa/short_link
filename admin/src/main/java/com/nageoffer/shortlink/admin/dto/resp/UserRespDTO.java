package com.nageoffer.shortlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nageoffer.shortlink.admin.common.serialize.PhoneDesensitizationSerializer;
import lombok.Data;

@Data
public class UserRespDTO {
    private Long id;

    /** 用户名 */
    private String username;



    /** 真实姓名 */
    private String realName;
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String mail;
}
