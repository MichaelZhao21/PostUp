package xyz.michaelzhao.postup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

        Button suggestButton = findViewById(R.id.suggest);
        suggestButton.setOnClickListener(v -> suggest());
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

    protected void suggest() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.michaelzhao.xyz/postup/vision");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    con.setRequestProperty("Accept", "string");
                    con.setDoOutput(true);
                    con.setDoInput(true);

                    JSONObject object = new JSONObject();
                    object.put("image", Util.bitmapToSmallString(Util.getBitmapFromUri(uri, getContentResolver())));
                    String out = object.toString();

                    DataOutputStream os = new DataOutputStream(con.getOutputStream());
                    Log.i("JSON", String.valueOf(object.toString().length()));
                    os.writeBytes(object.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(con.getResponseCode()));
                    Log.i("MSG" , con.getResponseMessage());

                    BufferedReader f = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = f.readLine()) != null) {
                        response.append(line.trim());
                    }
                    JSONObject res = new JSONObject(response.toString());

                    String quote = ((JSONObject)((JSONArray)res.get("quotes")).get(0)).get("quote").toString();
                    runOnUiThread(() -> setSuggestion(quote, 1));

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    protected void setSuggestion(String suggestion, int num) {
        if (num != 1) return;
        TextView suggView = findViewById(R.id.suggestion1);
        suggView.setText(suggestion);
    }
}