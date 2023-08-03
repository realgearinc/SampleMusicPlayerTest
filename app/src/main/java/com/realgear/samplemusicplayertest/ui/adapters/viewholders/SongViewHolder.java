package com.realgear.samplemusicplayertest.ui.adapters.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.realgear.samplemusicplayertest.R;
import com.realgear.samplemusicplayertest.glide.audiocover.AudioFileCover;
import com.realgear.samplemusicplayertest.ui.adapters.BaseRecyclerViewAdapter;
import com.realgear.samplemusicplayertest.ui.adapters.models.BaseRecyclerViewItem;
import com.realgear.samplemusicplayertest.ui.adapters.models.SongRecyclerViewItem;

public class SongViewHolder extends BaseViewHolder {

    private LinearLayout m_vRootView;

    private ConstraintLayout m_vImageView_Parent;

    private TextView m_vTextView_Title;
    private TextView m_vTextView_Artist;

    private ImageView m_vImageView_Art;

    public SongViewHolder(@NonNull View itemView) {
        super(itemView);

        this.m_vRootView = findViewById(R.id.item_root_view);
        this.m_vImageView_Parent = findViewById(R.id.item_song_art_image_view_parent);

        this.m_vTextView_Title = findViewById(R.id.item_song_title_text_view);
        this.m_vTextView_Artist = findViewById(R.id.item_song_artist_text_view);

        this.m_vImageView_Art = findViewById(R.id.item_song_art_image_view);
    }

    @Override
    public void onInitializeView(BaseRecyclerViewAdapter.ViewType viewType) {
        switch (viewType) {
            case GRID:
                this.m_vRootView.setOrientation(LinearLayout.VERTICAL);
                this.m_vImageView_Parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                break;

            case LIST:
                this.m_vRootView.setOrientation(LinearLayout.HORIZONTAL);
                this.m_vImageView_Parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, itemView.getResources().getDimensionPixelSize(R.dimen.item_library_song_art_size)));
                break;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewItem viewItem) {
        SongRecyclerViewItem item = (SongRecyclerViewItem) viewItem;

        this.m_vTextView_Title.setText(viewItem.getTitle());
        Glide.with(itemView.getContext())
                .load(new AudioFileCover(item.getFilePath()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(com.realgear.icons_pack.R.drawable.ic_album_24px)
                .into(this.m_vImageView_Art);
    }
}
