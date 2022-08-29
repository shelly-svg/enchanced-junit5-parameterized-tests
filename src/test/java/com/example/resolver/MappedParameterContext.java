package com.example.resolver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.platform.commons.util.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class MappedParameterContext implements ParameterContext {

    private final int index;
    private final Parameter parameter;
    private final Object target;

    @Override
    public Optional<Object> getTarget() {
        return Optional.of(target);
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return AnnotationUtils.isAnnotated(parameter, annotationType);
    }

    @Override
    public <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationType) {
        return AnnotationUtils.findAnnotation(parameter, annotationType);
    }

    @Override
    public <A extends Annotation> List<A> findRepeatableAnnotations(Class<A> annotationType) {
        return AnnotationUtils.findRepeatableAnnotations(parameter, annotationType);
    }

}