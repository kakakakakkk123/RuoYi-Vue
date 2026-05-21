package com.ruoyi.web.controller.account;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.ForgotPasswordBody;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.SysPasswordService;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.IStudentAccountService;

/**
 * 账户模块接口
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/account")
public class StudentAccountController extends BaseController
{
    @Autowired
    private IStudentAccountService studentAccountService;

    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysConfigService configService;

    @Anonymous
    @PostMapping("/forgotPassword")
    public AjaxResult forgotPassword(@RequestBody ForgotPasswordBody body)
    {
        if (configService.selectCaptchaEnabled())
        {
            registerService.validateCaptcha(body.getUsername(), body.getCode(), body.getUuid());
        }
        String msg = studentAccountService.forgotPassword(body);
        if (StringUtils.isEmpty(msg))
        {
            passwordService.clearLoginRecordCache(body.getUsername());
        }
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    @Anonymous
    @GetMapping("/registerEnabled")
    public AjaxResult registerEnabled()
    {
        return success(studentAccountService.studentRegisterEnabled());
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @GetMapping("/students")
    public TableDataInfo students(SysUser user)
    {
        startPage();
        return getDataTable(studentAccountService.selectStudentList(user));
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @PostMapping("/students/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport, boolean disableMissing) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        java.util.List<SysUser> userList = util.importExcel(file.getInputStream());
        String msg = studentAccountService.importStudentUsers(userList, updateSupport, disableMissing, getUsername());
        return success(msg);
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @PostMapping("/students/importTemplate")
    public void importTemplate(jakarta.servlet.http.HttpServletResponse response)
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "学生数据");
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @GetMapping("/students/{userId}")
    public AjaxResult student(@PathVariable Long userId)
    {
        return success(studentAccountService.selectStudentByUserId(userId));
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @PutMapping("/students/{userId}/password")
    public AjaxResult resetPassword(@PathVariable Long userId, @RequestBody SysUser body)
    {
        AjaxResult result = toAjax(studentAccountService.resetStudentPassword(userId, body.getPassword()));
        passwordService.clearLoginRecordCache(studentAccountService.selectStudentByUserId(userId).getUserName());
        return result;
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @PutMapping("/students/{userId}/status")
    public AjaxResult changeStatus(@PathVariable Long userId, @RequestBody SysUser body)
    {
        return toAjax(studentAccountService.changeStudentStatus(userId, body.getStatus()));
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @PutMapping("/students/password")
    public AjaxResult resetPasswords(@RequestBody SysUser body)
    {
        AjaxResult result = toAjax(studentAccountService.resetStudentPasswords(body.getRoleIds(), body.getPassword()));
        for (Long userId : body.getRoleIds())
        {
            passwordService.clearLoginRecordCache(studentAccountService.selectStudentByUserId(userId).getUserName());
        }
        return result;
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @PutMapping("/students/status")
    public AjaxResult changeStatuses(@RequestBody SysUser body)
    {
        return toAjax(studentAccountService.changeStudentStatuses(body.getRoleIds(), body.getStatus()));
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @DeleteMapping("/students/{userIds}")
    public AjaxResult deleteStudents(@PathVariable Long[] userIds)
    {
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId()))
        {
            return error("当前用户不能删除");
        }
        return toAjax(studentAccountService.deleteStudentUsers(userIds));
    }

    @PreAuthorize("@ss.hasAnyRoles('teacher,admin')")
    @PutMapping("/registerEnabled")
    public AjaxResult setRegisterEnabled(@RequestBody SysUser body)
    {
        boolean enabled = "0".equals(body.getStatus()) || "true".equalsIgnoreCase(body.getStatus());
        return toAjax(studentAccountService.updateStudentRegisterEnabled(enabled, getUsername()));
    }
}
