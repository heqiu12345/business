package edu.chd.controller;

import edu.chd.common.Const;
import edu.chd.pojo.UserInfo;
import edu.chd.service.IUserService;
import edu.chd.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/user")
public class UserController {


    @Autowired
    IUserService userService;


    @RequestMapping(value = "/login.do")
    public ServerResponse login(String username, String password, HttpSession session){
        ServerResponse serverResponse = userService.loginLogic(username,password);
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, serverResponse.getData());
        }
        return serverResponse;
    }

    @RequestMapping(value = "/register.do")
    public ServerResponse register(UserInfo userInfo, HttpSession session){
        ServerResponse serverResponse = userService.registerLogic(userInfo);
        return serverResponse;
    }

}
