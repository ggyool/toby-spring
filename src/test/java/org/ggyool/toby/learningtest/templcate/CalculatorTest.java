package org.ggyool.toby.learningtest.templcate;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.ggyool.toby.learningtest.Calculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    @DisplayName("txt파일의 숫자들의 합을 계산한다.")
    @Test
    void sumOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        String path = getClass().getResource("/numbers.txt").getPath();
        int sum = calculator.calcSum(path);
        assertThat(sum).isEqualTo(10);
    }

    @DisplayName("txt파일의 숫자들의 곱을 계산한다.")
    @Test
    void productOfNumbers() throws IOException {
        Calculator calculator = new Calculator();
        String path = getClass().getResource("/numbers.txt").getPath();
        int sum = calculator.calcProduct(path);
        assertThat(sum).isEqualTo(0);
    }
}
