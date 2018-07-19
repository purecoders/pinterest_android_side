package com.farjami.mohsen.pinterest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.farjami.mohsen.pinterest.R;

import com.farjami.mohsen.pinterest.data_model.Post;
import com.farjami.mohsen.pinterest.view.activity.ActivityShowPost;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainPostsAdapter extends RecyclerView.Adapter<MainPostsAdapter.ListViewHolder>{

  private OnBottomReachedListener onBottomReachedListener;

  private Context context;
  private List <Post> posts;

  public MainPostsAdapter(Context context, List <Post> posts){

    this.context = context;
    this.posts = posts;
  }

  @Override
  public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.item_post_in_main_menu,parent,false);
    return new ListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ListViewHolder holder, int position) {

    final Post post = posts.get(position);


    Picasso.with(context).
      load(post.getImageUrl())
      //.placeholder(context.getResources().getDrawable(R.drawable.default_good_image)).
      //.error(context.getResources().getDrawable(R.drawable.default_no_image))
      .into(holder.image);

    //holder.expand_text_view.setText(post.getDescription());
    SparseBooleanArray mTogglePositions = new SparseBooleanArray();
    holder.expand_text_view.setText(post.getDescription(),mTogglePositions, position);




    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(context, ActivityShowPost.class);
        intent.putExtra("ID", post.getId());
        intent.putExtra("IMAGE", post.getImageUrl());
        intent.putExtra("DESCRIPTION", post.getDescription());
        intent.putExtra("TAGS", post.getTags());
//        intent.putExtra("SAVE_COUNT", post.getSaveCount());
//        intent.putExtra("SHARE_COUNT", post.getShareCount());
//        intent.putExtra("USERNAME", post.getUsername());
//        intent.putExtra("DESCRIPTION", post.getDescription());
        context.startActivity(intent);

      }
    });


  }

  @Override
  public int getItemCount() {
    return posts.size();
  }

  public class ListViewHolder extends RecyclerView.ViewHolder{

    private ImageView image;
    private TextView expandable_text;
    private  ExpandableTextView expand_text_view;

    public ListViewHolder(View itemView) {
      super(itemView);
      image = (ImageView) itemView.findViewById(R.id.post_image);
      expandable_text= (TextView) itemView.findViewById(R.id.expandable_text);
      expand_text_view= (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);

    }


  }


  public void notifyData(List<Post> newPosts) {
    //Log.d("notifyData ", myList.size() + "");
    if (newPosts.size() > 0) {
      for (int i = 0; i < newPosts.size(); i++) {
        this.posts.add(newPosts.get(i));
      }
      this.notifyItemInserted(posts.size() - 1);

      //notifyDataSetChanged();
    }
  }






}