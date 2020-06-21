package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        // Back Button
        ImageButton exit = findViewById(R.id.backViewPost);
        exit.setOnClickListener(v -> back());

        // Post Button
        Button postButton = findViewById((R.id.toPostButton));
        postButton.setOnClickListener(v -> post());

        // Edit Button
        Button editButton = findViewById(R.id.editPostButton);
        editButton.setOnClickListener(v -> edit());

        // Image
        ImageView button = findViewById(R.id.pictureDisplayView);
        PostData data = Global.data.get(Global.keyToUseForCurrentSavedPost);
        button.setImageBitmap(data.img);

        // Text Display
        TextView text = findViewById(R.id.textDisplayView);
        text.setText(data.text);

        // Title
        TextView title = findViewById(R.id.viewPostTitle);
        title.setText(Global.keyToUseForCurrentSavedPost);
    }

    protected void back() {
        Intent intent = new Intent(this, SavedPostsActivity.class);
        startActivity(intent);
    }

    protected void post() {
        Intent intent = new Intent(this, PostPostActivity.class);
        startActivity(intent);
    }

    protected void edit() {
        Intent intent = new Intent(this, EditPostActivity.class);
        startActivity(intent);
    }
}