package com.realgear.samplemusicplayertest.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.extensions.gridlayout.GridLayoutManagerExtended;
import com.realgear.mediaplayer.LibraryManager;
import com.realgear.mediaplayer.model.Song;
import com.realgear.samplemusicplayertest.R;
import com.realgear.samplemusicplayertest.ui.adapters.BaseRecyclerViewAdapter;
import com.realgear.samplemusicplayertest.ui.adapters.LibraryRecyclerViewAdapter;
import com.realgear.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;
import com.realgear.samplemusicplayertest.ui.adapters.models.SongRecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentLibrary extends Fragment {

    private View m_vRootView;

    private RecyclerView m_vLibraryRecyclerView;
    private BaseRecyclerViewAdapter m_vLibraryAdapter;
    private GridLayoutManagerExtended m_vGridLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.m_vRootView = inflater.inflate(R.layout.fragment_library, container, false);
        return this.m_vRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.m_vLibraryRecyclerView = findViewById(R.id.library_recyclerView);

        int panelheights = getResources().getDimensionPixelSize(R.dimen.navigation_bar_height) + getResources().getDimensionPixelSize(R.dimen.media_player_bar_height);
        this.m_vLibraryRecyclerView.setPadding(0, 0, 0, panelheights);

        List<Song> songs = LibraryManager.getSongs(getContext());
        List<BaseRecyclerViewItem> items = new ArrayList<>();

        for (Song song : songs) {
            items.add(new SongRecyclerViewItem(song));
        }

        this.m_vLibraryAdapter = new LibraryRecyclerViewAdapter(items);
        this.m_vLibraryAdapter.setHasStableIds(true);
        setAdapterViewType(BaseRecyclerViewAdapter.ViewType.GRID);
        this.m_vLibraryRecyclerView.setAdapter(this.m_vLibraryAdapter);

        FloatingActionButton btn = findViewById(R.id.btn_test_layout);
        btn.setOnClickListener(v -> {
            setAdapterViewType((
                    this.m_vLibraryAdapter.getViewType() == BaseRecyclerViewAdapter.ViewType.GRID) ?
                    BaseRecyclerViewAdapter.ViewType.LIST : BaseRecyclerViewAdapter.ViewType.GRID);
        });
    }

    private void setAdapterViewType(BaseRecyclerViewAdapter.ViewType viewType) {
        this.m_vLibraryAdapter.setAdapterViewType(viewType);

        int rowCount = (viewType == BaseRecyclerViewAdapter.ViewType.LIST) ? 1 : 3;

        if(m_vGridLayout == null) {
            this.m_vGridLayout = new GridLayoutManagerExtended(getContext(), rowCount);
            this.m_vLibraryRecyclerView.setLayoutManager(this.m_vGridLayout);
        }
        else {
            this.m_vGridLayout.setSpanCount(rowCount);
            this.m_vLibraryAdapter.notifyDataSetChanged();
        }
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return this.m_vRootView.findViewById(id);
    }
}
