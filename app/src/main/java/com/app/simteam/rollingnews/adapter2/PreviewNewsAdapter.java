package com.app.simteam.rollingnews.adapter2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.simteam.rollingnews.R;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sev_user on 9/1/2016.
 */
public class PreviewNewsAdapter extends RecyclerView.Adapter<NewsItemHolder> {
    Context context;
    ArrayList<NewsRollingItem> listNews;

    public PreviewNewsAdapter(Context context, ArrayList<NewsRollingItem> listNews) {
        this.context = context;
        this.listNews = listNews;
    }

    @Override
    public NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_news_item, parent, false);
        NewsItemHolder holder = new NewsItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsItemHolder holder, int position) {

        final NewsRollingItem news = listNews.get(position);
        holder.title.setText(news.title);

        if (listNews.get(position).btm == null /*&& listNews.get(position).imgUrl != null*/) {
            holder.img.setImageDrawable(null);
//            Log.d("TAN", "Load image from " + listNews.get(position).imgUrl);
            new ImageDownloaderTask(holder.img, position).execute(listNews.get(position).imgUrl);
        } else {
            holder.img.setImageBitmap(listNews.get(position).btm);
        }
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = news.url;
//                url = url.replace("htpp://", "");
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.setData(Uri.parse(url));
//                context.startActivity(i);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                PackageManager packageManager = context.getPackageManager();
                Uri uri = Uri.parse(url.trim());
                browserIntent.setData(uri);
                if (browserIntent.resolveActivity(packageManager) != null) {
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                } else {
                    Log.d("TAM", "url is not valid");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    public void addView(ArrayList<NewsRollingItem> list) {
        listNews.addAll(list);
        notifyDataSetChanged();
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final int pos;

        public ImageDownloaderTask(ImageView imageView, int position) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            pos = position;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        listNews.get(pos).setBitmap(bitmap);
                    } else {
//                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
//                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return getResizedBitmap(bitmap, 100, 100);
            }
        } catch (Exception e) {
            if (urlConnection != null)
                urlConnection.disconnect();
            Log.e("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

}
