package io.github.djordjije11.reeled.legacyconnector.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.springframework.util.Assert;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
@Immutable
public class LegacyAuthorData {

    @Column(name = "legacy_name")
    private String name;

    @Column(name = "legacy_type_id")
    private Long typeId;

    @Column(name = "legacy_bio")
    private String bio;

    @Column(name = "legacy_image_url")
    private String imageUrl;

    public LegacyAuthorData(String name, Long typeId, String bio, String imageUrl) {
        Assert.hasText(name, "name must be provided");
        Assert.notNull(typeId, "typeId must not be null");

        this.name = name;
        this.typeId = typeId;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }
}
