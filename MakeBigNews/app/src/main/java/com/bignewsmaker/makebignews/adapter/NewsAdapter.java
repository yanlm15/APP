package com.bignewsmaker.makebignews.adapter;

import android.content.Context;
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

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口

    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    private List<News> mNewsList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView newsTitle, newsIntro, newsSource;
        ImageView newsImage, newsImage1, newsImage2, newsImage3;
        private OnItemClickListener onItemClickListener = null;

        public ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsIntro = (TextView) view.findViewById(R.id.news_intro);
            newsSource = (TextView) view.findViewById(R.id.news_source);
            newsImage = (ImageView) view.findViewById(R.id.news_image);
            newsImage1 = (ImageView) view.findViewById(R.id.news_image1);
            newsImage2 = (ImageView) view.findViewById(R.id.news_image2);
            newsImage3 = (ImageView) view.findViewById(R.id.news_image3);

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

    public NewsAdapter(List<News> newsList) {
        mNewsList = newsList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        final ViewHolder holder = new ViewHolder(view, onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        String[] urls = news.getNews_Pictures().split(";");

        holder.newsTitle.setText(news.getNews_Title());
        holder.newsIntro.setText(news.getNews_Intro().replaceAll("\\s", ""));
        holder.newsSource.setText("\n" + news.getNews_Source());
        if (urls.length == 0||!const_data.getShow_picture())
            holder.newsImage.setVisibility(View.GONE);
        else if (urls.length < 3) {
//            holder.newsImage.setImageResource(R.mipmap.ic_launcher);
            Glide.with(holder.newsImage).load(R.mipmap.ic_launcher).into(holder.newsImage);
        } else {
            holder.newsImage1.setVisibility(View.VISIBLE);
            holder.newsImage2.setVisibility(View.VISIBLE);
            holder.newsImage3.setVisibility(View.VISIBLE);
            Glide.with(holder.newsImage1).load(R.mipmap.ic_launcher).into(holder.newsImage1);
            Glide.with(holder.newsImage2).load(R.mipmap.ic_launcher).into(holder.newsImage2);
            Glide.with(holder.newsImage3).load(R.mipmap.ic_launcher).into(holder.newsImage3);
        }

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

}

