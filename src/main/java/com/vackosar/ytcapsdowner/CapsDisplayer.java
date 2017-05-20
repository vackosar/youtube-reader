package com.vackosar.ytcapsdowner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapsDisplayer extends AsyncTask<String, Void, Void> {

    private static final String PUNCTION = ".*[.!?,].*";
    private final TextView captionTextView;
    private final ShareActionProvider shareActionProvider;
    private final SamplePunctuator samplePunctuator;
    private final Sampler sampler;
    private final Handler handler;

    CapsDisplayer(TextView captionTextView, Sampler sampler, SamplePunctuator samplePunctuator, ShareActionProvider shareActionProvider, Handler handler) {
        this.shareActionProvider = shareActionProvider;
        this.captionTextView = captionTextView;
        this.sampler = sampler;
        this.samplePunctuator = samplePunctuator;
        this.handler = handler;
    }

    private void punctuate(String url) {
        try {
            setText("Downloading and extracting subtitles from: " + url + " ...");
            CapsDownloader.Result caps = new CapsDownloader().download(url);
            if (punctuated(caps.text)) {
                setText(join(caps.title, caps.text));
            } else {
                setText("Generating punctuation for the caps ...");
                String text = new Punctuator(sampler, samplePunctuator).punctuate(caps.text);
                setText(join(caps.title, text));
            }
        } catch (Exception e) {
            setText(ExceptionUtils.getRootCauseMessage(e).replaceAll("^.*?Exception: ", ""));
        }
    }

    private String join(String title, String text) {
        return title.toUpperCase() + "\n\n" + text;
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

    private void setText(final String text) {
        handler.post(new Runnable() {@Override public void run() {captionTextView.setText(text);}});
        setShareIntent(text);
    }

    private void setShareIntent(String text) {
        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        handler.post(new Runnable() {@Override public void run() {shareActionProvider.setShareIntent(sendIntent);}});
    }

    @Override
    protected Void doInBackground(String... params) {
        punctuate(params[0]);
        return null;
    }

}
