package com.app.simteam.rollingnews.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.constant.Constant;
import com.app.simteam.rollingnews.constant.TabType;
import com.app.simteam.rollingnews.adapter.ViewPagerAdapter;
import com.app.simteam.rollingnews.data.CategoryItemData;
import com.app.simteam.rollingnews.data.WebItemData;
import com.app.simteam.rollingnews.service.ScrollingService;
import com.app.simteam.rollingnews.utils.DataMaker;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public ArrayList<String> listAddress;
    public static ArrayList<WebItemData> weblist;
    public static ArrayList<CategoryItemData> categorylist;
    DataMaker dataMaker;
    private int listSize;
    private SharedPreferences sharedPreferences;
    private int webListSize;
    private int categoryListSize;
    private String tmpString;
    private SharedPreferences.Editor e;
    private ArrayList<String> newfeed;
    private boolean mIsFloatingViewShow = false;
    private int REQUEST_CODE_ASK_PERMISSIONS = 23;
    public static String REFRESH_DATA_INTENT = "UPDATE_FAB";
    private FloatingActionButton fab;
    private DataUpdateReceiver dataUpdateReceiver;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listAddress = new ArrayList<>();

        sharedPreferences = getSharedPreferences(Constant.KEY_SHARED, MODE_PRIVATE);
        mIsFloatingViewShow = sharedPreferences.getBoolean("FLOATING_VIEW", false);
        e = sharedPreferences.edit();

        fill_web_data();
        fill_category_data();

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
//                        mIsFloatingViewShow = true;
//                        e.putBoolean("FLOATING_VIEW", mIsFloatingViewShow);
//                        e.commit();
                            fab.setImageResource(R.drawable.icon_stop_normal);
                            showFloatingView();
                        }
                    }

                }
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // checking permission on android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showConfirmDialog();
            }
        }
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(MainActivity.this)
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

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.ripple_material_light), TabType.TAB_WEB), "Web NEWS");
        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.ripple_material_light), TabType.TAB_CATEGORY), "Category NEWS");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if (id == R.id.action_sync) {
            if (isServiceRunning()) {
                hideFloatingView();
                updateSelectedItems();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                showFloatingView();
            } else
                Toast.makeText(this, "RollingNEWS is not started. Please press start button!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
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

    }

    public void fill_category_data() {
        categorylist = new ArrayList<>();
        dataMaker = new DataMaker();

        if (Locale.getDefault().getLanguage().equals("vi")) {
            categorylist = dataMaker.setCategoryItems_vi();
        } else
            categorylist = dataMaker.setCategoryItems();


        listSize = categorylist.size();
        if (sharedPreferences.contains(Constant.KEY_CATEGORY)) {
            if (listSize != 0) {
                for (int i = 0; i < listSize; i++) {
                    if (sharedPreferences.getInt("CATEGORY_" + i, -1) == 1) {
                        categorylist.get(i).setCategoryIsAdded(true);
                    } else
                        categorylist.get(i).setCategoryIsAdded(false);
                }
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constant.KEY_CATEGORY, categorylist.size());
        for (int i = 0; i < categorylist.size(); i++) {
            if (categorylist.get(i).categoryIsAdded) {
                editor.putInt("CATEGORY_" + i, 1);
            } else
                editor.putInt("CATEGORY_" + i, 0);
        }
        editor.commit();
    }


    private void showFloatingView() {
//        Intent intent = new Intent(getApplication(), ScrollingService.class);
        Intent intent = new Intent(getApplication(), ScrollingService.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putStringArrayListExtra("data", newfeed);
        startService(intent);

    }

    private void hideFloatingView() {
//        stopService(new Intent(getApplication(), ScrollingService.class));
        stopService(new Intent(getApplication(), ScrollingService.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
        }
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

    private boolean updateSelectedItems() {
        if (newfeed == null)
            newfeed = new ArrayList<String>();
        else
            newfeed.removeAll(newfeed);

        webListSize = weblist.size();
        categoryListSize = categorylist.size();
        if (listAddress.size() != 0) {
            listAddress.removeAll(listAddress);
        }
        for (int i = 0; i < webListSize; i++) {
            // checking selected web
            if (sharedPreferences.getInt("WEB_" + i, -1) == 1) {
                tmpString = weblist.get(i).getLink();
                listAddress.add(tmpString);
                // checking selected category
                for (int j = 0; j < categoryListSize; j++) {
                    if (sharedPreferences.getInt("CATEGORY_" + j, -1) == 1) {
                        int tmpCategoryIndex = j;
                        switch (tmpCategoryIndex) {
                            case 0:
                                tmpString = weblist.get(i).getEconomics();
                                break;
                            case 1:
                                tmpString = weblist.get(i).getCulture();
                                break;
                            case 2:
                                tmpString = weblist.get(i).getPolytics();
                                break;
                            case 3:
                                tmpString = weblist.get(i).getSocial();
                                break;
                            case 4:
                                tmpString = weblist.get(i).getLaws();
                                break;
                            case 5:
                                tmpString = weblist.get(i).getEducation();
                                break;
                            case 6:
                                tmpString = weblist.get(i).getScience();
                                break;
                            case 7:
                                tmpString = weblist.get(i).getTechnology();
                                break;
                            case 8:
                                tmpString = weblist.get(i).getWorld();
                                break;
                            case 9:
                                tmpString = weblist.get(i).getSport();
                                break;
                        }
                        listAddress.add(tmpString);
                    }
                }
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
            e.commit();
            return true;
        }
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

    private class DataUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TAN", "Come onReceive");
            if (intent.getAction().equals(REFRESH_DATA_INTENT)) {
                fab.setImageResource(R.drawable.icon_start_normal);
            }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
