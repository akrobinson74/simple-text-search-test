package com.getyourguide.simpletextsearchtest.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.getyourguide.simpletextsearchtest.model.Review;

public class TupleRepositoryTest {
//    private static final TupleRepository TUPLE_REPOSITORY = new TupleRepositoryository();

    private TupleRepository tupleRepository = new TupleRepository();

    @Test
    public void testTRT() {
        assertTrue(TupleRepository.getKeywordCount() > 1);
    }

    @Test
    public void testContainsTerm() {
        final List<Review> reviewList = tupleRepository.findMatchingAll(Arrays.asList("wonderful","sistine","museum"));
        assertNotNull(reviewList);
    }
}
