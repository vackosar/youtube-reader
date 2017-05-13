package com.vackosar.ytcapsdowner;

import android.content.Intent;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapsPunctuator {

    private static final String PUNCTION = ".*[.!?,].*";
    private final Punctuator punctuator;
    private final TextView textView;
    private final ShareActionProvider shareActionProvider;

    public CapsPunctuator(final TextView textView, final Punctuator punctuator, ShareActionProvider shareActionProvider) {
        this.punctuator = punctuator;
        this.textView = textView;
        this.shareActionProvider = shareActionProvider;
    }

    public void punctuate(String url) {
        try {
            CapsDownloader.Result result = new CapsDownloader().execute(url).get();
            if (result.result != null) {
                String text = result.result;
                if (punctuated(text)) {
                    setText(text);
                } else {
                    setText(punctuator.punctuate(text));
                }
            } else {
                throw new RuntimeException(result.exception);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean punctuated(String text) {
        Pattern pattern = Pattern.compile(PUNCTION);
        Matcher matcher = pattern.matcher(text);
        int dotCount = 0;
        while (matcher.find()) {
            dotCount++;
        }
        return dotCount * 30 > text.length();
    }

    private void setText(String text) {
        textView.setText(text);
        setShareIntent(text);
    }

    private void setShareIntent(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        shareActionProvider.setShareIntent(sendIntent);
    }
}
