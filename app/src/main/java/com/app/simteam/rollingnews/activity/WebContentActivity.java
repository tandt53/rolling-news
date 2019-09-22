package com.app.simteam.rollingnews.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.utils.HTMLPage;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class WebContentActivity extends AppCompatActivity {

    WebView browser;
    URLConnection urlConnection = null;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);

        browser = (WebView) findViewById(R.id.web_content);

        url = getIntent().getExtras().getString("url");
//        StringBuilder html = new StringBuilder();
//        try {
//            String page = new HTMLPage().execute(url).get();
//            Document doc = Jsoup.parse(page);
//            Elements elements = doc.select("p");
//            html.append("<!DOCTYPE HTML>");
//            html.append("<html>");
//            html.append("<body>");
//
//            for(Element e : elements) {
//                html.append(e);
//            }
//
//            html.append("</body>");
//            html.append("</html>");
//            browser.loadDataWithBaseURL(null, html.toString(), "text/html","utf-8", "");
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        new GetRSS(browser).execute(url);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        url = getIntent().getExtras().getString("url");
        new GetRSS(browser).execute(url);
    }

    public class GetRSS extends AsyncTask<String, JSONObject, String> {
        StringBuilder s = new StringBuilder();

        WebView webView;

        public GetRSS(WebView wv) {
            this.webView = wv;
        }
        @Override
        protected String doInBackground(String...params) {
            try {
                URL fivefilter = new URL("https://www.readability.com/api/content/v1/parser?url=" + params[0] + "&token=a0c140bf1ba403efff2788adfd7ec97f249d4693");
                urlConnection = fivefilter.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader inread = new InputStreamReader(in);

                int data = inread.read();

                while (data != -1) {
                    char current = (char) data;
                    data = inread.read();
                    s.append(current);
                }


                JSONObject object = new JSONObject(s.toString());
                //Log.d("MRNEO", object.toString());
                return s.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection = null;
                }
            }
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            try {
                JSONObject json = new JSONObject(jsonObject);
                String title = "<p style='color:blue;font-size:20;font-weight:bold'>" + json.getString("title") + "</p>";

                String webContent = json.getString("content");

                String page = "<html><head><meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" /></head><body style='text-align:justify'>"
                        + title + webContent + "</body></html>";

                WebSettings ws = webView.getSettings();
                ws.setDefaultTextEncodingName("utf-8");
                webView.loadData(page, "text/html; charset=utf-8", "utf-8");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

//    private int getScale(){
//        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        int width = display.getWidth();
//        Double val = new Double(width)/new Double(PIC_WIDTH);
//        val = val * 100d;
//        return val.intValue();
//    }
}
