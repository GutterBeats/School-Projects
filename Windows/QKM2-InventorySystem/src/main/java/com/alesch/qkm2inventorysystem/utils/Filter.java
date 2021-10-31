/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.utils;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Abstract class used to create filters to apply to TextFields.
 */
public abstract class Filter implements UnaryOperator<TextFormatter.Change> {

    /**
     * Pattern to use when filtering changes.
     * @return regex pattern.
     */
    protected abstract Pattern getFilterPattern();

    @Override
    public TextFormatter.Change apply(TextFormatter.Change aT) {
        return getFilterPattern().matcher(aT.getText()).matches() ? aT : null;
    }
}
