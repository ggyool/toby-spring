package org.ggyool.toby.learningtest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Calculator {

    public Integer calcIntegerSum(String path) throws IOException {
        LineCallback<Integer> lineCallback = (line, value) -> value + Integer.parseInt(line);
        return calculateContext(path, 0, lineCallback);
    }

    public Double calcDoubleSum(String path) throws IOException {
        LineCallback<Double> lineCallback = (line, value) -> value + Double.parseDouble(line);
        return calculateContext(path, 0.0, lineCallback);
    }

    public String concatenate(String path) throws IOException {
        LineCallback<String> lineCallback = (line, value) -> line + value;
        return calculateContext(path, "", lineCallback);
    }

    public Integer calcProduct(String path) throws IOException {
        LineCallback<Integer> lineCallback = (line, value) -> value * Integer.parseInt(line);
        return calculateContext(path, 1, lineCallback);
    }

    private <T> T calculateContext(String path, T initialValue, LineCallback<T> lineCallback) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine();
        if (Objects.isNull(line)) {
            return null;
        }
        try {
            T res = initialValue;
            while (Objects.nonNull(line)) {
                res = lineCallback.run(line, res);
                line = br.readLine();
            }
            return res;
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
}
