package com.vackosar.ytcapsdowner;

import android.widget.TextView;

public class CapsPunctuator {

    private static final String PUNCTUATED = ".*[.!?,].*";
    private final Punctuator punctuator;
    private final TextView textView;

    public CapsPunctuator(final TextView textView, final Punctuator punctuator) {
        this.punctuator = punctuator;
        this.textView = textView;
    }

    public void punctuate(String url) {
        try {
            String text = new CapsDownloader().execute(url).get();
            if (text.matches(PUNCTUATED)) {
                textView.setText(text);
            } else {
                textView.setText(punctuator.punctuate(text));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
