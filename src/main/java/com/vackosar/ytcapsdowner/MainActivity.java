package com.vackosar.ytcapsdowner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

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

        if (Intent.ACTION_VIEW.equals(action)) {
            String url = intent.getData().toString();
            if (url != null) {
                final TextView textView = (TextView) getWindow().findViewById(R.id.captionText);
                editText.setText(url);
                new CapsDownloader(textView).execute(url);
            }
        }
        Punctuator punctuator = new Punctuator(getAssets());
        punctuator.punctuate(punctuator.SAMPLE);
        punctuator.close();
    }

    @Override
    public void onClick(View view) {
        final TextView textView = (TextView) getWindow().findViewById(R.id.captionText);
        final EditText editText = (EditText) getWindow().findViewById(R.id.url);
        String url = editText.getText().toString();
        new CapsDownloader(textView).execute(url);
    }
}
