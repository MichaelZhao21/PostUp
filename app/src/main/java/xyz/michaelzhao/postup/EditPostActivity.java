package xyz.michaelzhao.postup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {

    public static final int CHOOSE_IMAGE = 1;
    public static final String SAVES_FILE_NAME = "saves.json";
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

        Button suggestButton = findViewById(R.id.suggest);
        suggestButton.setOnClickListener(v -> suggest());
    }

    protected void save() {
        // Load previous saved posts
        HashMap<String, PostData> loadedPosts = Util.load(getApplicationContext(), SAVES_FILE_NAME);

        // Get file
        File file = new File(getApplicationContext().getFilesDir(), "save.json");

        // Create json array of saved posts from HashMap
        JSONArray savedPostsArray = new JSONArray();
        for(String key : loadedPosts.keySet()) {
            savedPostsArray.put(PostData.PostDataToJsonObject(loadedPosts.get(key)));
        }

        // Get text from the text box
        EditText editText = findViewById(R.id.textDisplay);
        String text = editText.getText().toString();

        name = "IM A DUMMY THICC";

        // Create object for this post
        JSONObject currSave = PostData.PostDataToJsonObject(new PostData(name, text, Util.getBitmapFromUri(uri, getContentResolver())));

        // If the name already exists in another saved post, sucks lmfao
        if(loadedPosts.containsKey(name)) {
            System.out.println("That name already exists");
            return;
        }

        try {

            // Add the object to the array
            savedPostsArray.put(currSave);

            // Write to file
            FileOutputStream fs = getApplicationContext().openFileOutput(SAVES_FILE_NAME, MODE_PRIVATE);
            fs.write(savedPostsArray.toString().getBytes());
            fs.close();

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

    protected void suggest() {
        Log.d("bitmap", Util.bitmapToString(Util.getBitmapFromUri(uri, getContentResolver())));
//        String url = "https://api.michaelzhao.xyz/postup/vision";
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                response -> {
//                    Log.d("Response", response);
//                },
//                error -> {
//                    Log.d("Error", error.toString());
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                Bitmap map = Util.getBitmapFromUri(uri, getContentResolver());
//                params.put("image", Util.bitmapToString(map));
//                return params;
//            }
//        };
    }
}