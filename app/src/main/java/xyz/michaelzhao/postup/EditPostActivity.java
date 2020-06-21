package xyz.michaelzhao.postup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditPostActivity extends AppCompatActivity {

    public static final int CHOOSE_IMAGE = 1;
    public Uri uri;
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        TextView title = findViewById(R.id.editPostTitle);
        title.setText(name);

        ImageView imageView = findViewById(R.id.pictureDisplay);
        imageView.setOnClickListener(v -> openPhoto());

        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(v -> save());
    }

    protected void save() {

        // Force keyboard die
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Get file
        File file = new File(getApplicationContext().getFilesDir(), Global.SAVES_FILE_NAME);

        // Create json array of saved posts from HashMap
        JSONArray savedPostsArray = new JSONArray();
        for(String key : Global.data.keySet()) {
            savedPostsArray.put(PostData.PostDataToJsonObject(Global.data.get(key)));
        }

        // Get text from the text box
        EditText editText = findViewById(R.id.textDisplay);
        String text = editText.getText().toString();

        // Create object for this post
        JSONObject currSave = PostData.PostDataToJsonObject(new PostData(name, text, Util.getBitmapFromUri(uri, getContentResolver())));

        // If the name already exists in another saved post, sucks lmfao
        if(Global.data.containsKey(name)) {
            System.out.println("That name already exists");
            return;
        }

        try {

            // Add the object to the array
            savedPostsArray.put(currSave);

            // Write to file
            FileOutputStream fs = getApplicationContext().openFileOutput(Global.SAVES_FILE_NAME, MODE_PRIVATE);
            fs.write(savedPostsArray.toString().getBytes());
            fs.close();

            PostData data = PostData.JsonObjectToPostData(currSave);
            Global.data.put(data.name, data);

            Intent intent = new Intent(this, SavedPostsActivity.class);
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void openPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
//                uri = data.getClipData().getItemAt(1).getUri(); TODO: add support to get and display multiple images
                uri = data.getData();
                ImageView imageView = findViewById(R.id.pictureDisplay);
                imageView.setImageBitmap(Util.getBitmapFromUri(uri, getContentResolver()));
            }
        }
    }
}