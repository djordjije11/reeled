package io.github.djordjije11.reeledlegacy;

import io.github.djordjije11.reeledlegacy.commons.exception.ReeledException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * To import mock data, you can use the following steps:
 * 1. Export new test_post.csv file if missing, with a query: SELECT id FROM pm_post;
 * 2. Uncomment @Test annotation and run this test to generate CSV files
 * 3. Import generated CSV files
 *
 * @author Djordjije Radovic
 */
class TestPostDailyPerformanceFileGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TestPostDailyPerformanceFileGenerator.class);

    private static final CSVFormat TEST_POST_CSV_FILE_FORMAT = CSVFormat.Builder.create()
            .setDelimiter(',')
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreHeaderCase(true)
            .setIgnoreEmptyLines(true)
            .setNullString("")
            .setTrim(true)
            .build();

    private static final String TEST_POST_FILE_PATH = "src/test/resources/test_post.csv";

    private static final String[] TEST_POST_DAILY_PERFORMANCE_CSV_FILE_HEADER = new String[]{"post_id", "date", "search_appearances", "views"};

    private static final CSVFormat TEST_POST_DAILY_PERFORMANCE_CSV_FILE_FORMAT = CSVFormat.Builder.create()
            .setDelimiter(',')
            .setHeader(TEST_POST_DAILY_PERFORMANCE_CSV_FILE_HEADER)
            .setRecordSeparator('\n')
            .setIgnoreEmptyLines(true)
            .setQuoteMode(QuoteMode.MINIMAL)
            .setTrim(true)
            .build();

    private static final YearMonth FROM = YearMonth.of(2025, Month.FEBRUARY);

    private static final YearMonth TO = YearMonth.of(2025, Month.MARCH);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //    @Test
    void generatePostDailyPerformanceFiles() {
        generateFiles(FROM, TO, getPostIds());
    }

    private static Set<Long> getPostIds() {
        final File file = new File(TEST_POST_FILE_PATH);

        try (final CSVParser csvParser = CSVParser.parse(file, StandardCharsets.UTF_8, TEST_POST_CSV_FILE_FORMAT)) {
            return csvParser.stream().parallel().map(TestPostDailyPerformanceFileGenerator::mapToPostId).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new ReeledException("Unable to parse CSV file", e);
        }
    }

    private static Long mapToPostId(CSVRecord csvRecord) {
        return Long.parseLong(csvRecord.get("id"));
    }

    private static void generateFiles(YearMonth from, YearMonth to, Set<Long> postIds) {
        final Random random = new Random();

        YearMonth current = from;
        while (current.isBefore(to)) {
            try {
                generateFile(current, postIds, random);
            } catch (IOException | RuntimeException e) {
                logger.error("Error while generating file for year month: {}", current, e);
            }
            current = current.plusMonths(1);
        }
    }

    private static void generateFile(YearMonth yearMonth, Set<Long> postIds, Random random) throws IOException {
        final String fileName = "test-post-daily-performance-%s-%s.csv".formatted(yearMonth.getYear(), yearMonth.getMonthValue());

        try (final CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(fileName), TEST_POST_DAILY_PERFORMANCE_CSV_FILE_FORMAT)) {
            for (LocalDate date = yearMonth.atDay(1); !date.isAfter(yearMonth.atEndOfMonth()); date = date.plusDays(1)) {
                final LocalDate currentDate = date;
                postIds.forEach(postId -> {
                    try {
                        csvPrinter.printRecord(generateCsvRecord(currentDate, postId, random));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            logger.debug("CSV file created: {}", fileName);
        }
    }

    private static List<String> generateCsvRecord(LocalDate date, Long postId, Random random) {
        final long searchAppearances = random.nextLong(100_000L);
        final long views = searchAppearances == 0 ? 0 : random.nextLong(searchAppearances);

        return Stream.of(postId, date.format(DATE_FORMATTER), searchAppearances, views)
                .map(x -> Optional.ofNullable(x).map(Object::toString).orElse(""))
                .toList();
    }
}
