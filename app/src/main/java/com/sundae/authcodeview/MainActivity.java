package com.sundae.authcodeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "MainActivity";

    private AuthCodeView authCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authCodeView = (AuthCodeView) findViewById(R.id.authcode);

        authCodeView.setAuthCodeTextChangeListener(new AuthCodeView.AuthCodeTextChangeListener() {
            @Override
            public void onInput(char code) {
                Log.e(TAG , "onInput " + code );
            }

            @Override
            public void onDelete() {
                Log.e(TAG , "onDelete " );
            }

            @Override
            public void onInputFinish(String code) {
                Log.e(TAG , "onInputFinish " + code );
            }
        });

    }
}
