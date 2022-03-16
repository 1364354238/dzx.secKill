package com.dzx.seckill.persistence.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author dzx
 * @since 2022-03-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user")
@ApiModel(value = "User对象", description = "")
public class User extends Model<User> {

    @ApiModelProperty("用户id，手机号码")
    @TableId("id")
    private Long id;

    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("MD5(MD5(pass明文+固定salt)+salt")
    @TableField("password")
    private String password;

    @ApiModelProperty("头像")
    @TableField("head")
    private String head;

    @ApiModelProperty("注册时间")
    @TableField("register_date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime registerDate;

    @ApiModelProperty("最后一次登录时间")
    @TableField("last_login_date")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastLoginDate;

    @ApiModelProperty("登录次数")
    @TableField("login_count")
    private Integer loginCount;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
