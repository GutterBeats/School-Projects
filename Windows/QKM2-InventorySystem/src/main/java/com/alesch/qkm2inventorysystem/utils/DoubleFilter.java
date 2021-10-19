/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.utils;

import java.util.regex.Pattern;

public class DoubleFilter extends Filter {

    @Override
    protected Pattern getFilterPattern() {
        return Pattern.compile("\\d*([.]\\d*)?");
    }
}
