package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class SavedPostsActivity extends AppCompatActivity {

    public static final String SAVES_FILE_NAME = "saves.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);

        // Load previous saved posts
        HashMap<String, PostData> loadedPosts = Util.load(getApplicationContext(), SAVES_FILE_NAME);

        // Array of the 9 image buttons we can use lmao
        ImageButton[] savedDraftButtons = {findViewById(R.id.imageButton1), findViewById(R.id.imageButton2),
                findViewById(R.id.imageButton3), findViewById(R.id.imageButton4),
                    findViewById(R.id.imageButton5), findViewById(R.id.imageButton6),
                        findViewById(R.id.imageButton7),findViewById(R.id.imageButton8),
                            findViewById(R.id.imageButton9)};

        int pos = 0;
        for(String key : loadedPosts.keySet()) {
//            try {
//                savedDraftButtons[pos].setImageBitmap(Util.getBitmapFromUri(loadedPosts.get(key).img, getContentResolver()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            pos++;
            if(pos == 9) {
                break;
            }
        }
        while(pos < 9) {
            savedDraftButtons[pos].setVisibility(View.GONE);
            pos++;
        }
    }
}