package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Button openImage = (Button) findViewById(R.id.openImage);
        Button writeText = (Button) findViewById(R.id.writeText);
        openImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen("image");
            }
        });
        writeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen("text");
            }
        });
    }

    protected void nextScreen(String type) {
        Intent intent = new Intent(this, AddMediaActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}