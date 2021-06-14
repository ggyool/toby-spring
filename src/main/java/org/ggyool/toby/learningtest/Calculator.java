package org.ggyool.toby.learningtest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Calculator {

    public int calcSum(String path) throws IOException {
        return calculateContext(path, (br) -> {
            int sum = 0;
            String line;
            while (Objects.nonNull(line = br.readLine())) {
                sum += Integer.parseInt(line);
            }
            return sum;
        });
    }

    public int calcProduct(String path) throws IOException {
        return calculateContext(path, (br) -> {
            String line = line = br.readLine();
            if (Objects.isNull(line)) {
                return 0;
            }
            int res = 1;
            while (Objects.nonNull(line)) {
                res *= Integer.parseInt(line);
                line = br.readLine();
            }
            return res;
        });
    }

    private int calculateContext(String path, CalculateStrategy calculateStrategy) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return calculateStrategy.run(br);
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
}
