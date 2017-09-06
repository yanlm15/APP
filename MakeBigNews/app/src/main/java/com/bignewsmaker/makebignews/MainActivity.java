package com.bignewsmaker.makebignews;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<LIST> newsList = new ArrayList<LIST>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获得新闻
        initNews();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);

    }

    private void initNews() {

        String s = "";
        try {
            //暂时性的解决NetworkOnMainThreadException办法
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpURLConnection conn;
            URL url = new URL("http://166.111.68.66:2042/news/action/query/latest?pageNo=1&pageSize=50");
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(30 * 1000);
            if (conn.getResponseCode() != 200)
                return;
            InputStream inputStream = conn.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            byte[] data = outputStream.toByteArray();
            s += new String(data, "utf-8");
            outputStream.close();
            inputStream.close();
            conn.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            News news = mapper.readValue(s, News.class);
            for (int i = 0; i < news.getList().size(); i++)
                newsList.add(news.getList().get(i));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
