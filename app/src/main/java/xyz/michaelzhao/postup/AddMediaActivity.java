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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;

public class AddMediaActivity extends AppCompatActivity {

    public static final int CHOOSE_IMAGE = 1;
    private Bitmap image;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        EditText editText = (EditText) findViewById(R.id.textDisplayAdd);
        ImageView imageView = (ImageView) findViewById(R.id.pictureDisplayAdd);

        assert type != null;
        if (type.equals("image")) {
            imageView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhoto();
                }
            });
        }
        else {
            imageView.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
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
            Uri uri = null;
            if (data != null) {
                uri = data.getData();

                try {
                    image = getBitmapFromUri(uri);
                    ImageView iv = (ImageView) findViewById(R.id.pictureDisplay);
                    iv.setImageBitmap(image);
                    moveToCallback();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                TextView error = (TextView) findViewById(R.id.feedbackInfo);
                error.setText(R.string.image_load_error);
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

    protected void moveToCallback() {
        Intent intent = new Intent(this, EditPostActivity.class);
        intent.putExtra("type", type);

        if (type.equals("image")) {
            intent.putExtra("image", image);
        }
        else {
            EditText editText = (EditText) findViewById(R.id.textDisplayAdd);
            intent.putExtra("text", editText.getText().toString());
        }
        startActivity(intent);
    }
}