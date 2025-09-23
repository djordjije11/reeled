package io.github.djordjije11.reeledlegacy.controller;

import io.github.djordjije11.reeledlegacy.dto.PostMetricsSearchDto;
import io.github.djordjije11.reeledlegacy.model.PostDailyMetricsProjection;
import io.github.djordjije11.reeledlegacy.service.PostDailyPerformanceService;
import io.github.djordjije11.reeledlegacy.service.PostMetricsQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

/**
 * @author Djordjije Radovic
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Post Metrics")
@Validated
public class PostMetricsController {

    private final PostDailyPerformanceService postDailyPerformanceService;

    private final PostMetricsQueryService postMetricsQueryService;

    private final AsyncTaskExecutor asyncTaskExecutor;

    public PostMetricsController(PostDailyPerformanceService postDailyPerformanceService,
                                 PostMetricsQueryService postMetricsQueryService,
                                 @Qualifier(APPLICATION_TASK_EXECUTOR_BEAN_NAME) AsyncTaskExecutor asyncTaskExecutor) {
        this.postDailyPerformanceService = postDailyPerformanceService;
        this.postMetricsQueryService = postMetricsQueryService;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @PostMapping(value = "/posts/metrics/import", consumes = "multipart/form-data")
    @Operation(description = "Imports post metrics csv file for an author")
    @ApiResponse(responseCode = "202", description = "Post metrics csv file accepted")
    public ResponseEntity<Void> importMetrics(@Parameter(description = """
            <strong>CSV</strong> file with post metrics.<br/><br/>
            Expected format:
            <pre>
            post_id,date,search_appearances,views
            1,2025-02-01,93924,24643
            </pre>
            """) @RequestParam("file") MultipartFile file) {
        asyncTaskExecutor.execute(() -> postDailyPerformanceService.importPostDailyPerformances(file));

        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/authors/{authorId}/posts/metrics/search")
    @Operation(description = "Returns post metrics for an author")
    @ApiResponse(responseCode = "200", description = "Post metrics for an author returned")
    public ResponseEntity<List<PostDailyMetricsProjection>> searchMetrics(@PathVariable Long authorId, @Valid @RequestBody PostMetricsSearchDto searchDto) {
        return ResponseEntity.ok(postMetricsQueryService.search(authorId,
                searchDto.query().dateFrom(),
                searchDto.query().dateTo(),
                searchDto.query().durationFrom(),
                searchDto.query().durationTo(),
                searchDto.query().categoryIds(),
                searchDto.query().monetized()));
    }
}
