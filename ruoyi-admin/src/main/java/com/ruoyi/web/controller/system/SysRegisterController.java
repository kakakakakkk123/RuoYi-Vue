package com.ruoyi.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.EmailCodeBody;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.EmailCodeService;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.IStudentAccountService;

/**
 * 注册验证
 */
@Anonymous
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private IStudentAccountService studentAccountService;

    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private EmailCodeService emailCodeService;

    @PostMapping("/registerEmailCode")
    public AjaxResult registerEmailCode(@RequestBody EmailCodeBody body)
    {
        emailCodeService.sendRegisterCode(body.getEmail());
        return success("邮箱验证码已发送");
    }

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (configService.selectCaptchaEnabled())
        {
            registerService.validateCaptcha(user.getUsername(), user.getCode(), user.getUuid());
        }
        if (StringUtils.isNotEmpty(user.getEmail()))
        {
            emailCodeService.validateRegisterCode(user.getEmail(), user.getEmailCode());
        }
        String msg = studentAccountService.register(user);
        return StringUtils.isEmpty(msg) ? success("注册成功，请使用新账号登录") : error(msg);
    }
}
