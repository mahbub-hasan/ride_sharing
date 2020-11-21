package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class RequesterCollaborationLog {
    private int requestCollaborationId;
    private Integer activityStatusId;
    private String comments;
    private Timestamp commentsTime;
    private Integer msgReceivedUserId;
    private boolean isSeen;
    private boolean isFinal;
    private RequestInfo requestInfoByRequestId;
    private ActivityInfo activityInfoByActivityId;
    private UserInfo userInfoByCommentsBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestCollaborationID", nullable = false)
    public int getRequestCollaborationId() {
        return requestCollaborationId;
    }

    public void setRequestCollaborationId(int requestCollaborationId) {
        this.requestCollaborationId = requestCollaborationId;
    }

    @Basic
    @Column(name = "ActivityStatusID", nullable = true)
    public Integer getActivityStatusId() {
        return activityStatusId;
    }

    public void setActivityStatusId(Integer activityStatusId) {
        this.activityStatusId = activityStatusId;
    }

    @Basic
    @Column(name = "Comments", nullable = false, length = 2147483647)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Basic
    @Column(name = "CommentsTime", nullable = false)
    public Timestamp getCommentsTime() {
        return commentsTime;
    }

    public void setCommentsTime(Timestamp commentsTime) {
        this.commentsTime = commentsTime;
    }

    @Basic
    @Column(name = "MsgReceivedUserID", nullable = true)
    public Integer getMsgReceivedUserId() {
        return msgReceivedUserId;
    }

    public void setMsgReceivedUserId(Integer msgReceivedUserId) {
        this.msgReceivedUserId = msgReceivedUserId;
    }

    @Basic
    @Column(name = "isSeen", nullable = false)
    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    @Basic
    @Column(name = "isFinal", nullable = false)
    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequesterCollaborationLog that = (RequesterCollaborationLog) o;

        if (requestCollaborationId != that.requestCollaborationId) return false;
        if (isSeen != that.isSeen) return false;
        if (isFinal != that.isFinal) return false;
        if (activityStatusId != null ? !activityStatusId.equals(that.activityStatusId) : that.activityStatusId != null)
            return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        if (commentsTime != null ? !commentsTime.equals(that.commentsTime) : that.commentsTime != null) return false;
        if (msgReceivedUserId != null ? !msgReceivedUserId.equals(that.msgReceivedUserId) : that.msgReceivedUserId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestCollaborationId;
        result = 31 * result + (activityStatusId != null ? activityStatusId.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (commentsTime != null ? commentsTime.hashCode() : 0);
        result = 31 * result + (msgReceivedUserId != null ? msgReceivedUserId.hashCode() : 0);
        result = 31 * result + (isSeen ? 1 : 0);
        result = 31 * result + (isFinal ? 1 : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "RequestID", referencedColumnName = "RequestID", nullable = false)
    public RequestInfo getRequestInfoByRequestId() {
        return requestInfoByRequestId;
    }

    public void setRequestInfoByRequestId(RequestInfo requestInfoByRequestId) {
        this.requestInfoByRequestId = requestInfoByRequestId;
    }

    @ManyToOne
    @JoinColumn(name = "ActivityID", referencedColumnName = "ActivityID", nullable = false)
    public ActivityInfo getActivityInfoByActivityId() {
        return activityInfoByActivityId;
    }

    public void setActivityInfoByActivityId(ActivityInfo activityInfoByActivityId) {
        this.activityInfoByActivityId = activityInfoByActivityId;
    }

    @ManyToOne
    @JoinColumn(name = "CommentsBy", referencedColumnName = "UserID", nullable = false)
    public UserInfo getUserInfoByCommentsBy() {
        return userInfoByCommentsBy;
    }

    public void setUserInfoByCommentsBy(UserInfo userInfoByCommentsBy) {
        this.userInfoByCommentsBy = userInfoByCommentsBy;
    }
}
