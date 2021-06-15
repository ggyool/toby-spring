package org.ggyool.toby.learningtest.templcate;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.ggyool.toby.learningtest.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        int sum = calculator.calcIntegerSum(path);
        assertThat(sum).isEqualTo(10);
    }

    @DisplayName("txt파일이 소수의 합을 구한다.")
    @Test
    void sumOfDoubleNumbers() throws IOException {
        String path = getClass().getResource("/double_numbers.txt").getPath();

        double sum = calculator.calcDoubleSum(path);
        assertThat(sum).isEqualTo(10.6);
    }

    @DisplayName("txt파일의 문자열을 합친다.")
    @Test
    void con() throws IOException {
        String path = getClass().getResource("/numbers.txt").getPath();

        String res = calculator.concatenate(path);
        assertThat(res).isEqualTo("1234");
    }

    @DisplayName("txt파일의 숫자들의 곱을 계산한다.")
    @Test
    void productOfNumbers() throws IOException {
        String path = getClass().getResource("/numbers.txt").getPath();
        int mul = calculator.calcProduct(path);
        assertThat(mul).isEqualTo(24);
    }

    @DisplayName("예외 - txt파일이 비어있으면 결과는 null이 나온다.")
    @Test
    void productOfEmptyNumbers() throws IOException {
        String path = getClass().getResource("/empty.txt").getPath();

        Integer sum = calculator.calcIntegerSum(path);
        assertThat(sum).isNull();
        Integer mul = calculator.calcProduct(path);
        assertThat(mul).isNull();
    }

}
