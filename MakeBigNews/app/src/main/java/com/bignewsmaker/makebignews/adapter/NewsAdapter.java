package com.bignewsmaker.makebignews.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignewsmaker.makebignews.Interface.NewsService;
import com.bignewsmaker.makebignews.R;
import com.bignewsmaker.makebignews.basic_class.ConstData;
import com.bignewsmaker.makebignews.basic_class.Item1;
import com.bignewsmaker.makebignews.basic_class.News;
import com.bignewsmaker.makebignews.extra_class.RetrofitTool;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1;
    private boolean hasMore = true;   // 变量，是否有更多数据
    private boolean fadeTips = false; // 变量，是否隐藏了底部的提示
    private RetrofitTool retrofitTool = RetrofitTool.getInstance();//设置接收器
    private HashSet<String> hs;
    private OnItemClickListener onItemClickListener;
    private ConstData const_data = ConstData.getInstance();// 设置访问全局变量接口
    private List<News> mNewsList;
    private Context context;

    public NewsAdapter(List<News> newsList, Context context, boolean hasMore) {
        mNewsList = newsList;
        this.context = context;
        this.hasMore = hasMore;
    }

    public void add(List<News> news) {
        mNewsList.addAll(news);
    }

    //点击接口
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView delete;
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
            delete = (ImageView) view.findViewById(R.id.deletenews);
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

    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context != null)
            context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        final ViewHolder holder = new ViewHolder(view, onItemClickListener);

        holder.delete.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                final News news = mNewsList.get(position);
                mNewsList.remove(position);
                notifyDataSetChanged();
//                Toast.makeText(context, "已屏蔽新闻\""+news.getNews_Title()+"\"", Toast.LENGTH_SHORT).show();

                NewsService service = retrofitTool.getRetrofit().create(NewsService.class);
                Call<News> repos = service.listRepos(news.getNews_ID());
                repos.enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        if (response.isSuccessful()) {
                            ArrayList<Item1> item1 = response.body().getKeywords();
                            String[] keywords = new String[item1.size()>5?5:item1.size()];
                            for (int i = 0; i < keywords.length; i++)
                                keywords[i] = item1.get(i).word;
                            addToDislike(keywords,news,position);

                        }
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        if (viewType == NORMAL_TYPE) {
            return holder;
        } else {
            return new FootHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.footview, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        if (holder instanceof ViewHolder) {

            News news = mNewsList.get(position);
            String[] urls = news.getNews_Pictures().split(";|\\s");

            ((ViewHolder) holder).newsTitle.setText(news.getNews_Title());
            ((ViewHolder) holder).newsIntro.setText(news.getNews_Intro().replaceAll("\\s", ""));
            ((ViewHolder) holder).newsSource.setText("\n"
                    + (!news.getNews_Author().equals("") ? news.getNews_Author() : news.getNews_Source()));
            if (!const_data.isModel_day()) {
                ((ViewHolder) holder).cardView.setBackgroundColor(Color.rgb(66, 66, 66));
                ((ViewHolder) holder).newsTitle.setTextColor(Color.rgb(255, 255, 255));
                ((ViewHolder) holder).newsIntro.setTextColor(Color.rgb(255, 255, 255));
                ((ViewHolder) holder).newsSource.setTextColor(Color.rgb(255, 255, 255));
            } else {
                ((ViewHolder) holder).cardView.setBackgroundColor(Color.rgb(255, 255, 255));
                ((ViewHolder) holder).newsTitle.setTextColor(Color.rgb(0, 0, 0));
                ((ViewHolder) holder).newsIntro.setTextColor(Color.rgb(0, 0, 0));
                ((ViewHolder) holder).newsSource.setTextColor(Color.rgb(0, 0, 0));
            }
            hs = const_data.getHaveRead();
            if (hs.contains(news.getNews_ID())) {
                ((ViewHolder) holder).newsTitle.setTextColor(Color.rgb(128, 128, 128));
                ((ViewHolder) holder).newsIntro.setTextColor(Color.rgb(128, 128, 128));
                ((ViewHolder) holder).newsSource.setTextColor(Color.rgb(128, 128, 128));
            }

            if (urls.length == 0 || !urls[0].contains("h") || !const_data.getShow_picture())
                return;
            else if (urls.length == 1) {
                if (urls[0].toLowerCase().contains("logo") || urls[0].toLowerCase().contains("icocopy"))
                    return;
                ((ViewHolder) holder).newsImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(urls[0]).into(((ViewHolder) holder).newsImage);
            } else if (urls.length == 2) {
                ((ViewHolder) holder).newsImage.setVisibility(View.VISIBLE);
                String url = urls[1].toLowerCase().contains("logo") || urls[1].toLowerCase().contains("icocopy") ? urls[0] : urls[1];
                Glide.with(context).load(url).into(((ViewHolder) holder).newsImage);
            } else {
                ((ViewHolder) holder).newsImage.setVisibility(View.GONE);
                ((ViewHolder) holder).newsImage0.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).newsImage1.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).newsImage2.setVisibility(View.VISIBLE);
                Glide.with(context).load(urls[0]).into(((ViewHolder) holder).newsImage0);
                Glide.with(context).load(urls[1]).into(((ViewHolder) holder).newsImage1);
                Glide.with(context).load(urls[2]).into(((ViewHolder) holder).newsImage2);
            }
        } else {
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                fadeTips = false;
                if (mNewsList.size() > 0) {
                    // 如果查询数据发现增加之后，就显示正在加载更多
                    ((FootHolder) holder).tips.setText("正在加载...");
                }
            }
        }
    }

    // 暴露接口，改变fadeTips的方法
    public boolean isFadeTips() {
        return fadeTips;
    }

    public void setFadeTips(boolean fadeTips) {
        this.fadeTips = fadeTips;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    @Override
    public int getItemCount() {
        int begin = fadeTips ? 0 : 1;
        return mNewsList.size() + begin;
    }

    @Override
    public int getItemViewType(int position) {
        if (fadeTips)
            return NORMAL_TYPE;
        if (position == getItemCount() - 1) {
            return FOOT_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    public String getId(int position) {
        return mNewsList.get(position).getNews_ID();
    }

    private void addToDislike(final String[] keywords,final News news, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("已屏蔽该条新闻，请选择需要进一步屏蔽的关键词");
        final boolean[] checked=new boolean[keywords.length];
        builder.setMultiChoiceItems(keywords, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checked[which]=isChecked;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0;i<keywords.length;i++){
                    if(checked[i])
                        const_data.setDislike(keywords[i]);
                }
                Toast.makeText(context, "已屏蔽相关新闻，刷新后生效", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("撤销", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNewsList.add(position,news);
                notifyDataSetChanged();
                Toast.makeText(context, "已撤销", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();


    }
}

