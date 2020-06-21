package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class PostPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_post);

        // Back Button
        ImageButton exit = findViewById(R.id.backSharePost);
        exit.setOnClickListener(v -> back());
    }

    protected void back() {
        Intent intent = new Intent(this, ViewPostActivity.class);
        startActivity(intent);
    }
}