package com.nageoffer.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.nageoffer.shortlink.admin.common.database.BaseDO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户持久化实体，对应数据库表 {@code t_user}。
 */
@Data
@TableName("t_user")
public class UserDO extends BaseDO implements Serializable  {

    @Serial
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String mail;

    /** 注销时间戳 */
    private Long deletionTime;


}
