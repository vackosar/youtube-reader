package com.vackosar.ytcapsdowner;

import android.widget.TextView;

public class CapsPunctuator {

    private final CapsDownloader capsDownloader;
    private final Punctuator punctuator;
    private final TextView textView;

    public CapsPunctuator(
            final TextView textView,
            final CapsDownloader capsDownloader,
            final Punctuator punctuator) {
        this.capsDownloader = capsDownloader;
        this.punctuator = punctuator;
        this.textView = textView;
    }

    public void punctuate(String url) {
        try {
            String text = capsDownloader.execute(url).get();
            if (text.matches(".*[.!?,].*")) {
                textView.setText(text);
            } else {
                textView.setText(punctuator.punctuate(text));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
