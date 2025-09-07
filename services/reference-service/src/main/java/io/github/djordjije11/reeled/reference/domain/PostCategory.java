package io.github.djordjije11.reeled.reference.domain;

import io.github.djordjije11.reeled.commons.exception.ReeledDomainException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@Getter
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "r_post_category")
public class PostCategory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String key;

    public PostCategory(String key, PostCategoryRepository postCategoryRepository) {
        Assert.hasText(key, "key must be provided");

        if (postCategoryRepository.existsByKey(key)) {
            throw new ReeledDomainException("Cannot create post category, key already exists: %s".formatted(key));
        }

        this.key = key;
    }
}
