package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.ForgotPasswordBody;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.system.domain.StudentProfile;

/**
 * 账户模块服务层
 * 
 * @author ruoyi
 */
public interface IStudentAccountService
{
    public String register(RegisterBody registerBody);

    public String forgotPassword(ForgotPasswordBody body);

    public StudentProfile selectStudentProfileByUserId(Long userId);

    public int updateStudentProfile(SysUser user, StudentProfile profile);

    public List<SysUser> selectStudentList(SysUser user);

    public SysUser selectStudentByUserId(Long userId);

    public int resetStudentPassword(Long userId, String password);

    public int resetStudentPasswords(Long[] userIds, String password);

    public int changeStudentStatus(Long userId, String status);

    public int changeStudentStatuses(Long[] userIds, String status);

    public int deleteStudentUsers(Long[] userIds);

    public boolean studentRegisterEnabled();

    public boolean updateStudentRegisterEnabled(boolean enabled, String operName);

    public String importStudentUsers(List<SysUser> userList, Boolean isUpdateSupport, Boolean disableMissing, String operName);
}
