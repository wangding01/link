package com.cn.linka.business.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailException;
import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.User;
import com.cn.linka.business.dao.UserLogin;
import com.cn.linka.business.dao.UserRegisteredDao;
import com.cn.linka.business.mapper.UserMapper;
import com.cn.linka.business.service.UserService;
import com.cn.linka.common.config.SnowFlake;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import com.cn.linka.common.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.relational.core.sql.FalseCondition;
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
        message.setSubject("登录验证码");
        // 设置邮件的正文
        Integer code = RandomUtil.randomInt(100000, 999999);
        String text = "您的验证码为：" + code + ",请勿泄露给他人。";
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
    public synchronized BaseDaoForHttp<UserRegisteredDao> registered(String email, String verifyCode, String password) {
        log.info("注册流程开始");
        if (userMapper.selectByEmail(email).isPresent()) {
            log.info("该邮箱已经注册");
            throw new BusException(BusinessExceptionEnum.EMAIL_HAS_REGISTERED);
        }
        if (!checkEmailCodeStatus(email, verifyCode)) {
            throw new BusException(BusinessExceptionEnum.EMAIL_VERIFY_CODE_ERROR);
        }
        //生产userId
        String userId = SnowFlake.nextIdString();
        userMapper.insert(User.builder()
                .password(password)
                .phone("邮箱注册-无手机号码")
                .userName("Link-No-" + userId)
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
        Optional<User> optionalUser =  userMapper.selectByEmail(email);
        if(optionalUser.isPresent()){
            String token = JwtUtils.getToken(optionalUser.get());
            if(passWord.equals(optionalUser.get().getPassword())){
                return BaseDaoForHttp.success(User.toUserLogin(optionalUser.get(),token));
            }else {
                throw new BusException(BusinessExceptionEnum.USERNAME_PASSWORD_ERROR);
            }
        }else {
            throw new BusException(BusinessExceptionEnum.USERNAME_PASSWORD_ERROR);
        }
    }

    @Override
    public BaseDaoForHttp userUpdate(User user) {
        if(StringUtils.isEmpty(user.getUserId())){
            throw new BusException(BusinessExceptionEnum.USER_ID_ISNULL);
        }
        if(userMapper.userUpdate(user)<1){
            throw new BusException(BusinessExceptionEnum.USER_MSG_UPDATE_FAIL);
        }else {
            return BaseDaoForHttp.success();
        }
    }

    @Override
    public BaseDaoForHttp<User> getUserByUserId(String userId) {
        Optional<User> optionalUser =userMapper.getUserByUserId(userId);
        if(optionalUser.isPresent()){
            return BaseDaoForHttp.success(optionalUser.get());
        }else {
            throw new BusException(BusinessExceptionEnum.USER_MSG_NOT_EXIST);
        }
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
