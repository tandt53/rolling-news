package com.app.simteam.rollingnews.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.activity.MainActivity;
import com.app.simteam.rollingnews.adapter2.NewsRollingItem;
import com.app.simteam.rollingnews.constant.Constant;
import com.app.simteam.rollingnews.data.RSSFeed;
import com.app.simteam.rollingnews.data.RSSFeedParser;
import com.app.simteam.rollingnews.utils.DataMaker;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sim on 4/18/2016.
 */
public class ScrollingService extends Service {
    private WindowManager mWindowManager;
    private LinearLayout mLinearLayout;
    private Context context;
    private View mView;

    private RelativeLayout waitLayout;
    private ProgressBar progressBar;

    private LinearLayout horizontalOuterLayout;
    private HorizontalScrollView horizontalScrollview;
    private LinearLayout layoutBackground;
    private int scrollMax;
    private int scrollPos = 0;
    private TimerTask scrollerSchedule;
    private Timer scrollTimer = null;
    private static ArrayList<NewsRollingItem> listNews;
    private static ArrayList<String> feed;
    private static ArrayList<String> feedLink;
    private static ArrayList<String> feedSub;
    private static ArrayList<Integer> feedIndex;
    private int width;
    private int height;
    ArrayList<String> listAddress;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private boolean isAddedView = false;
    private int isLayoutChange = 0;
    boolean stop;
    int index = 0;
    private ImageView imgNEWS;
    private Handler handler;
    private Runnable r;
    boolean isPortrait;
    WindowManager.LayoutParams params;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private DoRssTask3 RssTask;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        isAddedView = false;
        sharedPreferences = getSharedPreferences(Constant.KEY_SHARED, 0);
        editor = sharedPreferences.edit();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        int y = sharedPreferences.getInt("POSITION", -1);
        if (y != -1)
            params.y = y;
        mLinearLayout = new LinearLayout(this);
        //noinspection ResourceAsColor
        mLinearLayout.setBackgroundColor(android.R.color.darker_gray);
        mWindowManager.addView(mLinearLayout, params);

        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        isPortrait = true;

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.horizontal_layout, mLinearLayout);

        horizontalScrollview = (HorizontalScrollView) mView.findViewById(R.id.horiztonal_scrollview_id);
        horizontalOuterLayout = (LinearLayout) mView.findViewById(R.id.horiztonal_outer_layout_id);
        horizontalScrollview.setHorizontalScrollBarEnabled(false);
        layoutBackground = (LinearLayout) mView.findViewById(R.id.layoutBackground);

        // layout for waiting download NEWS
        View v = layoutInflater.inflate(R.layout.download_progress, null, true);
        waitLayout = (RelativeLayout) v.findViewById(R.id.waitingLayout);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        horizontalOuterLayout.addView(v);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.icon_rotate);

        // Most Left Icon
        imgNEWS = (ImageView) mView.findViewById(R.id.imgNews);
        imgNEWS.startAnimation(a);
        imgNEWS.setOnTouchListener(touchListener);
//        imgNEWS.setOnLongClickListener(longClickListener);
//        imgNEWS.setOnClickListener(clickListener);

        listNews = new ArrayList<>();
        feed = new ArrayList<>();
        feedLink = new ArrayList<>();
        feedSub = new ArrayList<>();
        feedIndex = new ArrayList<>();

        listAddress = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        index = 0;
        isLayoutChange = -1;
        stop = false;
        update();

        if (scrollTimer == null) {
            scrollTimer = new Timer();

            final Runnable Timer_Tick = new Runnable() {
                public void run() {
                    if (horizontalOuterLayout.getVisibility() == View.VISIBLE) {
                        if (scrollMax == 0) {
                            getScrollMaxAmount();
                        }
                        scrollPos = (int) (horizontalScrollview.getScrollX() + 2.0);
//                    Log.d("TAN", "scrollPos: " + scrollPos + "; scrollMax: " + scrollMax + "; width: " + width);
                        horizontalScrollview.scrollTo(scrollPos, 0);
                    }
                }
            };
            if (scrollerSchedule != null) {
                scrollerSchedule.cancel();
                scrollerSchedule = null;
            }
            scrollerSchedule = new TimerTask() {
                @Override
                public void run() {
                    Timer_Tick.run();
                }
            };
            scrollTimer.schedule(scrollerSchedule, 10, 10);
        }

        r = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);

                if (horizontalOuterLayout.getVisibility() == View.VISIBLE) {
//                    if(listNews.size() > 0){
                    if (feed.size() > 0) {
                        if (index < feed.size()) {
                            int actualIndex = feedIndex.get(index);
                            if (waitLayout.getVisibility() == View.VISIBLE) {
                                waitLayout.setVisibility(View.INVISIBLE);
                                handler.postDelayed(this, 500);
                                addImagesToView(feed.get(actualIndex), feedLink.get(actualIndex), feedSub.get(actualIndex));
                                index++;
                            } else {
                                if ((scrollPos >= scrollMax - width && isPortrait == true) || (scrollPos >= scrollMax - height && isPortrait == false)) {
                                    addImagesToView(feed.get(actualIndex), feedLink.get(actualIndex), feedSub.get(actualIndex));
                                    index++;
                                }
                            }
                        } else {
                            Log.d("TAN", "Start refresh");
                            horizontalOuterLayout.removeAllViews();
                            horizontalOuterLayout.invalidate();
                            scrollPos = 0;

                            int t = feed.size();
                            for (int i = t - 1; i >= 0; i--) {
                                feed.remove(i);
                                feedLink.remove(i);
                                feedSub.remove(i);
                                feedIndex.remove(i);
                            }
                            isLayoutChange = 0;
                            stop = false;
                            index = 0;
                            update();
                        }
                    }
//                }
                }

            }
        }

        ;
        handler.postDelayed(r, 500);
        ViewTreeObserver vto = horizontalOuterLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()

                {
                    @Override
                    public void onGlobalLayout() {
                        horizontalOuterLayout.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        getScrollMaxAmount();
                        isLayoutChange++;
                    }
                }

        );
        horizontalOuterLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                                            @Override
                                                            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                                                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                                                getScrollMaxAmount();
                                                            }
                                                        }

        );
        Intent inte = new Intent(this, MainActivity.class);
        inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (inte != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, inte, 0);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Notification noti = new Notification.Builder(getApplicationContext())
                        .setContentTitle("RollingNEWS")
                        .setContentText("NEWS running")
                        .setSmallIcon(R.drawable.icon_rollingnews_notification_256x256)
                        .setContentIntent(pendingIntent)
                        .setColor(getResources().getColor(R.color.green))
                        .build();
                startForeground(1234, noti);
            } else {
                Notification noti = new Notification.Builder(getApplicationContext())
                        .setContentTitle("RollingNEWS")
                        .setContentText("NEWS running")
                        .setSmallIcon(R.drawable.icon_rollingnews_notification_96x96)
                        .setContentIntent(pendingIntent)
                        .build();
                startForeground(1234, noti);
            }

        }

        return Service.START_STICKY;
    }

    private void update() {
        String s = sharedPreferences.getString("LIST_ADDRESS", null);
        int sz = sharedPreferences.getInt("LIST_ADDRESS_SIZE", 0);
        if (s != null && sz != 0) {
            StringTokenizer st = new StringTokenizer(s, "#");
            for (int i = 0; i < sz; i++) {
                String link = st.nextToken();
                listAddress.add(link);
            }
        }
        for (int i = 0; i < listAddress.size(); i++) {
            RssTask = new DoRssTask3();
            RssTask.execute(listAddress.get(i));
        }
    }

    public void removeView() {
        mWindowManager.removeView(mLinearLayout);
    }

    @Override
    public void onDestroy() {
        removeView();
        if (handler != null)
            handler.removeCallbacks(r);
        if (scrollerSchedule != null) {
            scrollerSchedule.cancel();
            scrollerSchedule = null;
        }
        if (RssTask != null)
            RssTask.cancel(true);
        isAddedView = false;
        super.onDestroy();
    }

    public void getScrollMaxAmount() {
        int actualWidth = (horizontalOuterLayout.getMeasuredWidth() /*- 512*/);
        scrollMax = actualWidth;
    }

    private void addImagesToView(String str, final String strLink, final String strSub) {
        LayoutInflater newsItemLayout = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = newsItemLayout.inflate(R.layout.news_item, null, true);
        TextView t1 = (TextView) v.findViewById(R.id.txtWebName);
        final TextView t2 = (TextView) v.findViewById(R.id.txtNewsContent);
        //noinspection ResourceAsColor
//        t1.setBackgroundResource(R.drawable.bg_red_round);
        t1.setText(strSub);
        t2.setText(str);
        t2.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                      PackageManager packageManager = getApplicationContext().getPackageManager();
                                      Uri uri = Uri.parse(strLink.trim());
                                      browserIntent.setData(uri);
                                      if (browserIntent.resolveActivity(packageManager) != null) {
                                          browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                          startActivity(browserIntent);
                                      } else {
                                          Log.d("TAM", "url is not valid");
                                      }
//                                      Intent browserInter = new Intent();
//                                      browserInter.setAction("com.app.simteam.rollingnews.action.WEBCONTENT");
//                                      browserInter.putExtra("url", strLink.trim());
//                                      browserInter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                                      startActivity(browserInter);
                                  }
                              }
        );
        horizontalOuterLayout.addView(v);
    }

    private void stopAutoScrolling() {
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer = null;
        }
    }

    public class DoRssTask3 extends AsyncTask<String, Void, List<RSSFeed>> {
        private RSSFeedParser mNewsFeeder;
        private ArrayList<RSSFeed> mRssFeedList;
        private String sub;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<RSSFeed> doInBackground(String... params) {
            for (String urlVal : params) {
                //Log.d("TAN", "Downloading: " + urlVal);
                sub = new DataMaker().getSub(urlVal);
                mNewsFeeder = new RSSFeedParser(urlVal);
            }
            mRssFeedList = mNewsFeeder.parse();
            publishProgress();
            return mRssFeedList;
        }

        @Override
        protected void onPostExecute(List<RSSFeed> result) {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (mRssFeedList != null) {
                int size = mRssFeedList.size();
                int feedIndexSize = feedIndex.size();
                for (int i = 0; i < size; i++) {
                    feed.add(mRssFeedList.get(i).getTitle());
                    Log.d("TAN", "feed: " + mRssFeedList.get(i).getTitle() + "; image: " + mRssFeedList.get(i).getImage());
                    feedLink.add(mRssFeedList.get(i).getLink());
                    feedSub.add(sub);
                    feedIndex.add(new Integer(feedIndexSize + i));
                }
                Collections.shuffle(feedIndex.subList(index + 1, feedIndex.size() - 1));
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isPortrait = false;
        } else
            isPortrait = true;
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;
        private long startTime;
        private long endTime;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    startTime = System.currentTimeMillis();
                    return true;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    float duration = endTime - startTime;
                    float space = event.getRawY() - initialTouchY;
                    Log.d("TAN", "POSITION: " + params.y + "; duration: " + duration);
                    editor.putInt("POSITION", params.y);
                    editor.commit();
                    if (duration < 900 && space == 0) {
                        if (horizontalOuterLayout.getVisibility() == View.VISIBLE) {
                            horizontalOuterLayout.setVisibility(View.INVISIBLE);
                            layoutBackground.setVisibility(View.INVISIBLE);
                            horizontalOuterLayout.invalidate();
                        } else {
                            horizontalOuterLayout.setVisibility(View.VISIBLE);
                            layoutBackground.setVisibility(View.VISIBLE);
                            horizontalOuterLayout.invalidate();
                        }
                    } else if (duration >= 900 && space < 10 && space > -10) {
                        sendBroadcast(new Intent(MainActivity.REFRESH_DATA_INTENT));
                        stopSelf();
                    }

                    return true;
                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    mWindowManager.updateViewLayout(mLinearLayout, params);
                    return true;
            }
            return false;
        }
    };

    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            sendBroadcast(new Intent(MainActivity.REFRESH_DATA_INTENT));
            stopSelf();
            return false;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (horizontalOuterLayout.getVisibility() == View.VISIBLE) {
                horizontalOuterLayout.setVisibility(View.INVISIBLE);
                layoutBackground.setVisibility(View.INVISIBLE);
                horizontalOuterLayout.invalidate();
            } else {
                horizontalOuterLayout.setVisibility(View.VISIBLE);
                layoutBackground.setVisibility(View.VISIBLE);
                horizontalOuterLayout.invalidate();
            }
        }
    };

}

