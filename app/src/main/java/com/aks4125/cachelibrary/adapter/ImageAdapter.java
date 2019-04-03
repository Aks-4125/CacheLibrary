package com.aks4125.cachelibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aks4125.cachelibrary.R;
import com.aks4125.cachelibrary.models.PinterestModel;
import com.aks4125.cachex.AppLogger;
import com.aks4125.cachex.DownloadUtils;
import com.aks4125.cachex.interfaces.IDataBridge;
import com.aks4125.cachex.model.DataBridge;
import com.aks4125.cachex.model.ImageDataBridge;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends
        RecyclerView.Adapter<ImageAdapter.PinHolder> {

    private static final String TAG = ImageAdapter.class.getSimpleName();

    private List<Object> list;
    private OnItemClickListener onItemClickListener;
    private DownloadUtils mProvider;

    public ImageAdapter(List<Object> list,
                        OnItemClickListener onItemClickListener) {
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        mProvider = DownloadUtils.getInstance();
    }

    @NotNull
    @Override
    public PinHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_image, parent, false);
        return new PinHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NotNull final PinHolder holder, int position) {
        String imageUrl;
        if (list.get(position) instanceof PinterestModel) {
            PinterestModel mData = (PinterestModel) list.get(position);
            imageUrl = mData.getUrls().getRaw();
            holder.tvUsername.setText(mData.getUser().getName());
        } else { // in load more case only
            imageUrl = (String) list.get(holder.getLayoutPosition());
            holder.tvUsername.setVisibility(View.GONE);
        }

        if (ThreadLocalRandom.current().nextInt(0, 10 + 1) % 2 == 0) {
            holder.imageView.getLayoutParams().height = 1000;
        }
        // image request
        DataBridge mDataTypeImageCancel = new ImageDataBridge(holder.imageView, imageUrl,
                new IDataBridge() {
                    @Override
                    public void onStart(DataBridge mDownloadDataType) {
                        /*on subscribe*/
                    }

                    @Override
                    public void onSuccess(DataBridge mDownloadDataType) {
                        AppLogger.d(TAG, "onSuccess: came from" + mDownloadDataType.source);
                        /* perform to do operations */
                    }

                    @Override
                    public void onFailure(DataBridge mDownloadDataType, int statusCode, byte[] errorResponse, Throwable e) {
                        holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                    }

                    @Override
                    public void onRetry(DataBridge mDownloadDataType, int retryNo) {
                        /* required */
                    }
                });
        mProvider.getRequest(mDataTypeImageCancel);
        AppLogger.d(TAG, "onBindViewHolder: " + position);

        holder.bind(onItemClickListener, position, imageUrl);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {

        void onItemClick(ImageView imageView, String imageUrl, int pos);
    }

    class PinHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView mParentView;
        TextView tvUsername;

        PinHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mImgView);
            mParentView = itemView.findViewById(R.id.mParentView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }

        void bind(final OnItemClickListener listener, int position, String imageUrl) {
            itemView.setOnClickListener(v -> listener.onItemClick(imageView, imageUrl, position));


        }
    }

}