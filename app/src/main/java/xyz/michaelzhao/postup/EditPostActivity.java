package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class EditPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("both")) {

        }
        else if (type.equals("image")) {
            Uri uri = Uri.parse(intent.getStringExtra("uri"));
            ImageView view = (ImageView) findViewById(R.id.pictureDisplay);
            try {
                view.setImageBitmap(getBitmapFromUri(uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (type.equals("text")) {
            String text = intent.getStringExtra("text");
            EditText editText = (EditText) findViewById(R.id.textDisplay);
            editText.setText(text);
        }
        else {
            Log.e("EditPostActivityIntent", "No type stored in extra \"type\"");
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