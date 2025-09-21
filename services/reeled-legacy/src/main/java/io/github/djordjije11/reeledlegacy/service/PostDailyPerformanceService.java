package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.commons.exception.ReeledException;
import io.github.djordjije11.reeledlegacy.model.PostDailyPerformance;
import io.github.djordjije11.reeledlegacy.model.PostDailyPerformanceKey;
import io.github.djordjije11.reeledlegacy.repository.PostDailyPerformanceRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class PostDailyPerformanceService {

    private static final Logger logger = LoggerFactory.getLogger(PostDailyPerformanceService.class);

    private static final CSVFormat CSV_FILE_FORMAT = CSVFormat.Builder.create()
            .setDelimiter(',')
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .setIgnoreEmptyLines(true)
            .setNullString("")
            .setTrim(true)
            .build();

    private final PostDailyPerformanceRepository postDailyPerformanceRepository;

    public void importPostDailyPerformances(MultipartFile file) {
        Assert.notNull(file, "file must not be null");

        logger.info("Importing post daily performances (file: {})...", file.getOriginalFilename());

        try (final CSVParser csvParser = CSVParser.parse(file.getInputStream(), StandardCharsets.UTF_8, CSV_FILE_FORMAT)) {
            csvParser.stream().forEach(csvRecord -> {
                try {
                    save(new PostDailyPerformanceKey(getCsvRecordValue(csvRecord, "post_id", Long::parseLong),
                                    getCsvRecordValue(csvRecord, "date", LocalDate::parse)),
                            getCsvRecordValue(csvRecord, "search_appearances", Long::parseLong),
                            getCsvRecordValue(csvRecord, "views", Long::parseLong));
                } catch (RuntimeException e) {
                    logger.warn("Unable to process post daily performance CSV record (record: {}, recordNumber: {})",
                            csvRecord.toMap(),
                            csvRecord.getRecordNumber(),
                            e);
                }
            });

            logger.info("Successfully imported post daily performances from CSV file (file: {})", file.getOriginalFilename());
        } catch (IOException e) {
            throw new ReeledException("Failed to parse post daily performances CSV file (file: %s)".formatted(file.getOriginalFilename()), e);
        }
    }

    private void save(PostDailyPerformanceKey key, Long searchAppearances, Long views) {
        logger.info("Saving post daily performance (key: {})...", key);

        postDailyPerformanceRepository.findByKey(key).ifPresentOrElse(postDailyPerformance -> {
            postDailyPerformance.setSearchAppearances(searchAppearances);
            postDailyPerformance.setViews(views);

            postDailyPerformanceRepository.save(postDailyPerformance);
        }, () -> {
            final PostDailyPerformance postDailyPerformance = new PostDailyPerformance();
            postDailyPerformance.setKey(key);
            postDailyPerformance.setSearchAppearances(searchAppearances);
            postDailyPerformance.setViews(views);
            postDailyPerformanceRepository.save(postDailyPerformance);
        });

        logger.info("Post daily performance successfully saved (key: {})", key);
    }

    private static <T> T getCsvRecordValue(CSVRecord csvRecord, String header, Function<String, T> mapper) {
        return Optional.ofNullable(csvRecord.get(header)).filter(StringUtils::isNotBlank).map(mapper).orElse(null);
    }
}
