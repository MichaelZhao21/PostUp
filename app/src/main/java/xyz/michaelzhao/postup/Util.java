package xyz.michaelzhao.postup;

import android.content.Context;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
            for(int i = 0; i < arr.length(); i++) {
                PostData data = PostData.JsonObjectToPostData(arr.getJSONObject(i));
                postDataHashMap.put(data.name, data);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return postDataHashMap;
    }
}
