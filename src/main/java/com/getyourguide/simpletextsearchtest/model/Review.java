package com.getyourguide.simpletextsearchtest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    private final String date;
    private final String message;
    private final Integer reviewId;

    @JsonCreator
    public Review(
        @JsonProperty("date") final String date,
        @JsonProperty("message") final String message,
        @JsonProperty("review_id") final Integer reviewId) {
        this.date = date;
        this.message = message;
        this.reviewId = reviewId;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (date != null ? !date.equals(review.date) : review.date != null) return false;
        if (!message.equals(review.message)) return false;
        return reviewId.equals(review.reviewId);
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + message.hashCode();
        result = 31 * result + reviewId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Review{" +
            "date='" + date + '\'' +
            ", message='" + message + '\'' +
            ", reviewId=" + reviewId +
            '}';
    }

    public Boolean containsAllTerms(List<String> soughtTerms) {
        for (String term: soughtTerms) {
            if (!this.message.toLowerCase().contains(term))
                return false;
        }
        return true;
    }
}
