package com.bignewsmaker.makebignews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private List<LIST> mNewsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsIntro, newsSource;

        public ViewHolder(View view) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsIntro = (TextView) view.findViewById(R.id.news_intro);
            newsSource = (TextView) view.findViewById(R.id.news_source);
        }
    }

    public NewsAdapter(List<LIST> newsList) {
        mNewsList = newsList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item_nopicture, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LIST list = mNewsList.get(position);
        holder.newsTitle.setText(list.getNews_Title());
        holder.newsIntro.setText(list.getNews_Intro().replaceAll("\\s",""));
        holder.newsSource.setText("\n"+list.getNews_Source());

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}