package io.github.djordjije11.reeled.postmetrics.infra.messaging;

import io.github.djordjije11.reeled.codes.PostCodes.PostCategory;
import io.github.djordjije11.reeled.post.event.PostUpserted;
import io.github.djordjije11.reeled.postmetrics.domain.PostData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToBaseEnum;
import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToDuration;
import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToString;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class PostMetricsEventMapper {

    static PostData mapToPostData(PostUpserted postUpserted) {
        Assert.notNull(postUpserted, "postUpserted must not be null");

        return new PostData(postUpserted.getAuthorId(),
                mapToBaseEnum(postUpserted.getCategory(), PostCategory.class),
                mapToDuration(postUpserted.getDuration()),
                postUpserted.getMonetized(),
                mapToString(postUpserted.getTitle()),
                mapToString(postUpserted.getVideoUrl()));
    }
}
