package com.app.simteam.rollingnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.simteam.rollingnews.R;

public class AboutActivity extends Activity {
    Button btnEmail;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        btnEmail = (Button) findViewById(R.id.btnEmail);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnEmail.setOnClickListener(listner);
        btnSubmit.setOnClickListener(listner);
    }

    public View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == btnEmail){
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","simteam8@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Please update application as my favor");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Simply Dev Team,\n\nPlease update web list with my favorites as following:\n");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } else if(v == btnSubmit){
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        }
    };
}
