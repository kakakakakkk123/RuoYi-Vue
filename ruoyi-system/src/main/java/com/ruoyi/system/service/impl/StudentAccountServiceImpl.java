package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.ForgotPasswordBody;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.StudentProfile;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.mapper.StudentAccountMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IStudentAccountService;

/**
 * Account module service implementation
 *
 * @author ruoyi
 */
@Service
public class StudentAccountServiceImpl implements IStudentAccountService
{
    private static final String STUDENT_ROLE_KEY = "student";
    private static final String REGISTER_CONFIG_KEY = "sys.account.registerUser";
    private static final Long STUDENT_DEFAULT_DEPT_ID = 103L;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private StudentAccountMapper accountMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterBody registerBody)
    {
        String username = StringUtils.trim(registerBody.getUsername());
        String password = StringUtils.trim(registerBody.getPassword());
        String studentNo = StringUtils.trim(registerBody.getStudentNo());
        String nickName = StringUtils.trim(registerBody.getNickName());
        String email = StringUtils.trim(registerBody.getEmail());

        if (!studentRegisterEnabled())
        {
            return "当前未开放学生自主注册。";
        }
        if (StringUtils.isEmpty(username))
        {
            return "登录账号不能为空。";
        }
        if (StringUtils.isEmpty(studentNo))
        {
            return "学号不能为空。";
        }
        if (StringUtils.isEmpty(password))
        {
            return "密码不能为空。";
        }
        if (StringUtils.isEmpty(nickName))
        {
            return "昵称不能为空。";
        }

        SysUser user = new SysUser();
        user.setUserName(username);
        user.setDeptId(STUDENT_DEFAULT_DEPT_ID);
        user.setStudentNo(studentNo);
        user.setNickName(nickName);
        user.setEmail(email);

        if (!userService.checkUserNameUnique(user))
        {
            return "登录账号已存在。";
        }
        if (!userService.checkStudentNoUnique(user))
        {
            return "该学号已被注册。";
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return "邮箱已被注册。";
        }

        SysRole studentRole = selectStudentRole();
        if (StringUtils.isNull(studentRole))
        {
            throw new ServiceException("系统缺少学生角色（role_key=student）。");
        }

        user.setRoleIds(new Long[] { studentRole.getRoleId() });
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setStatus(UserConstants.NORMAL);
        user.setPwdUpdateDate(DateUtils.getNowDate());
        user.setCreateBy("public");
        if (userService.insertUser(user) <= 0)
        {
            return "注册失败，请稍后重试或联系管理员。";
        }

        StudentProfile profile = new StudentProfile();
        profile.setUserId(user.getUserId());
        profile.setCreateBy(username);
        accountMapper.insertStudentProfile(profile);
        return StringUtils.EMPTY;
    }

    @Override
    public String forgotPassword(ForgotPasswordBody body)
    {
        String username = StringUtils.trim(body.getUsername());
        String studentNo = StringUtils.trim(body.getStudentNo());
        String newPassword = StringUtils.trim(body.getNewPassword());

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(studentNo))
        {
            return "账号和学号不能为空。";
        }
        if (StringUtils.isEmpty(newPassword))
        {
            return "新密码不能为空。";
        }

        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user))
        {
            return "未找到该账号。";
        }
        if (!StringUtils.equals(studentNo, user.getStudentNo()))
        {
            return "学号校验失败。";
        }
        if (!selectStudentRoleNames(user).contains(STUDENT_ROLE_KEY) && !user.isAdmin())
        {
            return "只有学生账号支持自助重置密码。";
        }

        String encoded = SecurityUtils.encryptPassword(newPassword);
        userService.resetUserPwd(user.getUserId(), encoded);
        return StringUtils.EMPTY;
    }

    @Override
    public StudentProfile selectStudentProfileByUserId(Long userId)
    {
        StudentProfile profile = accountMapper.selectStudentProfileByUserId(userId);
        if (StringUtils.isNull(profile))
        {
            profile = new StudentProfile();
            profile.setUserId(userId);
        }
        return profile;
    }

    @Override
    @Transactional
    public int updateStudentProfile(SysUser user, StudentProfile profile)
    {
        SysUser current = userService.selectUserById(user.getUserId());
        if (StringUtils.isNull(current))
        {
            throw new ServiceException("未找到该用户。");
        }
        current.setNickName(user.getNickName());
        current.setEmail(user.getEmail());
        current.setPhonenumber(user.getPhonenumber());
        current.setSex(user.getSex());
        current.setAvatar(user.getAvatar());
        current.setRemark(user.getRemark());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(current))
        {
            throw new ServiceException("手机号已存在。");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(current))
        {
            throw new ServiceException("邮箱已存在。");
        }
        int rows = userService.updateUserProfile(current);
        StudentProfile existing = accountMapper.selectStudentProfileByUserId(user.getUserId());
        profile.setUserId(user.getUserId());
        if (StringUtils.isNull(existing))
        {
            profile.setCreateBy(SecurityUtils.getUsername());
            accountMapper.insertStudentProfile(profile);
        }
        else
        {
            profile.setUpdateBy(SecurityUtils.getUsername());
            accountMapper.updateStudentProfile(profile);
        }
        return rows;
    }

    @Override
    public List<SysUser> selectStudentList(SysUser user)
    {
        return accountMapper.selectStudentList(user);
    }

    @Override
    public SysUser selectStudentByUserId(Long userId)
    {
        return accountMapper.selectStudentByUserId(userId);
    }

    @Override
    public int resetStudentPassword(Long userId, String password)
    {
        ensureStudentUser(userId);
        return userService.resetUserPwd(userId, SecurityUtils.encryptPassword(password));
    }

    @Override
    public int changeStudentStatus(Long userId, String status)
    {
        ensureStudentUser(userId);
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setStatus(status);
        return userService.updateUserStatus(user);
    }

    @Override
    public int deleteStudentUsers(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            ensureStudentUser(userId);
        }
        return userService.deleteUserByIds(userIds);
    }

    @Override
    public boolean studentRegisterEnabled()
    {
        String value = configValue(REGISTER_CONFIG_KEY);
        return StringUtils.isEmpty(value) || StringUtils.equals("true", value);
    }

    @Override
    public boolean updateStudentRegisterEnabled(boolean enabled, String operName)
    {
        SysConfig config = new SysConfig();
        config.setConfigKey(REGISTER_CONFIG_KEY);
        List<SysConfig> list = configService.selectConfigList(config);
        if (list.isEmpty())
        {
            throw new ServiceException("注册开关配置不存在。");
        }
        SysConfig target = list.get(0);
        target.setConfigValue(Boolean.toString(enabled));
        target.setUpdateBy(operName);
        return configService.updateConfig(target) > 0;
    }

    private SysRole selectStudentRole()
    {
        return roleService.selectRoleAll().stream()
                .filter(role -> StringUtils.equals(STUDENT_ROLE_KEY, role.getRoleKey()))
                .findFirst().orElse(null);
    }

    private void ensureStudentUser(Long userId)
    {
        SysUser user = userService.selectUserById(userId);
        if (StringUtils.isNull(user))
        {
            throw new ServiceException("未找到该用户。");
        }
        if (user.isAdmin())
        {
            throw new ServiceException("管理员账号不能执行此操作。");
        }
        if (!selectStudentRoleNames(user).contains(STUDENT_ROLE_KEY))
        {
            throw new ServiceException("仅支持学生账号。");
        }
    }

    private List<String> selectStudentRoleNames(SysUser user)
    {
        if (StringUtils.isNull(user) || StringUtils.isEmpty(user.getRoles()))
        {
            if (StringUtils.isNull(user))
            {
                return List.of();
            }
            SysUser full = userService.selectUserById(user.getUserId());
            user = full;
        }
        if (StringUtils.isNull(user) || StringUtils.isEmpty(user.getRoles()))
        {
            return List.of();
        }
        return user.getRoles().stream().map(SysRole::getRoleKey).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String configValue(String key)
    {
        SysConfig query = new SysConfig();
        query.setConfigKey(key);
        List<SysConfig> list = configService.selectConfigList(query);
        return list.isEmpty() ? "" : list.get(0).getConfigValue();
    }
}
