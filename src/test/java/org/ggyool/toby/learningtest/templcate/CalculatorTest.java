package org.ggyool.toby.learningtest.templcate;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.ggyool.toby.learningtest.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @DisplayName("txt파일의 숫자들의 합을 계산한다.")
    @Test
    void sumOfNumbers() throws IOException {
        String path = getClass().getResource("/numbers.txt").getPath();
        int sum = calculator.calcSum(path);
        assertThat(sum).isEqualTo(10);
    }

    @DisplayName("txt파일의 숫자들의 곱을 계산한다.")
    @Test
    void productOfNumbers() throws IOException {
        String path = getClass().getResource("/numbers.txt").getPath();
        int mul = calculator.calcProduct(path);
        assertThat(mul).isEqualTo(24);
    }

    @DisplayName("txt파일이 비어있으면 결과는 0이 나온다.")
    @Test
    void productOfEmptyNumbers() throws IOException {
        String path = getClass().getResource("/empty.txt").getPath();

        int sum = calculator.calcSum(path);
        assertThat(sum).isEqualTo(0);
        int mul = calculator.calcProduct(path);
        assertThat(mul).isEqualTo(0);
    }
}
