package com.ssk.bidwise.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象 VO
 */
@Data
public class UserVO {

    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private LocalDateTime createTime;
}
