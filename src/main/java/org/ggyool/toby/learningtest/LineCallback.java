package org.ggyool.toby.learningtest;

public interface LineCallback<T> {

    T run(String line, T value);
}
