package io.github.djordjije11.reeled.legacyconnector.domain;

import io.github.djordjije11.reeled.codes.AuthorCodes.AuthorType;
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
public class AuthorData {

    private String name;

    private AuthorType type;

    private String bio;

    private String imageUrl;

    public AuthorData(String name, AuthorType type, String bio, String imageUrl) {
        Assert.hasText(name, "name must be provided");
        Assert.notNull(type, "type must not be null");

        this.name = name;
        this.type = type;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }
}
