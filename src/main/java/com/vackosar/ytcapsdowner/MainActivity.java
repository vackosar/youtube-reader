package com.vackosar.ytcapsdowner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang.exception.ExceptionUtils;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private GraphExecutor graphExecutor;
    private CapsPunctuator capsPunctuator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void init(ShareActionProvider shareActionProvider) {
        final Button button = (Button) getWindow().findViewById(R.id.displayButton);
        button.setOnClickListener(this);

        graphExecutor = new GraphExecutor(getAssets());
        WordIndex wordIndex = new WordIndex(getAssets());
        SamplePunctuator samplePunctuator = new SamplePunctuator(wordIndex, graphExecutor);
        Sampler sampler = new Sampler();
        Punctuator punctuator = new Punctuator(sampler, samplePunctuator);
        capsPunctuator = new CapsPunctuator(getCaptionText(), punctuator, shareActionProvider);

        punctuate(loadUrl());
    }

    private String loadUrl() {
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            return intent.getData().toString();
        } else if (Intent.ACTION_SEND.equals(action)) {
            return intent.getStringExtra(Intent.EXTRA_TEXT);
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        final EditText editText = (EditText) getWindow().findViewById(R.id.url);
        String url = editText.getText().toString();
        punctuate(url);
    }

    private void punctuate(String url) {
        final EditText editText = (EditText) getWindow().findViewById(R.id.url);
        if (url == null) {
            editText.setText("");
        } else {
            editText.setText(url);
            try {
                capsPunctuator.punctuate(url);
            } catch (Exception e) {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(ExceptionUtils.getRootCauseMessage(e).replaceAll("^.*?Exception: ", ""))
                        .show();
            }
        }
    }

    private TextView getCaptionText() {
        return (TextView) getWindow().findViewById(R.id.captionText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        graphExecutor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        init(shareActionProvider);
        return true;
    }

}
