package com.ossnetwork.choloeksatheserver.repository;

import com.ossnetwork.choloeksatheserver.model.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Ratings, Integer> {
}