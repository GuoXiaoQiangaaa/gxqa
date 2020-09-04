package com.pwc.modules.sys.controller.form;

import lombok.Data;

import java.util.List;

@Data
public class SetUserPermsForm {


    /**
     * 用户ID
     */
    private Long userId;

    List<UserPermsForm> userPerms;

}
