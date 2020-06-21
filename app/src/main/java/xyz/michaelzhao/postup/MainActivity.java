package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load data
        Global.data = Util.load(getApplicationContext(), Global.SAVES_FILE_NAME);

        Button newButton = (Button) findViewById(R.id.NewButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });

        ImageButton settingsButton = (ImageButton) findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity("settings");
            }
        });

        Button draftsButton = (Button) findViewById(R.id.SavedPostsButton);
        draftsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity("saved");
            }
        });

    }

    protected void createPost() {
        DialogFragment addName = new NameDialog();
        addName.show(getSupportFragmentManager(), "addName");
    }

    protected void newActivity(String option) {
        Intent intent;
        if (option.equals("saved"))
            intent = new Intent(this, SavedPostsActivity.class);
        else if (option.equals("settings"))
                intent = new Intent(this, SettingsActivity.class);
        else
            return;
        startActivity(intent);
    }
}