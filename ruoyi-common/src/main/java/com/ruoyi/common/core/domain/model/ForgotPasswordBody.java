package com.ruoyi.common.core.domain.model;

/**
 * 忘记密码请求对象
 * 
 * @author ruoyi
 */
public class ForgotPasswordBody extends LoginBody
{
    /**
     * 学号
     */
    private String studentNo;

    /**
     * 新密码
     */
    private String newPassword;

    public String getStudentNo()
    {
        return studentNo;
    }

    public void setStudentNo(String studentNo)
    {
        this.studentNo = studentNo;
    }

    public String getNewPassword()
    {
        return newPassword;
    }

    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }
}
