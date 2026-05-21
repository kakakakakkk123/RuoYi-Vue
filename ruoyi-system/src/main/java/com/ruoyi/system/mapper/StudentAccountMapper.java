package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.StudentProfile;

/**
 * 账户模块数据层
 * 
 * @author ruoyi
 */
public interface StudentAccountMapper
{
    public StudentProfile selectStudentProfileByUserId(Long userId);

    public int insertStudentProfile(StudentProfile profile);

    public int updateStudentProfile(StudentProfile profile);

    public List<SysUser> selectStudentList(SysUser user);

    public SysUser selectStudentByUserId(Long userId);

    public SysUser checkStudentNoUnique(String studentNo);
}
