package com.yk.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 12080
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    /**
     * 用户id
     */
    Long id;

    /**
     * 用户昵称
     */
    String nickname;

    /**
     * 用户头像地址
     */
    String avatarUrl;

}
