package xyz.michaelzhao.postup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
        // TODO: write data to save file
        Log.d("tag","message");
        //File file = new File(getApplicationContext().getFilesDir(), "save.json");
        JSONArray listsaves = new JSONArray();
        /*try(FileReader file = new FileReader("save.json");) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(file);
            listsaves = (JSONArray) obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        EditText editText = findViewById(R.id.textDisplay);
        String text = editText.getText().toString();
        JSONObject newsave = new JSONObject();
        load();
        try {
            newsave.put("name:", name);
            newsave.put("text:", text);
            newsave.put("image:", uri);
            listsaves.put(newsave);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fileWriter = new FileWriter("save.json");
            fileWriter.write(listsaves.toString());
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(newsave);
        /*for(int i = 0; i < listsaves.length(); i++) {
            try {
                System.out.println(listsaves.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    protected void load() {
        try {
            FileReader file = new FileReader("save.json");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(file);
            JSONArray saved = (JSONArray) obj;

            for (int i = 0; i < saved.length(); i++) {
                JSONObject entry = (JSONObject) saved.get(i);
                String name = (String) entry.get("name");
                String text = (String) entry.get("text");
                String job = (String) entry.get("job");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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