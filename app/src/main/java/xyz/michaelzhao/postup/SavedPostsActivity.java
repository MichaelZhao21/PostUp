package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class SavedPostsActivity extends AppCompatActivity {

    public static final String SAVES_FILE_NAME = "saves.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);

        // Back Button
        ImageButton exit = (ImageButton) findViewById(R.id.imageButton13);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        // Load previous saved posts
        HashMap<String, PostData> loadedPosts = Util.load(getApplicationContext(), SAVES_FILE_NAME);
        // Get file
        File file = new File(getApplicationContext().getFilesDir(), SAVES_FILE_NAME);

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
            Drawable icon;
            String imageUri = loadedPosts.get(key).img.toString();
            Log.w("imageURI", imageUri);
            Uri uri=Uri.parse(imageUri);

            InputStream is;
            try {
                is = this.getContentResolver().openInputStream( uri );
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 10;
                Bitmap preview_bitmap=BitmapFactory.decodeStream(is,null,options);

                icon = new BitmapDrawable(getResources(),preview_bitmap);

            } catch (FileNotFoundException e) {
                //set default image from the button
                icon = getResources().getDrawable(R.drawable.bottombutton);
            }

            savedDraftButtons[pos].setBackground(icon);
            savedDraftButtons[pos].setVisibility(View.INVISIBLE);
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

    protected void back() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}