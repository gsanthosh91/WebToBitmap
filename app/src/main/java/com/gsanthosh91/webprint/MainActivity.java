package com.gsanthosh91.webprint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.setInitialScale(50);

        //webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                //if(progress==100)
                //    new Background().execute();
            }
        });
        webView.loadUrl("https://www.lipsum.com/");


    }



    class Background extends AsyncTask<Void, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(Void... params)
        {
            try
            {
                Thread.sleep(2000);
                Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                webView.draw(canvas);
                return bitmap;
            }
            catch (InterruptedException e){}
            catch (Exception e){}
            return null;
        }
        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap result)
        {

            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();

            Intent anotherIntent = new Intent(MainActivity.this, PrintActivity.class);
            anotherIntent.putExtra("image", byteArray);
            startActivity(anotherIntent);

            //imageview.setImageBitmap(result);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // return true so that the menu pop up is opened
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                new Background().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
