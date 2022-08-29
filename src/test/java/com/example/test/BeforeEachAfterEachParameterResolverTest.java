package com.example.test;

import com.example.resolver.BeforeEachAfterEachParameterResolver;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Slf4j
@ExtendWith(BeforeEachAfterEachParameterResolver.class)
class BeforeEachAfterEachParameterResolverTest {

    private TestEnum testParam;

    @BeforeEach
    void setUp(TestEnum param) {
        log.info("Captured param -> {}", param);
        testParam = param;
    }

    @ParameterizedTest
    @EnumSource(TestEnum.class)
    void test(TestEnum param) {
        Assertions.assertThat(param).isEqualTo(testParam);
    }

    enum TestEnum {
        FIRST_PARAMETER,
        SECOND_PARAMETER
    }

}