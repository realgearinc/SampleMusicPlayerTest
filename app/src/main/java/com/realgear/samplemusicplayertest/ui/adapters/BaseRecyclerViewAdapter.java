package com.realgear.samplemusicplayertest.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.realgear.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;
import com.realgear.samplemusicplayertest.ui.adapters.viewholders.BaseViewHolder;

import java.util.List;

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public enum ViewType {
        LIST, GRID
    }

    final List<BaseRecyclerViewItem> m_vItems;

    private ViewType m_vLayoutViewType;

    public BaseRecyclerViewAdapter(List<BaseRecyclerViewItem> items) {
        this.m_vItems = items;
    }

    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onInitializeView(this.m_vLayoutViewType);
        holder.onBindViewHolder(this.m_vItems.get(position));
    }

    public void setAdapterViewType(ViewType viewType) {
        this.m_vLayoutViewType = viewType;
    }

    @Override
    public int getItemCount() {
        return this.m_vItems.size();
    }

    @Override
    public long getItemId(int position) {
        return this.m_vItems.get(position).getHashCode();
    }

    @Override
    public int getItemViewType(int position) {
        return this.m_vItems.get(position).getItemType().ordinal();
    }

    public ViewType getViewType() {
        return this.m_vLayoutViewType;
    }
}
