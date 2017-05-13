package com.vackosar.ytcapsdowner;

import android.widget.TextView;

public class CapsPunctuator {

    private final CapsDownloader capsDownloader;
    private final Punctuator punctuator;
    private final TextView textView;

    CapsPunctuator(
            final TextView textView,
            final CapsDownloader capsDownloader,
            final Punctuator punctuator) {
        this.capsDownloader = capsDownloader;
        this.punctuator = punctuator;
        this.textView = textView;
    }

    void punctuate(String url) {
        try {
            String text = capsDownloader.execute(url).get();
            String punctuated = punctuator.punctuate(text);
            textView.setText(punctuated);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
