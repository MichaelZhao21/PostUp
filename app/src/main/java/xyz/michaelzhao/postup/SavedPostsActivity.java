package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class SavedPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);

        // Back Button
        ImageButton exit = findViewById(R.id.backSaved);
        exit.setOnClickListener(v -> back());

        // Array of the 9 image buttons we can use lmao
        ImageButton[] savedDraftButtons = {
                findViewById(R.id.imageButton), findViewById(R.id.imageButton2),
                findViewById(R.id.imageButton3), findViewById(R.id.imageButton4),
                findViewById(R.id.imageButton5), findViewById(R.id.imageButton6),
                findViewById(R.id.imageButton7),findViewById(R.id.imageButton8)};

        int pos = 0;
        for(String key : Global.data.keySet()) {
            if (pos == 9) break;
            PostData data = Global.data.get(key);
            ImageButton button = savedDraftButtons[pos++];
            button.setImageBitmap(data.img);
            button.setOnClickListener(v -> clickPic());
        }
    }

    protected void clickPic() {

    }

    protected void back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}