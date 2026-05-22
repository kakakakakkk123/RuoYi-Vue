package com.ruoyi.common.core.domain.model;

/**
 * 账户安全配置
 */
public class AccountSecuritySettings
{
    private String blackIpList;

    private String blockedUserAgentKeywords;

    public String getBlackIpList()
    {
        return blackIpList;
    }

    public void setBlackIpList(String blackIpList)
    {
        this.blackIpList = blackIpList;
    }

    public String getBlockedUserAgentKeywords()
    {
        return blockedUserAgentKeywords;
    }

    public void setBlockedUserAgentKeywords(String blockedUserAgentKeywords)
    {
        this.blockedUserAgentKeywords = blockedUserAgentKeywords;
    }
}
