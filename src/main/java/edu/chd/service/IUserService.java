package edu.chd.service;

import edu.chd.pojo.UserInfo;
import edu.chd.utils.ServerResponse;

public interface IUserService {

    /**
     * 登录
     */
    public ServerResponse loginLogic(String username, String password);


    /**
     * 注册
     */
    public ServerResponse registerLogic(UserInfo userInfo);

}
