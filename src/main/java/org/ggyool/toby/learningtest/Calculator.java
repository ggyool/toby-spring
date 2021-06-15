package org.ggyool.toby.learningtest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Calculator {

    public int calcSum(String path) throws IOException {
        return calculateContext(path, 0, (line, value) -> value + Integer.parseInt(line));
    }

    public int calcProduct(String path) throws IOException {
        return calculateContext(path, 1, (line, value) -> value * Integer.parseInt(line));
    }

    private int calculateContext(String path, int initialValue, LineCallback lineCallback) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine();
        if (Objects.isNull(line)) {
            return 0;
        }
        try {
            int res = initialValue;
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
