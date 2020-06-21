package xyz.michaelzhao.postup;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Util {
    protected static HashMap<String, PostData> load(Context context, String saveFileName) {
        // <name, postdata object> to store each saved post
        HashMap<String, PostData> postDataHashMap = new HashMap<>();

        try {
            // Get input file stream
            FileInputStream fis = context.openFileInput(saveFileName);
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
            // Loop through the JSONArray and add to the map
            for (int i = 0; i < arr.length(); i++) {
                PostData data = PostData.JsonObjectToPostData(arr.getJSONObject(i));
                postDataHashMap.put(data.name, data);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return postDataHashMap;
    }

    protected static Bitmap getBitmapFromUri(Uri uri, ContentResolver contentResolver) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    contentResolver.openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String string) {
        try {
            byte[] bytes = Base64.decode(string, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
