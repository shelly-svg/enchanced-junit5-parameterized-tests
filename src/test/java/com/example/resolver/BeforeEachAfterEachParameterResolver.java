package com.example.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.engine.execution.BeforeEachMethodAdapter;
import org.junit.jupiter.engine.extension.ExtensionRegistry;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class BeforeEachAfterEachParameterResolver implements BeforeEachMethodAdapter, ParameterResolver {

    private ParameterResolver parameterizedTestParameterResolver;

    @Override
    public void invokeBeforeEachMethod(ExtensionContext context, ExtensionRegistry registry) {
        registry.getExtensions(ParameterResolver.class)
            .stream()
            .filter(parameterResolver -> parameterResolver.getClass()
                .getName()
                .contains("ParameterizedTestParameterResolver"))
            .findFirst()
            .ifPresentOrElse(
                parameterResolver -> parameterizedTestParameterResolver = parameterResolver,
                () -> {
                    throw new IllegalStateException("ParameterizedTestParameterResolver is missing in the registry");
                }
            );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        if (isExecutedOnAfterOrBeforeMethod(parameterContext)) {
            var mappedParameterContext = getMappedContext(parameterContext, extensionContext);
            return parameterizedTestParameterResolver.supportsParameter(mappedParameterContext, extensionContext);
        }
        return false;
    }

    private boolean isExecutedOnAfterOrBeforeMethod(ParameterContext parameterContext) {
        return Arrays.stream(parameterContext.getDeclaringExecutable().getDeclaredAnnotations())
            .anyMatch(this::isAfterEachOrBeforeEachAnnotation);
    }

    private boolean isAfterEachOrBeforeEachAnnotation(Annotation annotation) {
        return annotation.annotationType() == BeforeEach.class || annotation.annotationType() == AfterEach.class;
    }

    private MappedParameterContext getMappedContext(ParameterContext parameterContext,
        ExtensionContext extensionContext) {
        return new MappedParameterContext(
            parameterContext.getIndex(),
            extensionContext.getRequiredTestMethod().getParameters()[parameterContext.getIndex()],
            parameterContext.getTarget());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return parameterizedTestParameterResolver.resolveParameter(getMappedContext(parameterContext, extensionContext),
            extensionContext);
    }

}