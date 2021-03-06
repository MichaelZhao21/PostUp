package xyz.michaelzhao.postup;

import android.graphics.Bitmap;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

public class PostData {

    public String name;
    public String text;
    public Bitmap img;

    public PostData() {
        this.name = "";
        this.text = "";
        this.img = null;
    }

    public PostData(String name, String text, Bitmap img) {
        this.name = name;
        this.text = text;
        this.img = img;
    }

    public static PostData JsonObjectToPostData(JSONObject job) {
        PostData data = new PostData();
        if (job != null) {
            try {
                data.name = job.get("name").toString();
                data.text = job.get("text").toString();
                data.img = Util.stringToBitmap(job.get("img").toString());
                return data;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject PostDataToJsonObject(PostData data) {
        try {
            JSONObject object = new JSONObject();
            object.put("name", data.name);
            object.put("text", data.text);
            object.put("img", Util.bitmapToString(data.img));
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}