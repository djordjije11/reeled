package io.github.djordjije11.reeledlegacy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Post Category")
@Validated
public class PostCategoryController {

}
