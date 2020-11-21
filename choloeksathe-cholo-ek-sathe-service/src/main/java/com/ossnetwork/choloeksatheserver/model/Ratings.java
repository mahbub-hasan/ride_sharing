package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Ratings {
    private int id;
    private Integer ratingPoint;
    private String comments;
    private Timestamp ratingDate;
    private ActivityInfo activityInfoByActivityId;
    private UserInfo userInfoByRatingBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "RatingPoint", nullable = true)
    public Integer getRatingPoint() {
        return ratingPoint;
    }

    public void setRatingPoint(Integer ratingPoint) {
        this.ratingPoint = ratingPoint;
    }

    @Basic
    @Column(name = "Comments", nullable = true, length = 250)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Basic
    @Column(name = "RatingDate", nullable = true)
    public Timestamp getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(Timestamp ratingDate) {
        this.ratingDate = ratingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ratings ratings = (Ratings) o;

        if (id != ratings.id) return false;
        if (ratingPoint != null ? !ratingPoint.equals(ratings.ratingPoint) : ratings.ratingPoint != null) return false;
        if (comments != null ? !comments.equals(ratings.comments) : ratings.comments != null) return false;
        if (ratingDate != null ? !ratingDate.equals(ratings.ratingDate) : ratings.ratingDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ratingPoint != null ? ratingPoint.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (ratingDate != null ? ratingDate.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "ActivityID", referencedColumnName = "ActivityID")
    public ActivityInfo getActivityInfoByActivityId() {
        return activityInfoByActivityId;
    }

    public void setActivityInfoByActivityId(ActivityInfo activityInfoByActivityId) {
        this.activityInfoByActivityId = activityInfoByActivityId;
    }

    @ManyToOne
    @JoinColumn(name = "RatingBy", referencedColumnName = "UserID")
    public UserInfo getUserInfoByRatingBy() {
        return userInfoByRatingBy;
    }

    public void setUserInfoByRatingBy(UserInfo userInfoByRatingBy) {
        this.userInfoByRatingBy = userInfoByRatingBy;
    }
}
