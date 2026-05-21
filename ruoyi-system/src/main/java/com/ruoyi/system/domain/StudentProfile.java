package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生档案对象 edu_student_profile
 * 
 * @author ruoyi
 */
public class StudentProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long profileId;

    private Long userId;

    private String signature;

    private String todoItems;

    private String learningHistory;

    private String learningNotes;

    private String favorites;

    private String wrongQuestions;

    private String discussions;

    public Long getProfileId()
    {
        return profileId;
    }

    public void setProfileId(Long profileId)
    {
        this.profileId = profileId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public String getTodoItems()
    {
        return todoItems;
    }

    public void setTodoItems(String todoItems)
    {
        this.todoItems = todoItems;
    }

    public String getLearningHistory()
    {
        return learningHistory;
    }

    public void setLearningHistory(String learningHistory)
    {
        this.learningHistory = learningHistory;
    }

    public String getLearningNotes()
    {
        return learningNotes;
    }

    public void setLearningNotes(String learningNotes)
    {
        this.learningNotes = learningNotes;
    }

    public String getFavorites()
    {
        return favorites;
    }

    public void setFavorites(String favorites)
    {
        this.favorites = favorites;
    }

    public String getWrongQuestions()
    {
        return wrongQuestions;
    }

    public void setWrongQuestions(String wrongQuestions)
    {
        this.wrongQuestions = wrongQuestions;
    }

    public String getDiscussions()
    {
        return discussions;
    }

    public void setDiscussions(String discussions)
    {
        this.discussions = discussions;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("profileId", getProfileId())
                .append("userId", getUserId())
                .append("signature", getSignature())
                .append("todoItems", getTodoItems())
                .append("learningHistory", getLearningHistory())
                .append("learningNotes", getLearningNotes())
                .append("favorites", getFavorites())
                .append("wrongQuestions", getWrongQuestions())
                .append("discussions", getDiscussions())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
