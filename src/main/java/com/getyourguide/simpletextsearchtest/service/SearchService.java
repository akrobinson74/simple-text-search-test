package com.getyourguide.simpletextsearchtest.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.getyourguide.simpletextsearchtest.model.Review;
import com.getyourguide.simpletextsearchtest.repository.ReviewRepository;
import com.getyourguide.simpletextsearchtest.repository.TupleRepository;

@RestController
public class SearchService {

    @Autowired
    private TupleRepository tupleRepository;

    @Autowired
    private ReviewRepository reviewRepository;

//    @RequestMapping(path = "/s/{searchTerms}", method = RequestMethod.GET)
//    public ResponseEntity<List<String>> searchForTerms(@PathVariable("searchTerms") final String searchTerms) {

//        final Optional<List<String>> optionalMatchingReviews =
//            Optional.ofNullable(reviewRepository.findMatches(Arrays.asList(searchTerms.split(" "))));
//
//        if (!optionalMatchingReviews.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok().body(optionalMatchingReviews.get());
//    }

    @RequestMapping(path = "/s/{searchTerms}", method = RequestMethod.GET)
    public ResponseEntity<List<Review>> searchForTerms(@PathVariable("searchTerms") final String searchTerms) {

        final Optional<List<Review>> optionalMatchingTuples =
            Optional.ofNullable(tupleRepository.findMatchingAll(Arrays.asList(searchTerms.split(" "))));

        if (!optionalMatchingTuples.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(optionalMatchingTuples.get());
    }
}