package com.cn.linka.business.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailException;
import com.cn.linka.business.bean.UserOrderBean;
import com.cn.linka.business.dao.*;
import com.cn.linka.business.mapper.UserMapper;
import com.cn.linka.business.mapper.UserOrderMapper;
import com.cn.linka.business.service.UserPortalService;
import com.cn.linka.business.service.UserService;
import com.cn.linka.common.config.SnowFlake;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import com.cn.linka.common.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Value("${spring.mail.username}")
    private String from;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserOrderMapper userOrderMapper;
    @Resource
    private UserPortalService userPortalService;

    @Override
    public List<User> queryUserList() {
        return userMapper.queryUserList();
    }

    @Override
    public BaseDaoForHttp email(String to) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件发送者
        message.setFrom(from);
        // 设置邮件接收者
        message.setTo(to);
        // 设置邮件的主题
        message.setSubject("注册验证码");
        // 设置邮件的正文
        Integer code = RandomUtil.randomInt(100000, 999999);
        String text = "LINKCN:您的验证码为：" + code + ",请勿泄露给他人。";
        message.setText(text);
        // 发送邮件
        try {
            javaMailSender.send(message);
            //将验证码保存到redis缓存中，设置有效时间为5分钟
            stringRedisTemplate.opsForValue().set(to, String.valueOf(code), 5, TimeUnit.MINUTES);
            return BaseDaoForHttp.success();
        } catch (MailException e) {
            e.printStackTrace();
        }
        return BaseDaoForHttp.fail();
    }

    @Override
    public BaseDaoForHttp<UserRegisteredDao> registered(String email, String verifyCode, String password) {
        log.info("注册流程开始");
        if (userMapper.selectByEmail(email).isPresent()) {
            log.info("该邮箱已经注册");
            throw new BusException(BusinessExceptionEnum.EMAIL_HAS_REGISTERED);
        }
        if (!checkEmailCodeStatus(email, verifyCode)) {
            throw new BusException(BusinessExceptionEnum.EMAIL_VERIFY_CODE_ERROR);
        }
        //生产userId
        String userId = "Link" + SnowFlake.nextIdString();
        userMapper.insert(User.builder()
                .password(password)
                .phone("邮箱注册-无手机号码")
                .userName("Link用户")
                .userId(userId)
                .userImg("")
                .createDt(new Date())
                .email(email)
                .build());
        UserRegisteredDao registeredDao = new UserRegisteredDao();
        registeredDao.setUserId(userId);
        return BaseDaoForHttp.success(registeredDao);
    }

    @Override
    public BaseDaoForHttp checkEmail(String email) {
        log.info("检查邮箱是否注册");
        if (userMapper.selectByEmail(email).isPresent()) {
            log.info("该邮箱已经注册");
            throw new BusException(BusinessExceptionEnum.EMAIL_HAS_REGISTERED);
        }
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp checkEmailVerifyCode(String email, String verifyCode) {
        if (!checkEmailCodeStatus(email, verifyCode)) {
            throw new BusException(BusinessExceptionEnum.EMAIL_VERIFY_CODE_ERROR);
        }
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp<UserLogin> userEmailLogin(String email, String passWord) {
        Optional<User> optionalUser = userMapper.selectByEmail(email);
        if (optionalUser.isPresent()) {
            String token = JwtUtils.getToken(optionalUser.get());
            if (passWord.equals(optionalUser.get().getPassword())) {
                return BaseDaoForHttp.success(User.toUserLogin(optionalUser.get(), token));
            } else {
                throw new BusException(BusinessExceptionEnum.USERNAME_PASSWORD_ERROR);
            }
        } else {
            throw new BusException(BusinessExceptionEnum.USERNAME_PASSWORD_ERROR);
        }
    }

    @Override
    public BaseDaoForHttp userUpdate(UserUpdate user) {
        if (StringUtils.isEmpty(user.getUserId())) {
            throw new BusException(BusinessExceptionEnum.USER_ID_ISNULL);
        }
        if (userMapper.userUpdate(user) < 1) {
            throw new BusException(BusinessExceptionEnum.USER_MSG_UPDATE_FAIL);
        } else {
            return BaseDaoForHttp.success();
        }
    }

    @Override
    public BaseDaoForHttp<User> getUserByUserId(String userId) {
        Optional<User> optionalUser = userMapper.getUserByUserId(userId);
        if (optionalUser.isPresent()) {
            return BaseDaoForHttp.success(optionalUser.get());
        } else {
            throw new BusException(BusinessExceptionEnum.USER_MSG_NOT_EXIST);
        }
    }

    @Override
    public BaseDaoForHttp userUpdatePassword(UserUpdatePasswordDao user) {
        if (!checkEmailCodeStatus(user.getEmail(), user.getVerifyCode())) {
            throw new BusException(BusinessExceptionEnum.EMAIL_VERIFY_CODE_ERROR);
        }
        if (userMapper.userUpdatePassword(user) < 1) {
            throw new BusException(BusinessExceptionEnum.USER_PASSWORD_UPDATE_FAIL);
        } else {
            return BaseDaoForHttp.success();
        }
    }

    @Override
    public BaseDaoForHttp<UserLinkBase> userDetail(String userId) {
        UserLinkBase userLinkBase = new UserLinkBase();
        Optional<User> optionalUser = userMapper.getUserByUserId(userId);
        if (!optionalUser.isPresent()) {
            throw new BusException(BusinessExceptionEnum.USER_MSG_NOT_EXIST);
        }
        userLinkBase.setEmail(optionalUser.get().getEmail());
        userLinkBase.setPhone(optionalUser.get().getPhone());
        userLinkBase.setUserId(userId);
        userLinkBase.setUserName(optionalUser.get().getUserName());
        userLinkBase.setUserStatus(optionalUser.get().getUserStatus());
        userLinkBase.setUserImg(optionalUser.get().getUserImg());
        List<UserOrderBean> effectOrders = userOrderMapper.getEffectOrder(userId);
        if (effectOrders.size() > 0) {
            userLinkBase.setMemberUntilDate(effectOrders.get(0).getEndDt());
        }
        UserPortalDao body = userPortalService.getPortalByUserId(userId).getBody();
        userLinkBase.setUserPortalDao(body);
        return BaseDaoForHttp.success(userLinkBase);
    }

    @Override
    public BaseDaoForHttp<UserLogin> userWxLogin(String openId, String wxNickName, String headUrl) {
        Optional<User> optionalUser = userMapper.selectByOpenId(openId);
        if (optionalUser.isPresent()) {
            String token = JwtUtils.getToken(optionalUser.get());
            return BaseDaoForHttp.success(User.toUserLogin(optionalUser.get(), token));
        } else {
            //生产userId
            String userId = "Link" + SnowFlake.nextIdString();
            User build = User.builder()
                    .phone("无手机号码")
                    .userName(wxNickName)
                    .userId(userId)
                    .openId(openId)
                    .userImg(headUrl)
                    .createDt(new Date())
                    .build();
            userMapper.insert(build);
        }
        Optional<User> optionalUserNew = userMapper.selectByOpenId(openId);
        String token = JwtUtils.getToken(optionalUserNew.get());
        return BaseDaoForHttp.success(User.toUserLogin(optionalUserNew.get(), token));
    }

    /**
     * 校验验证码的有效期
     *
     * @param email
     * @param verifyCode
     * @return
     */
    public boolean checkEmailCodeStatus(String email, String verifyCode) {
        return (verifyCode).equals(stringRedisTemplate.opsForValue().get(email)) ? true : false;
    }
}
