package io.github.djordjije11.reeledlegacy.commons.openapi;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Djordjije Radovic
 */
public class SchemaReplacer implements ModelConverter {

    private final Map<Class<?>, Class<?>> replacements;

    private SchemaReplacer(Map<Class<?>, Class<?>> replacements) {
        this.replacements = replacements;
    }

    @Override
    public Schema<?> resolve(AnnotatedType target, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (target.isSchemaProperty() && isReplacementConfigured(target)) {
            return createNewSchema(target);
        }

        return chain.hasNext() ? chain.next().resolve(target, context, chain) : null;
    }

    private boolean isReplacementConfigured(AnnotatedType target) {
        return replacements.containsKey(getTargetClass(target));
    }

    private static Class<?> getTargetClass(AnnotatedType target) {
        final Optional<io.swagger.v3.oas.annotations.media.Schema> targetSchema = getTargetSchema(target);
        final Optional<Class<?>> targetSchemaImpl = targetSchema.map(io.swagger.v3.oas.annotations.media.Schema::implementation);
        return targetSchemaImpl.isPresent() ? targetSchemaImpl.get() : ((JavaType) target.getType()).getRawClass();
    }

    private static Optional<io.swagger.v3.oas.annotations.media.Schema> getTargetSchema(AnnotatedType target) {
        return Arrays.stream(target.getCtxAnnotations())
                .filter(io.swagger.v3.oas.annotations.media.Schema.class::isInstance)
                .map(io.swagger.v3.oas.annotations.media.Schema.class::cast)
                .findFirst();
    }

    private Schema<Object> createNewSchema(AnnotatedType target) {
        final Optional<io.swagger.v3.oas.annotations.media.Schema> targetSchema = getTargetSchema(target);
        final io.swagger.v3.oas.annotations.media.Schema replacementSchema = getReplacementSchema(target);

        final Schema<Object> schema = new Schema<>();

        schema.setType(getSchemaProperty(targetSchema, replacementSchema, io.swagger.v3.oas.annotations.media.Schema::type, new StringSchema().getType()));
        schema.setFormat(getSchemaProperty(targetSchema, replacementSchema, io.swagger.v3.oas.annotations.media.Schema::format, null));
        schema.setTitle(getSchemaProperty(targetSchema, replacementSchema, io.swagger.v3.oas.annotations.media.Schema::title, null));
        schema.setDescription(getSchemaProperty(targetSchema, replacementSchema, io.swagger.v3.oas.annotations.media.Schema::description, null));
        schema.setPattern(getSchemaProperty(targetSchema, replacementSchema, io.swagger.v3.oas.annotations.media.Schema::pattern, null));
        schema.setExample(getSchemaProperty(targetSchema, replacementSchema, io.swagger.v3.oas.annotations.media.Schema::example, null));

        return schema;
    }

    private io.swagger.v3.oas.annotations.media.Schema getReplacementSchema(AnnotatedType target) {
        return replacements.get(getTargetClass(target)).getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static String getSchemaProperty(Optional<io.swagger.v3.oas.annotations.media.Schema> targetSchema,
                                            io.swagger.v3.oas.annotations.media.Schema replacementSchema,
                                            Function<io.swagger.v3.oas.annotations.media.Schema, String> schemaPropertyGetter,
                                            String defaultValue) {
        return targetSchema.map(schemaPropertyGetter)
                .filter(StringUtils::isNotEmpty)
                .orElseGet(() -> Optional.ofNullable(replacementSchema).map(schemaPropertyGetter).filter(StringUtils::isNotEmpty).orElse(defaultValue));
    }

    public static class Builder {

        private final Map<Class<?>, Class<?>> replacements = new HashMap<>();

        public Builder replace(Class<?> targetClass, Class<?> replacementClass) {
            replacements.put(targetClass, replacementClass);
            return this;
        }

        public SchemaReplacer build() {
            return new SchemaReplacer(replacements);
        }
    }
}
