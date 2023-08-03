package com.realgear.samplemusicplayertest.ui.adapters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.realgear.samplemusicplayertest.threads.UIThread;
import com.realgear.samplemusicplayertest.ui.adapters.helpers.BaseViewHelper;
import com.realgear.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;
import com.realgear.samplemusicplayertest.ui.adapters.models.SongRecyclerViewItem;
import com.realgear.samplemusicplayertest.ui.adapters.viewholders.BaseViewHolder;
import com.realgear.samplemusicplayertest.ui.adapters.viewholders.SongViewHolder;

import java.util.ArrayList;
import java.util.List;

public class LibraryRecyclerViewAdapter extends BaseRecyclerViewAdapter {
    private String TAG = getClass().getSimpleName();

    public LibraryRecyclerViewAdapter(List<BaseRecyclerViewItem> items) {
        super(items);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseRecyclerViewItem.ItemType itemType = BaseRecyclerViewItem.ItemType.values()[viewType];

        switch (itemType) {
            case SONG:
                return BaseViewHelper.onCreateViewHolder(SongViewHolder.class, parent);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.itemView.setOnClickListener((v) -> {
            int i = position;
            UIThread.getInstance().getMediaPlayerThread().getCallback().onClickPlay(i, getQueue());
            Log.e(TAG, "Attempting to play song at position : " + i);
        });
    }

    private List<Integer> getQueue() {
        List<Integer> results = new ArrayList<>();
        if (m_vItems != null) {
            for (BaseRecyclerViewItem item : m_vItems) {
                results.add(((SongRecyclerViewItem)item).getSongId());
            }
        }
        return results;
    }
}
