package com.example.neettimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UpdateCheckActivity extends AppCompatActivity {

    private static final String VERSION_URL = "https://github.com/namankumarmahto/Files/blob/main/neet_timer/json/version-info.json";
    private static final int CURRENT_VERSION_CODE = 1; // Replace with your current version code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_check); // Ensure to have a separate layout for this

        // Check for updates
        new CheckForUpdateTask().execute(VERSION_URL);
    }

    private class CheckForUpdateTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String versionInfoUrl = strings[0];
            try {
                URL url = new URL(versionInfoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                if (scanner.hasNext()) {
                    return scanner.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int latestVersionCode = jsonObject.getInt("versionCode");
                    String apkUrl = jsonObject.getString("apkUrl");
                    String changelog = jsonObject.getString("changelog");

                    if (latestVersionCode > CURRENT_VERSION_CODE) {
                        showUpdateDialog(apkUrl, changelog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showUpdateDialog(String apkUrl, String changelog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCheckActivity.this);
        builder.setTitle("Update Available");
        builder.setMessage("A new version of the app is available.\n\nChangelog:\n" + changelog);

        // Update button
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(apkUrl));
                startActivity(browserIntent);
            }
        });

        // Cancel button
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
