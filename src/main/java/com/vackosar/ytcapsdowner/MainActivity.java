package com.vackosar.ytcapsdowner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private GraphExecutor graphExecutor;
    private Sampler sampler;
    private SamplePunctuator samplePunctuator;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void init() {
        final Button button = (Button) getWindow().findViewById(R.id.displayButton);
        button.setOnClickListener(this);

        graphExecutor = new GraphExecutor(getAssets());
        WordIndex wordIndex = new WordIndex(getAssets());
        samplePunctuator = new SamplePunctuator(wordIndex, graphExecutor);
        sampler = new Sampler();
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
            Handler handler = new Handler();
            new CapsDisplayer(getCaptionText(), sampler, samplePunctuator, shareActionProvider, handler).execute(url);
        }
    }

    private TextView getCaptionText() {
        return (TextView) getWindow().findViewById(R.id.captionTextView);
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
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        init();
        return true;
    }

}
