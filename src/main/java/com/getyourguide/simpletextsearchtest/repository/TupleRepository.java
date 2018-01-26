package com.getyourguide.simpletextsearchtest.repository;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourguide.simpletextsearchtest.model.Review;
import com.getyourguide.simpletextsearchtest.model.ReviewSet;
import com.google.common.collect.ImmutableList;

@Component
public class TupleRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String DATA_FILE = "data.json";
    private static final Map<Integer, Review> REVIEW_MAP;
    private static final Set<String> STOPWORD_SET =
        new HashSet<String>() {{
            addAll(Arrays.asList("this", "that", "the", "to"));
        }};
    private static final Map<String, Set<Integer>> TERM_TO_TUPLESET_MAP;

    static {
        REVIEW_MAP = new HashMap<>();
        TERM_TO_TUPLESET_MAP = new HashMap<>();
        try {
            loadDataJSON(new File(TupleRepository.class.getClassLoader().getResource(DATA_FILE).getFile()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadDataJSON(final File data) throws Exception {
        Files.lines(data.toPath()).forEach(
            l -> {
                try {
                    OBJECT_MAPPER.readValue(l, ReviewSet.class).getReviewList()
                        .forEach(TupleRepository::addReviewTuples);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        );
    }

    private static void addReviewTuples(final Review review) {
        REVIEW_MAP.put(review.getReviewId(), review);

        Stream.of(review.getMessage().split(" ")).forEach(
            key -> {
                String lcKey = key.toLowerCase().replaceAll("[.,;-]", "");
                if (!lcKey.isEmpty() && !STOPWORD_SET.contains(lcKey)) {
                    if (TERM_TO_TUPLESET_MAP.containsKey(lcKey)) {
                        TERM_TO_TUPLESET_MAP.get(lcKey).add(review.getReviewId());
                    } else {
                        TERM_TO_TUPLESET_MAP.put(lcKey, new HashSet<Integer>() {{
                            add(review.getReviewId());
                        }});
                    }
                }
            }
        );
    }

    public static Integer getKeywordCount() {
        return TERM_TO_TUPLESET_MAP.size();
    }

    public List<Review> findMatchingAll(final List<String> soughtTerms) {
        int matchesNecessary = soughtTerms.size();

        final String initialTerm = soughtTerms.get(0);

        final Optional<Set<Integer>> optionalMatchingReviewIds =
            Optional.ofNullable(TERM_TO_TUPLESET_MAP.get(initialTerm));

        if (optionalMatchingReviewIds.isPresent()) {
            Set<Integer> remainingReviewIds = optionalMatchingReviewIds.get();

            // iterate through remaining terms, restrict the original result set to subsequent matches
            if (matchesNecessary > 1) {
                for (String term : soughtTerms.subList(1, matchesNecessary - 1)) {
                    if (remainingReviewIds.size() == 0 || !TERM_TO_TUPLESET_MAP.containsKey(term))
                        break;
                    remainingReviewIds.retainAll(TERM_TO_TUPLESET_MAP.get(term));
                }
            }

            if (remainingReviewIds.size() >= 1)
                remainingReviewIds.stream().map(id -> REVIEW_MAP.get(id)).collect(Collectors.toList());
        }

        return ImmutableList.of();
    }
}
