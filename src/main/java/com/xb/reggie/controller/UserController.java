package com.xb.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xb.reggie.common.R;
import com.xb.reggie.entity.User;
import com.xb.reggie.service.UserService;
import com.xb.reggie.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone =user.getPhone();
        if(StringUtils.isNotBlank(phone)){
            String s = ValidateCodeUtils.generateValidateCode(4).toString();
//            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,s);
            log.info(s);
            session.setAttribute(phone,s);
            return R.success("手机验证码发送成功");
        }
        return R.error("发送失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
         String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object attribute = session.getAttribute(phone);
        if(attribute!=null&&attribute.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");


    }
}
