package io.github.djordjije11.reeled.post.domain;

import io.github.djordjije11.reeled.post.event.PostDeleted;
import io.github.djordjije11.reeled.post.event.PostUpserted;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToBaseEnumValue;
import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToEpochMilli;
import static io.github.djordjije11.reeled.commons.lang.MappingUtils.mapToNanos;

/**
 * @author Djordjije Radovic
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class PostEventMapper {

    static PostUpserted mapToPostUpserted(Post post) {
        return PostUpserted.newBuilder()
                .setAuthorId(post.getAuthorId())
                .setCategory(mapToBaseEnumValue(post.getCategory()))
                .setDescription(post.getDescription())
                .setDuration(mapToNanos(post.getDuration()))
                .setId(post.getId())
                .setMonetized(post.isMonetized())
                .setTitle(post.getTitle())
                .setVideoUrl(post.getVideoUrl())
                .build();
    }

    static PostDeleted mapToPostDeleted(Post post) {
        return PostDeleted.newBuilder().setId(post.getId()).setDeletedDate(mapToEpochMilli(post.getDeletedDate())).build();
    }
}
