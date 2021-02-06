package com.vidor.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class User implements Serializable {

    private String userId;
    private String userName;
    private String userPass;

    private String mobile;

    private List<String> userRoles;
    private List<String> userPerms;

    public User(){

    }
    public User(String userName, String userPass, List<String> userRoles, List<String> userPerms) {
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
        this.userPass = userPass;
        this.userRoles = userRoles;
        this.userPerms = userPerms;
    }

    public User(String userName, String userPass, String mobile, List<String> userRoles, List<String> userPerms) {
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
        this.userPass = userPass;
        this.mobile = mobile;
        this.userRoles = userRoles;
        this.userPerms = userPerms;
    }

//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }
}
