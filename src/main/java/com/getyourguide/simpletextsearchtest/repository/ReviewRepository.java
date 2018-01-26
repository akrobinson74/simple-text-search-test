package com.getyourguide.simpletextsearchtest.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getyourguide.simpletextsearchtest.model.Review;
import com.getyourguide.simpletextsearchtest.model.ReviewSet;

@Component
public class ReviewRepository {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Integer TOPDOCS_MAX = 250;

//    private Map<Integer, Review> reviewMap;

    private final String indexDir;
    private final String inputDataFile;
    private final IndexSearcher indexSearcher;
    private final MMapDirectory mMapDirectory;

    @Autowired
    public ReviewRepository(
        @Value("${reviewIndex.dataFile}") final String inputDataFile,
        @Value("${reviewIndex.dir}") final String indexDir) throws IOException {
        this.indexDir = indexDir;
        this.inputDataFile = inputDataFile;

        Path indexPath = Paths.get(indexDir);
        if (Files.notExists(indexPath))
            Files.createDirectories(indexPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-xr-x")));

        this.mMapDirectory = new MMapDirectory(Paths.get(indexDir));
        this.indexSearcher = buildSearchIndex(inputDataFile);
    }

    public List<String> findMatches(final List<String> searchTerms) {
//        final String booleanMultiTermQueryString = String.join(" AND ", searchTerms);
        final BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        searchTerms.stream().forEach(st -> {
            queryBuilder.add(new BooleanClause(new TermQuery(new Term("message", st)), BooleanClause.Occur.MUST));
        });
//        final Query booleanQuery = new QueryParser(booleanMultiTermQueryString, new StandardAnalyzer());
        final Query query = queryBuilder.build();
//        final List<Document> documentList = indexSearcher.search(query);
//        TopDocsCollector<?> topDocsCollector = TopScoreDocCollector.
        TopDocs hits = null;
        try {
            hits = indexSearcher.search(query, TOPDOCS_MAX);
            return Arrays.asList(hits.scoreDocs).stream().map(sd -> {
                try {
                    return indexSearcher.doc(sd.doc);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }).map(hd -> hd.get("message")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.<String>emptyList();
    }

    private IndexSearcher buildSearchIndex(final String inputDataFile) {
        final File inputFile = new File(this.getClass().getClassLoader().getResource(inputDataFile).getFile());
        final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        IndexSearcher indexSearcher = null;

        try {
            IndexWriter indexWriter = new IndexWriter(mMapDirectory, indexWriterConfig);
            Files.lines(inputFile.toPath()).forEach(
                line -> {indexReview(indexWriter, line);}
            );

            indexWriter.close();

            indexSearcher = new IndexSearcher(DirectoryReader.open(mMapDirectory));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return indexSearcher;
    }

    private void indexReview(final IndexWriter indexWriter, final String reviewJSONStr) {
        try {
            OBJECT_MAPPER.readValue(reviewJSONStr, ReviewSet.class).getReviewList().stream().forEach(
                review -> {
                    try {
//                        reviewMap.put(review.getReviewId(), review);
                        indexWriter.addDocument(createSearchDoc(review));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Document createSearchDoc(final Review review) {
        Document document = new Document();
        document.add(new NumericDocValuesField("review_id", Long.valueOf(review.getReviewId())));
        document.add(new TextField("message", review.getMessage(), Field.Store.YES));
        document.add(new SortedDocValuesField("message", new BytesRef(review.getMessage())));
        return document;
    }
}
