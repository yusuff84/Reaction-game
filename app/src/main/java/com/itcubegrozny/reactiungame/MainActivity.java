package com.itcubegrozny.reactiungame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class    MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * ставим слушателя на кнопку
         * через Intent мы сообщаем системе, что хотим переключить экран (активити)
         * метод startActivity() уже переключает нас на другое активити
         */
        Button button_start = findViewById(R.id.button_start);
        button_start.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Game.class);
            startActivity(intent);
            finish();
        });

        // делаем активити полноэкранным
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}