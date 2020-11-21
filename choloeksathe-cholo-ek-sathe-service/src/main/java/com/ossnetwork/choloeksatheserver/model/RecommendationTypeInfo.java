package com.ossnetwork.choloeksatheserver.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class RecommendationTypeInfo {
    private int recommendationId;
    private String recommendation;
    private boolean isActive;
    private Collection<ActivityInfo> activityInfosByRecommendationId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecommendationID", nullable = false)
    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    @Basic
    @Column(name = "Recommendation", nullable = false, length = 50)
    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    @Basic
    @Column(name = "isActive", nullable = false)
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendationTypeInfo that = (RecommendationTypeInfo) o;

        if (recommendationId != that.recommendationId) return false;
        if (isActive != that.isActive) return false;
        if (recommendation != null ? !recommendation.equals(that.recommendation) : that.recommendation != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = recommendationId;
        result = 31 * result + (recommendation != null ? recommendation.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @OneToMany(mappedBy = "recommendationTypeInfoByRecommendationId")
    public Collection<ActivityInfo> getActivityInfosByRecommendationId() {
        return activityInfosByRecommendationId;
    }

    public void setActivityInfosByRecommendationId(Collection<ActivityInfo> activityInfosByRecommendationId) {
        this.activityInfosByRecommendationId = activityInfosByRecommendationId;
    }
}
