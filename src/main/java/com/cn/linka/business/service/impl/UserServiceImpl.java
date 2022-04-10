package com.cn.linka.business.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailException;
import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.User;
import com.cn.linka.business.dao.UserRegisteredDao;
import com.cn.linka.business.mapper.UserMapper;
import com.cn.linka.business.service.UserService;
import com.cn.linka.common.exception.BusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sun.font.TrueTypeFont;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
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
//            stringRedisTemplate.opsForValue().set(to, String.valueOf(code),5, TimeUnit.MINUTES);
            return BaseDaoForHttp.success();
        } catch (MailException e) {
            e.printStackTrace();
        }
        return BaseDaoForHttp.fail();
    }

    @Override
    public synchronized BaseDaoForHttp<UserRegisteredDao> registered(String email, String verifyCode, String password) {
        log.info("注册流程开始");
        //TODO 1校验验证码
        //TODO 生产userId
        if (userMapper.selectByEmail(email)!=null) {
            log.info("该邮箱已经注册");
            return BaseDaoForHttp.fail(7001,"该邮箱已经注册过");
        }
        String userId = String.valueOf(System.currentTimeMillis());
        User user = new User();
        user.setPassword(password);
        user.setCreateDt(new Date());
        user.setUserName("Link-No" + userId);
        user.setPhone("邮箱注册-无手机号码");
        user.setEmail(email);
        user.setUserId(userId);
        userMapper.insert(user);
        UserRegisteredDao registeredDao = new UserRegisteredDao();
        registeredDao.setUserId(userId);
        return BaseDaoForHttp.success(registeredDao);
    }

    @Override
    public BaseDaoForHttp checkEmail(String email) {
        log.info("检查邮箱是否注册");
        if (userMapper.selectByEmail(email).size()>1) {
            log.info("该邮箱已经注册");
            return BaseDaoForHttp.fail(7001,"该邮箱已经注册过");
        }
        return BaseDaoForHttp.success();
    }

}
