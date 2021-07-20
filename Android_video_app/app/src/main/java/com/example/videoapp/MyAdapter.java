package com.example.videoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.videoapp.data.VideoResponse;

import java.text.DecimalFormat;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    //放所有的视频资源
    public static List<VideoResponse.Video> mDataSet;

    private static final String TAG = "MyAdapter";

    private int mNumberItems;


    private final ListItemClickListener mOnClickListener;

    private static int viewHolderCount;

    public MyAdapter(int numListItems, ListItemClickListener listener) {
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    public void setData(List<VideoResponse.Video> mDataSet) {
        this.mDataSet = mDataSet;
        this.mNumberItems=mDataSet.size();
    }
    public static void addData(VideoResponse.Video newData){
        mDataSet.add(newData);
    }


    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.number_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + viewHolderCount);
        viewHolderCount++;

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View itemView;
        TextView like_count;
        ImageView cover;
        TextView nickname;
        ImageView avatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            nickname=itemView.findViewById(R.id.nickname);
            like_count=itemView.findViewById(R.id.like_count);
            cover=itemView.findViewById(R.id.item_cover);
            avatar=itemView.findViewById(R.id.avatar);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            VideoResponse.Video video = mDataSet.get(position);

            if(video.likeCount<10000) {//点赞数低于10000
                like_count.setText(String.valueOf(video.likeCount));
            }else{
                //以万为单位
                if(video.likeCount%10000==0){//整除一万
                    like_count.setText(video.likeCount/10000+"w");
                }else{//保留一位小数
                    float num =(float)video.likeCount/10000;
                    DecimalFormat df = new DecimalFormat("0.0");
                    like_count.setText(df.format(num)+"w");
                }
            }
            nickname.setText(video.nickname);

            //获取视频第一帧作为封面
//            String picUrl = video.url.replaceFirst("http", "https");
            String picUrl=video.url;
            loadCover(picUrl);

//            picUrl = video.avatar.replaceFirst("http", "https");
// 这里本来就是https啦
            picUrl=video.avatar;
            loadAvatar(picUrl);

        }

        public void loadAvatar(String url){
            RequestOptions cropOptions = new RequestOptions();
            cropOptions = cropOptions.circleCrop();
            Glide.with(itemView)
                    .load(url)
                    .apply(cropOptions)
                    .error(R.mipmap.avatar)
                    .into(avatar);
        }

        public void loadCover( String url) {
            Glide.with(itemView)
                    .setDefaultRequestOptions(
                            new RequestOptions()
                                    .frame(4000000)
                                    .error(R.mipmap.avatar)//可以忽略
                    )
                    .load(url)
                    .into(cover);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(clickedPosition);
            }
        }
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


}
