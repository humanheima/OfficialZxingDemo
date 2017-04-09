package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final int
            REQUEST_CODE = 14;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textResult = (TextView) findViewById(R.id.textResult);
    }

    public void startCapture(View view) {
        /**
         * 扫描出结果就返回
         */
        /*Intent intent = new Intent(MyCaptureActivity.ACTION);
        intent.putExtra(Intents.Scan.RESULT_DISPLAY_DURATION_MS, 0L);
        startActivityForResult(intent, REQUEST_CODE);*/
        /**
         * 扫描出结果继续扫描
         */
        Intent intent = new Intent(this, MyCaptureActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                String result = data.getStringExtra(Intents.Scan.RESULT);
                textResult.setText(result);
            }
        }

    }
}
