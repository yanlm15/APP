package com.bignewsmaker.makebignews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignewsmaker.makebignews.Interface.OnItemClickListener;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.News;

import java.util.List;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;

    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    private List<News> mNewsList;

    static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView newsTitle, newsIntro, newsSource;
        private OnItemClickListener onItemClickListener = null;

        public ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.news_title);
            newsIntro = (TextView) view.findViewById(R.id.news_intro);
            newsSource = (TextView) view.findViewById(R.id.news_source);

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
                .inflate(R.layout.item_news_nopicture, parent, false);
        final ViewHolder holder = new ViewHolder(view,onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News list = mNewsList.get(position);
        holder.newsTitle.setText(list.getNews_Title());
        holder.newsIntro.setText(list.getNews_Intro().replaceAll("\\s",""));
        holder.newsSource.setText("\n"+list.getNews_Source());

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}