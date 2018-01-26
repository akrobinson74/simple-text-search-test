package com.getyourguide.simpletextsearchtest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewSet {
    private final List<Review> reviewList;
    private final Boolean status;
    private final Integer totalReviews;

    @JsonCreator
    public ReviewSet(
        @JsonProperty("data") final List<Review> reviewList,
        @JsonProperty("status") final Boolean status,
        @JsonProperty("total_reviews") final Integer totalReviews) {
        this.reviewList = reviewList;
        this.status = status;
        this.totalReviews = totalReviews;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public Boolean getStatus() {
        return status;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }
}
