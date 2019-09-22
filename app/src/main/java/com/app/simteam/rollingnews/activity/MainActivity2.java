package com.app.simteam.rollingnews.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.adapter2.WebCardView;
import com.app.simteam.rollingnews.adapter2.WebCardViewAdapter;
import com.app.simteam.rollingnews.constant.Constant;
import com.app.simteam.rollingnews.data.CategoryItemData;
import com.app.simteam.rollingnews.data.WebItemData;
import com.app.simteam.rollingnews.service.ScrollingService;
import com.app.simteam.rollingnews.utils.DataMaker;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity2 extends AppCompatActivity {

    public static ArrayList<WebItemData> weblist;
    public static ArrayList<CategoryItemData> categorylist;
    private int webListSize;
    private int categoryListSize;
    private String tmpString;
    DataMaker dataMaker;
    private int listSize;
    private SharedPreferences sharedPreferences;
    private FloatingActionButton fab;
    private ArrayList<String> newfeed;
    public ArrayList<String> listAddress;
    private SharedPreferences.Editor e;
    ArrayList<WebCardView> webCardView;
    private int REQUEST_CODE_ASK_PERMISSIONS = 23;
    public static String REFRESH_DATA_INTENT = "UPDATE_FAB";
    private boolean mIsFloatingViewShow = false;
    private DataUpdateReceiver dataUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(Constant.KEY_SHARED, MODE_PRIVATE);

        listAddress = new ArrayList<>();

        sharedPreferences = getSharedPreferences(Constant.KEY_SHARED, MODE_PRIVATE);
        mIsFloatingViewShow = sharedPreferences.getBoolean("FLOATING_VIEW", false);
        e = sharedPreferences.edit();

        fill_web_data();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (isServiceRunning())
            fab.setImageResource(R.drawable.icon_stop_normal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (mIsFloatingViewShow)*/
                if (isServiceRunning()) {
                    hideFloatingView();
                    fab.setImageResource(R.drawable.icon_start_normal);
                } else {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(getApplicationContext(), "Please connect internet to down NEWS.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (updateSelectedItems()) {
                            fab.setImageResource(R.drawable.icon_stop_normal);
                            showFloatingView();
                        }
                    }

                }
            }
        });

        // checking permission on android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showConfirmDialog();
            }
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(MainActivity2.this)
                .setTitle("Accept permission")
                .setMessage("Application need you confirm a permission to run perfectly")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, REQUEST_CODE_ASK_PERMISSIONS);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    showConfirmDialog();
                }
            }
        }
    }

    public void fill_web_data() {

        weblist = new ArrayList<>();

        dataMaker = new DataMaker();
        try {
            weblist = dataMaker.setWebItems(getAssets().open("data.xml"));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listSize = weblist.size();

        if (sharedPreferences.contains(Constant.KEY_WEB)) {
            //get data from SharedPreferences
            if (listSize != 0) {
                for (int i = 0; i < listSize; i++) {
                    if (sharedPreferences.getInt("WEB_" + i, -1) == 1) {
                        weblist.get(i).setIsAdd(true);
                    } else {
                        weblist.get(i).setIsAdd(false);
                    }
                }
            }

        } else {
            // store list into SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constant.KEY_WEB, listSize);

            for (int i = 0; i < listSize; i++) {
                if (weblist.get(i).isAdded)
                    editor.putInt("WEB_" + i, 1);
                else
                    editor.putInt("WEB_" + i, 0);
            }
            editor.commit();
        }

        webCardView = new ArrayList<>();
        for (WebItemData item : weblist) {
            String name = item.title;
            String url = item.link;
            boolean isSelected = item.isAdded;
            String rss = item.link;

            String tmp = rss.replace("http://", "");
            StringTokenizer st = new StringTokenizer(tmp, "/");
            String home = "http://" + st.nextToken().toString();

            String uri = "drawable/" + item.image;

            int imageResource = this.getResources().getIdentifier(uri, null, this.getPackageName());
            Drawable icon = this.getResources().getDrawable(imageResource);
//            Log.d("TAN", "WEB: " + name + "; url: " + url + "; isSlected: " + isSelected + "; Drawable: " + icon);

            WebCardView webview = new WebCardView(name, url, isSelected, icon, rss, home);
            webCardView.add(webview);
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerviewGid);
        WebCardViewAdapter adapter = new WebCardViewAdapter(this, webCardView);
        GridLayoutManager grid = new GridLayoutManager(this, 2);
        rv.setLayoutManager(grid);
        rv.setHasFixedSize(false);
        rv.setAdapter(adapter);
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ScrollingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void showFloatingView() {
        Intent intent = new Intent(getApplication(), ScrollingService.class);
        intent.putStringArrayListExtra("data", newfeed);
        startService(intent);

    }

    private void hideFloatingView() {
        stopService(new Intent(getApplication(), ScrollingService.class));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean updateSelectedItems() {
        if (newfeed == null)
            newfeed = new ArrayList<String>();
        else
            newfeed.removeAll(newfeed);

        webListSize = weblist.size();
        if (listAddress.size() != 0) {
            listAddress.removeAll(listAddress);
        }
        for (int i = 0; i < webListSize; i++) {
            // checking selected web
            if (sharedPreferences.getInt("WEB_" + i, -1) == 1) {
                tmpString = weblist.get(i).getLink();
                listAddress.add(tmpString);
                Log.d("TAN", "WEB position: " + i);
            }
        }

        if (listAddress.size() == 0) {
            Toast.makeText(getApplicationContext(), "Please select at least a web to start Rolling NEWS", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            e.putInt("LIST_ADDRESS_SIZE", listAddress.size());
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : listAddress) {
                stringBuilder.append(str).append("#");
            }
            e.putString("LIST_ADDRESS", stringBuilder.toString());
            Log.d("TAN", "WEBs: " + stringBuilder.toString());
            e.commit();
            return true;
        }
    }

    @Override
    protected void onResume() {
        Log.d("TAN", "Come onResume");
        super.onResume();
        if (dataUpdateReceiver == null) dataUpdateReceiver = new DataUpdateReceiver();
        IntentFilter intentFilter = new IntentFilter(REFRESH_DATA_INTENT);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        Log.d("TAN", "Come onPause");
        super.onPause();
        if (dataUpdateReceiver != null)
            unregisterReceiver(dataUpdateReceiver);
    }

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TAN", "Come onReceive");
            if (intent.getAction().equals(REFRESH_DATA_INTENT)) {
                fab.setImageResource(R.drawable.icon_start_normal);
            }
        }
    }

}
