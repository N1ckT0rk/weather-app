package com.example.weatherapp;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TestMatchers {
    public static Matcher<CharSequence> matchesPattern(String regex) {
        return new TypeSafeMatcher<CharSequence>() {
            @Override
            protected boolean matchesSafely(CharSequence item) {
                return item != null && item.toString().matches(regex);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Text matches pattern: " + regex);
            }
        };
    }
}