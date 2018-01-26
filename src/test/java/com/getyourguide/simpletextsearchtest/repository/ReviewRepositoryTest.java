package com.getyourguide.simpletextsearchtest.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.getyourguide.simpletextsearchtest.SimpleTextSearchTestApplication;

@SpringBootTest(classes = SimpleTextSearchTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewRepositoryTest {
//    @Autowired
    private ReviewRepository reviewRepository;

    @Before
    public void setUp() throws IOException {
        reviewRepository = new ReviewRepository("data.json","/tmp/testidx");
    }

    @After
    public void tearDown() {
        reviewRepository = null;
    }

    @Test
    public void testTermMatch() {
        final List<String> documentList = reviewRepository.findMatches(Arrays.asList("awesome"));
        assertNotNull(documentList);
        assertTrue(documentList.size() >= 1);
    }
}
