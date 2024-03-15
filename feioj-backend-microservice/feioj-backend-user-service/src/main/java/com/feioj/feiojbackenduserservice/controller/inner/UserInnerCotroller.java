package com.feioj.feiojbackenduserservice.controller.inner;

import com.feioj.feiojbackendmodel.model.entity.User;
import com.feioj.feiojbackendserviceclient.service.UserFeignClient;
import com.feioj.feiojbackenduserservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * 该服务仅内部调用，不会提供给前端
 */
@RestController
@RequestMapping("/inner")
public class UserInnerCotroller implements UserFeignClient {
    @Autowired
    private UserService userService;
    @Override
    /**
     * 根据id获取用户信息
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
   public User getById(@RequestParam("userId") long userId){
        return  userService.getById(userId);
    }

    /**
     * 根据ID获取用户列表
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
     public  List<User> listByIds(@RequestParam("idList") Collection<Long> idList){
    return     userService.listByIds(idList);
    }
}
