package com.bignewsmaker.makebignews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignewsmaker.makebignews.Interface.OnItemClickListener;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private HashSet<String> hs;
    private OnItemClickListener onItemClickListener;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口

    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    private List<News> mNewsList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView newsTitle, newsIntro, newsSource;
        ImageView newsImage, newsImage0, newsImage1, newsImage2;
        private OnItemClickListener onItemClickListener = null;

        public ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsIntro = (TextView) view.findViewById(R.id.news_intro);
            newsSource = (TextView) view.findViewById(R.id.news_source);
            cardView = (CardView) view.findViewById(R.id.cardview);
            newsImage = (ImageView) view.findViewById(R.id.news_image);
            newsImage0 = (ImageView) view.findViewById(R.id.news_image0);
            newsImage1 = (ImageView) view.findViewById(R.id.news_image1);
            newsImage2 = (ImageView) view.findViewById(R.id.news_image2);
            //点击事件
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, getLayoutPosition());
            }
        }
    }

    public NewsAdapter(List<News> newsList, Context context) {
        mNewsList = newsList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context != null)
            context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        ViewHolder holder = new ViewHolder(view, onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        News news = mNewsList.get(position);
        String[] urls = news.getNews_Pictures().split(";|\\s");
        holder.newsTitle.setText(news.getNews_Title());
        holder.newsIntro.setText(news.getNews_Intro().replaceAll("\\s", ""));
        holder.newsSource.setText("\n" + (!news.getNews_Author().equals("")?news.getNews_Author():news.getNews_Source()));
        if (!const_data.isModel_day()) {
            holder.cardView.setBackgroundColor(Color.rgb(66, 66, 66));
            holder.newsTitle.setTextColor(Color.rgb(255, 255, 255));
            holder.newsIntro.setTextColor(Color.rgb(255, 255, 255));
            holder.newsSource.setTextColor(Color.rgb(255, 255, 255));
        } else {
            holder.cardView.setBackgroundColor(Color.rgb(255, 255, 255));
            holder.newsTitle.setTextColor(Color.rgb(0, 0, 0));
            holder.newsIntro.setTextColor(Color.rgb(0, 0, 0));
            holder.newsSource.setTextColor(Color.rgb(0, 0, 0));
        }
        hs=const_data.getHaveRead();
        if(hs.contains(news.getNews_ID())){
            holder.newsTitle.setTextColor(Color.rgb(128, 128, 128));
            holder.newsIntro.setTextColor(Color.rgb(128, 128, 128));
            holder.newsSource.setTextColor(Color.rgb(128, 128, 128));
        }

        if (urls.length == 0 || !urls[0].contains("h") || !const_data.getShow_picture())
            return;
        else if (urls.length < 3) {
            holder.newsImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(urls[0]).into(holder.newsImage);
        } else {
            holder.newsImage.setVisibility(View.GONE);
            holder.newsImage0.setVisibility(View.VISIBLE);
            holder.newsImage1.setVisibility(View.VISIBLE);
            holder.newsImage2.setVisibility(View.VISIBLE);
            Glide.with(context).load(urls[0]).into(holder.newsImage0);
            Glide.with(context).load(urls[1]).into(holder.newsImage1);
            Glide.with(context).load(urls[2]).into(holder.newsImage2);
        }


    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

}

