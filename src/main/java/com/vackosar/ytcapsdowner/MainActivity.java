package com.vackosar.ytcapsdowner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private GraphExecutor graphExecutor;
    private CapsPunctuator capsPunctuator;

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
        Punctuator punctuator = new Punctuator(sampler, samplePunctuator);
        CapsDownloader capsDownloader = new CapsDownloader();
        capsPunctuator = new CapsPunctuator(getCaptionText(), capsDownloader, punctuator);

        if (Intent.ACTION_VIEW.equals(action)) {
            String url = intent.getData().toString();
            if (url != null) {
                editText.setText(url);
                capsPunctuator.punctuate(url);
            }
        }
    }

    @Override
    public void onClick(View view) {
        final EditText editText = (EditText) getWindow().findViewById(R.id.url);
        String url = editText.getText().toString();
        capsPunctuator.punctuate(url);
    }

    private TextView getCaptionText() {
        return (TextView) getWindow().findViewById(R.id.captionText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        graphExecutor.close();
    }


}
