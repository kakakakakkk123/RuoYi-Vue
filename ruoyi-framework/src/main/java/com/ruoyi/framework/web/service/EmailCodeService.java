package com.ruoyi.framework.web.service;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.redis.RedisCache;

/**
 * 邮箱验证码服务
 */
@Service
public class EmailCodeService
{
    private static final int EXPIRE_MINUTES = 10;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private RedisCache redisCache;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    public String sendRegisterCode(String email)
    {
        String targetEmail = normalizeEmail(email);
        if (StringUtils.isEmpty(targetEmail))
        {
            throw new ServiceException("邮箱不能为空");
        }
        if (mailSender == null || StringUtils.isEmpty(fromAddress))
        {
            throw new ServiceException("邮件服务未配置，请先补充 spring.mail 配置");
        }

        String code = generateCode();
        String key = codeKey(targetEmail);
        redisCache.setCacheObject(key, code, EXPIRE_MINUTES, TimeUnit.MINUTES);

        try
        {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(targetEmail);
            message.setSubject("学生账号注册验证码");
            message.setText("你的注册验证码是：" + code + "，有效期 " + EXPIRE_MINUTES + " 分钟。");
            mailSender.send(message);
        }
        catch (MailException ex)
        {
            throw new ServiceException("验证码发送失败：" + ex.getMessage());
        }
        return code;
    }

    public void validateRegisterCode(String email, String code)
    {
        String targetEmail = normalizeEmail(email);
        String targetCode = StringUtils.trim(code);
        if (StringUtils.isEmpty(targetEmail))
        {
            return;
        }
        if (StringUtils.isEmpty(targetCode))
        {
            throw new ServiceException("请输入邮箱验证码");
        }
        String cacheCode = redisCache.getCacheObject(codeKey(targetEmail));
        if (StringUtils.isEmpty(cacheCode))
        {
            throw new ServiceException("邮箱验证码已过期，请重新获取");
        }
        if (!StringUtils.equalsIgnoreCase(cacheCode, targetCode))
        {
            throw new ServiceException("邮箱验证码错误");
        }
        redisCache.deleteObject(codeKey(targetEmail));
    }

    private String codeKey(String email)
    {
        return CacheConstants.EMAIL_CODE_KEY + normalizeEmail(email);
    }

    private String normalizeEmail(String email)
    {
        return StringUtils.trim(email).toLowerCase(Locale.ROOT);
    }

    private String generateCode()
    {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return Integer.toString(code);
    }
}
