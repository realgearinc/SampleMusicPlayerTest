package com.realgear.samplemusicplayertest.ui.adapters.models;

import com.realgear.mediaplayer.model.Song;

public class SongRecyclerViewItem extends BaseRecyclerViewItem {

    private Song m_vItem;

    public SongRecyclerViewItem(Song song) {
        super(song.getTitle(), ItemType.SONG);

        this.m_vItem = song;
    }

    public String getFilePath() {
        return this.m_vItem.getData();
    }

    @Override
    public int getHashCode() {
        int result = String.valueOf(this.m_vItem.getId()).hashCode();
        result = 31 * result * this.m_vItem.getTitle().hashCode();
        result = 31 * result * this.m_vItem.getData().hashCode();

        return result;
    }

    public Integer getSongId() {
        return (int)this.m_vItem.getId();
    }
}
