/*
 * Copyright (c) 2021 Anthony Chavez.
 */

package com.alesch.qkm2inventorysystem.utils;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public abstract class Filter implements UnaryOperator<TextFormatter.Change> {

    protected abstract Pattern getFilterPattern();

    @Override
    public TextFormatter.Change apply(TextFormatter.Change aT) {
        return getFilterPattern().matcher(aT.getText()).matches() ? aT : null;
    }
}
