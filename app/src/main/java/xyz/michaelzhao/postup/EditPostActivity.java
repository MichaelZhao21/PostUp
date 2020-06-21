package xyz.michaelzhao.postup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoto();
            }
        });

        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    protected void save() {
        // Get file
        File file = new File(getApplicationContext().getFilesDir(), "save.json");

        // Create json array of saved posts
        JSONArray savedPostsArray = new JSONArray();

        // Get text from the text box
        EditText editText = findViewById(R.id.textDisplay);
        String text = editText.getText().toString();

        // Create object for this post
        JSONObject currSave = new JSONObject();

        name = "IM A DUMMY THICC";
        try {
            // Put the strings into the object
            currSave.put("name", name);
            currSave.put("text", text);
            currSave.put("image", uri.toString());

            // Add the object to the array
            savedPostsArray.put(currSave);

            // Write to file
            FileOutputStream fs = getApplicationContext().openFileOutput(SAVES_FILE_NAME, MODE_PRIVATE);
            fs.write(savedPostsArray.toString().getBytes());
            fs.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        load();

    }

    protected void load() {
        try {
            // Get input file stream
            FileInputStream fis = getApplicationContext().openFileInput(SAVES_FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

            // Get stringbuilder and buffered reader
            StringBuilder sb = new StringBuilder();
            BufferedReader f = new BufferedReader(inputStreamReader);

            // Read file to string
            String line = f.readLine();
            while (line != null) {
                sb.append(line).append('\n');
                line = f.readLine();
            }

            JSONArray arr = new JSONArray(sb.toString());
            JSONObject obj = arr.getJSONObject(0);
            System.out.println(obj.toString());
            System.out.println(obj.get("name").toString());
            System.out.println(obj.get("text").toString());
            System.out.println(Uri.parse(obj.get("image").toString()));


        } catch (IOException | JSONException e) {
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
                try {
                    imageView.setImageBitmap(getBitmapFromUri(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}