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
            CapsDownloader.Result result = new CapsDownloader().execute(url).get();
            if (result.result != null) {
                String text = result.result;
                if (text.matches(PUNCTUATED)) {
                    textView.setText(text);
                } else {
                    textView.setText(punctuator.punctuate(text));
                }
            } else {
                throw new RuntimeException(result.exception);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
