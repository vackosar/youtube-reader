package com.vackosar.ytcapsdowner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private Punctuator punctuator;
    private GraphExecutor graphExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) getWindow().findViewById(R.id.url);
        editText.setText("");
        final Button button = (Button) getWindow().findViewById(R.id.displayButton);
        button.setOnClickListener(this);

        final Intent intent = getIntent();
        final String action = intent.getAction();

        graphExecutor = new GraphExecutor(getAssets());
        WordIndex wordIndex = new WordIndex(getAssets());
        SamplePunctuator samplePunctuator = new SamplePunctuator(wordIndex, graphExecutor);
        Sampler sampler = new Sampler();
        punctuator = new Punctuator(sampler, samplePunctuator);

        if (Intent.ACTION_VIEW.equals(action)) {
            String url = intent.getData().toString();
            if (url != null) {
                final TextView textView = (TextView) getWindow().findViewById(R.id.captionText);
                editText.setText(url);
                new CapsDownloader(textView, punctuator).execute(url);
            }
        }
    }

    @Override
    public void onClick(View view) {
        final TextView textView = (TextView) getWindow().findViewById(R.id.captionText);
        final EditText editText = (EditText) getWindow().findViewById(R.id.url);
        String url = editText.getText().toString();
        new CapsDownloader(textView, punctuator).execute(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        graphExecutor.close();
    }
}
