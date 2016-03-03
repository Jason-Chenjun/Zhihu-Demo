package com.cn.materiadesign.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.materiadesign.Application;
import com.cn.materiadesign.Constant;
import com.cn.materiadesign.R;
import com.cn.materiadesign.bean.LatestNews;
import com.cn.materiadesign.bean.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jun on 2/24/16.
 */
public class FragmentNews extends Fragment implements Response.Listener, Response.ErrorListener {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.LATESNEWS_URL, this, this);
        Application.getInstance().getRequestQueue().add(request);
        return view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "加载数据失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            LatestNews latestNews = JSON.parseObject(response.toString(), LatestNews.class);
            List<Story> stories = new ArrayList<>();
            for (Story story : latestNews.getStories()) {
                stories.add(story);
            }
            recyclerView.setAdapter(new ContentAdapter(stories));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Story> items;
        private Context context;

        public ContentAdapter(List<Story> stories) {
            this.items = stories != null ? stories : new ArrayList<Story>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Story story = items.get(position);
            holder.title.setText(story.getTitle());
            String imgUrl = story.getImages();
            Glide.with(context).load(imgUrl.substring(2, imgUrl.length() - 2)).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return items != null ? items.size() : 0;
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.card_text);
            this.image = (ImageView) itemView.findViewById(R.id.card_image);
        }
    }
}