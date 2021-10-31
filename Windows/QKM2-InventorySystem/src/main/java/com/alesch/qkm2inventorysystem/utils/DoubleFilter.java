/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.utils;

import java.util.regex.Pattern;

/**
 * Concrete filter class used to limit text field input to Doubles.
 */
public class DoubleFilter extends Filter {

    /**
     * Used to get a Regex Pattern used to limit the input
     * of text fields to Doubles.
     *
     * @return a Regex Pattern that matches Double input.
     */
    @Override
    protected Pattern getFilterPattern() {
        return Pattern.compile("\\d*([.]\\d*)?");
    }
}
