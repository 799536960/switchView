package com.duma.ld.switchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.duma.ld.mylibrary.SwitchView;

public class MainActivity extends AppCompatActivity {

    private SwitchView switchView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchView = (SwitchView) findViewById(R.id.switchview);
        button = (Button) findViewById(R.id.button);
        switchView.setOnClickCheckedListener(new SwitchView.onClickCheckedListener() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this, "type:" + switchView.isChecked(), Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchView.setChecked(false);
            }
        });
    }
}
