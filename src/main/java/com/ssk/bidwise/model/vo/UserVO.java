package com.ssk.bidwise.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图 VO
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String avatar;
    private Integer status;
    private LocalDateTime createTime;
}
