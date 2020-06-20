package xyz.michaelzhao.postup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

enum MenuOption {NEW, SAVED, SETTINGS}

public class MainActivity extends AppCompatActivity {
    //gurt wuz here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Mikey was here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newButton = (Button) findViewById(R.id.NewButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity(MenuOption.NEW);
            }
        });

        ImageButton settingsButton = (ImageButton) findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity(MenuOption.SETTINGS);
            }
        });

        Button draftsButton = (Button) findViewById(R.id.SavedPostsButton);
        draftsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity(MenuOption.SAVED);
            }
        });

    }

    protected void newActivity(MenuOption option) {
        Intent intent;
        switch (option) {
            case NEW:
                intent = new Intent(this, NewPostActivity.class);
                break;
            case SAVED:
                intent = new Intent(this, DraftsActivity.class);
                break;
            case SETTINGS:
                intent = new Intent(this, SettingsActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}