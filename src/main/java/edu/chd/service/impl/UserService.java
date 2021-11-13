package edu.chd.service.impl;

import edu.chd.common.Const;
import edu.chd.common.ResponseCode;
import edu.chd.dao.UserInfoMapper;
import edu.chd.pojo.UserInfo;
import edu.chd.service.IUserService;
import edu.chd.utils.DateUtil;
import edu.chd.utils.MD5Utils;
import edu.chd.utils.ServerResponse;
import edu.chd.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
@Service
public class UserService implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;


    public UserVO covert(UserInfo userInfo){
        UserVO userVO= new UserVO();

        userVO.setId(userInfo.getId());
        userVO.setUsername(userInfo.getUsername());
        userVO.setEmail(userInfo.getEmail());
        userVO.setPhone(userInfo.getPhone());

        userVO.setCreateTime(DateUtil.Date2String(userInfo.getCreateTime()));
        userVO.setUpdateTime(DateUtil.Date2String(userInfo.getUpdateTime()));

        return userVO;
    }

    @Override
    public ServerResponse loginLogic(String username, String password) {

        //step1:用户名和密码的非空判断
        if(StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode()
                    , ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode()
                    , ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }
        //step2:用户名是否存在
        Integer count = userInfoMapper.findByUsername(username);
        if(count == 0){
            //用户名不存在
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EXISTS.getCode(),
                    ResponseCode.USERNAME_NOT_EXISTS.getMsg());
        }
        //step3:根据用户名和密码查询
        password = MD5Utils.getMD5Code(password);
        UserInfo userInfo = userInfoMapper.findByUsernameAndPassword(username, password);
        if (userInfo == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_ERROR.getCode(),
                    ResponseCode.PASSWORD_ERROR.getMsg());
        }
        //step4:返回结果


        return ServerResponse.createServerResponseBySuccess(covert(userInfo));
    }



    @Override
    public ServerResponse registerLogic(UserInfo userInfo) {
        //step1:参数非空判断
        if(userInfo == null){
            return ServerResponse.createServerResponseByFail(ResponseCode.PARAMETER_NOT_EMPTY.getCode()
                    , ResponseCode.PARAMETER_NOT_EMPTY.getMsg());
        }

        String username = userInfo.getUsername();
        String password = userInfo.getPassword();
        String email = userInfo.getEmail();
        String phone = userInfo.getPhone();
        String question = userInfo.getQuestion();
        String answer = userInfo.getAnswer();

        //step2:非空判断
        if(username==null || username.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_NOT_EMPTY.getCode()
                    , ResponseCode.USERNAME_NOT_EMPTY.getMsg());
        }
        if(password==null || password.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.PASSWORD_NOT_EMPTY.getCode()
                    , ResponseCode.PASSWORD_NOT_EMPTY.getMsg());
        }
        if(email==null || email.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_NOT_EMPTY.getCode()
                    , ResponseCode.EMAIL_NOT_EMPTY.getMsg());
        }
        if(phone==null || phone.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.PHONE_NOT_EMPTY.getCode()
                    , ResponseCode.PHONE_NOT_EMPTY.getMsg());
        }
        if(question==null || question.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.QUESTION_NOT_EMPTY.getCode()
                    , ResponseCode.QUESTION_NOT_EMPTY.getMsg());
        }
        if(answer==null || answer.equals("")){
            return ServerResponse.createServerResponseByFail(ResponseCode.ANSWER_NOT_EMPTY.getCode()
                    , ResponseCode.ANSWER_NOT_EMPTY.getMsg());
        }


        //step2:用户名是否存在
        Integer count = userInfoMapper.findByUsername(userInfo.getUsername());
        if(count > 0){
            //用户名已存在
            return ServerResponse.createServerResponseByFail(ResponseCode.USERNAME_EXISTS.getCode(),
                    ResponseCode.USERNAME_EXISTS.getMsg());
        }

        //step3:邮箱是否注册过
        count = userInfoMapper.findByEmail(userInfo.getUsername());
        if(count > 0){
            //邮箱已存在
            return ServerResponse.createServerResponseByFail(ResponseCode.EMAIL_EXISTS.getCode(),
                    ResponseCode.EMAIL_EXISTS.getMsg());
        }

        //step4:密码加密
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));

        //step5:注册
        userInfo.setRole(Const.NORMAL_USER);
        int result = userInfoMapper.insert(userInfo);
        if(result == 0){
            //注册失败
            return ServerResponse.createServerResponseByFail(ResponseCode.REGISTER_FAIL.getCode(),
                    ResponseCode.REGISTER_FAIL.getMsg());
        }

        //step6:返回结果
        return ServerResponse.createServerResponseBySuccess();
    }
}
