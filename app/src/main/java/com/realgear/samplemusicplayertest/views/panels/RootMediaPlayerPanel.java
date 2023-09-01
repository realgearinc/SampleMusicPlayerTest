package com.realgear.samplemusicplayertest.views.panels;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.realgear.extensions.bottomsheet.CustomBottomSheetBehavior;
import com.realgear.multislidinguppanel.BasePanelView;
import com.realgear.multislidinguppanel.IPanel;
import com.realgear.multislidinguppanel.MultiSlidingUpPanelLayout;
import com.realgear.samplemusicplayertest.R;
import com.realgear.samplemusicplayertest.theme.AsyncPaletteBuilder;
import com.realgear.samplemusicplayertest.theme.interfaces.PaletteStateListener;
import com.realgear.samplemusicplayertest.threads.MediaPlayerThread;
import com.realgear.samplemusicplayertest.views.mediaviews.MediaPlayerBarView;
import com.realgear.samplemusicplayertest.views.mediaviews.MediaPlayerView;

public class RootMediaPlayerPanel extends BasePanelView implements PaletteStateListener {

    private MediaPlayerView mMediaPlayerView;
    private MediaPlayerBarView mMediaPlayerBarView;

    private AsyncPaletteBuilder mAsyncPaletteBuilder;

    public RootMediaPlayerPanel(@NonNull Context context, MultiSlidingUpPanelLayout panelLayout) {
        super(context, panelLayout);

        getContext().setTheme(R.style.Theme_SampleMusicPlayerTest);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_root_media_player, this, true);

        this.mAsyncPaletteBuilder = new AsyncPaletteBuilder(this);
    }

    @Override
    public void onCreateView() {
        this.setPanelState(MultiSlidingUpPanelLayout.HIDDEN);
        this.setSlideDirection(MultiSlidingUpPanelLayout.SLIDE_VERTICAL);

        this.setPeakHeight(getResources().getDimensionPixelSize(R.dimen.media_player_bar_height));
        this.setUserHiddenMode(true);
    }

    @Override
    public void onBindView() {
        mMediaPlayerView = new MediaPlayerView(findViewById(R.id.media_player_view));
        mMediaPlayerBarView = new MediaPlayerBarView(findViewById(R.id.media_player_bar_view));

        DisplayMetrics dm = getResources().getDisplayMetrics();
        FrameLayout layout = findViewById(R.id.media_player_bottom_sheet_behavior);

        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = dm.heightPixels - (mPeakHeight);
        layout.setLayoutParams(params);

        CustomBottomSheetBehavior<FrameLayout> bottomSheetBehavior = CustomBottomSheetBehavior.from(layout);
        bottomSheetBehavior.setSkipAnchored(false);
        bottomSheetBehavior.setAllowUserDragging(true);

        float anchor_offset = 0.80F;

        bottomSheetBehavior.setAnchorOffset((int)((dm.heightPixels - mPeakHeight) * anchor_offset));
        bottomSheetBehavior.setPeekHeight(getPeakHeight());
        //bottomSheetBehavior.setMediaPlayerBarHeight(getPeakHeight());
        bottomSheetBehavior.setState(CustomBottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.addBottomSheetCallback(new CustomBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int oldState, int newState) {
                switch (newState) {
                    case CustomBottomSheetBehavior.STATE_COLLAPSED:
                        mMediaPlayerBarView.getRootView().setZ(0F);
                        getMultiSlidingUpPanel().setSlidingEnabled(true);
                        break;
                    case CustomBottomSheetBehavior.STATE_ANCHORED:
                        getMultiSlidingUpPanel().setSlidingEnabled(false);
                        mMediaPlayerBarView.getRootView().setZ(0F);
                        break;
                    case CustomBottomSheetBehavior.STATE_EXPANDED:
                        mMediaPlayerBarView.getRootView().setZ(100F);
                        getMultiSlidingUpPanel().setSlidingEnabled(false);
                        break;
                    case CustomBottomSheetBehavior.STATE_DRAGGING:
                        getMultiSlidingUpPanel().setSlidingEnabled(false);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float fadeStart = bottomSheetBehavior.getSlideOffsetByTop(bottomSheetBehavior.getTopByState(CustomBottomSheetBehavior.STATE_ANCHORED));
                float alpha = Math.max(0, slideOffset - fadeStart) / (1F - fadeStart) ;

                mMediaPlayerView.onSliding(slideOffset, MediaPlayerView.STATE_PARTIAL);
                mMediaPlayerBarView.onSliding(alpha, MediaPlayerBarView.STATE_PARTIAL);

                Log.i("ALPHA", "Slide Offset : " + slideOffset);
                Log.i("ALPHA", "Alpha : " + alpha);

                /*if (alpha > 0.0F) {
                    mMediaPlayerBarView.getRootView().setVisibility(VISIBLE);
                }
                else {
                    mMediaPlayerBarView.getRootView().setVisibility(View.INVISIBLE);
                }*/
            }
        });
    }

    @Override
    public void onPanelStateChanged(int panelSate) {
        if (this.mMediaPlayerView != null)
            this.mMediaPlayerView.onPanelStateChanged(panelSate);
        if (this.mMediaPlayerBarView != null)
            this.mMediaPlayerBarView.onPanelStateChanged(panelSate);

        if (panelSate == MultiSlidingUpPanelLayout.HIDDEN) {
            if (MediaPlayerThread.getInstance() != null && MediaPlayerThread.getInstance().getCallback() != null)
                MediaPlayerThread.getInstance().getCallback().onClickStop();
        }
    }

    public void onUpdateMetadata(MediaMetadata mediaMetadata) {
        this.mMediaPlayerBarView.onUpdateMetadata(mediaMetadata);

        Bitmap bitmap = mediaMetadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART);
        this.mAsyncPaletteBuilder.onStartAnimation(bitmap);
    }

    public void onPlaybackStateChanged(PlaybackState state) {
        if (state.getState() == PlaybackState.STATE_PLAYING || state.getState() == PlaybackState.STATE_PAUSED) {
            if (this.getPanelState() == MultiSlidingUpPanelLayout.HIDDEN)
                this.collapsePanel();
        }

        this.mMediaPlayerBarView.onPlaybackStateChanged(state);
    }

    @Override
    public void onSliding(@NonNull IPanel<View> panel, int top, int dy, float slidingOffset) {
        super.onSliding(panel, top, dy, slidingOffset);

        mMediaPlayerView.onSliding(slidingOffset, MediaPlayerView.STATE_NORMAL);
        mMediaPlayerBarView.onSliding(slidingOffset, MediaPlayerBarView.STATE_NORMAL);
    }

    @Override
    public void onUpdateVibrantColor(int vibrantColor) {
        this.mMediaPlayerBarView.onUpdateVibrantColor(vibrantColor);
    }

    @Override
    public void onUpdateVibrantDarkColor(int vibrantDarkColor) {
        this.mMediaPlayerBarView.onUpdateVibrantDarkColor(vibrantDarkColor);
    }

    @Override
    public void onUpdateVibrantLightColor(int vibrantLightColor) {

    }

    @Override
    public void onUpdateMutedColor(int mutedColor) {
        this.mMediaPlayerBarView.onUpdateMutedColor(mutedColor);
    }

    @Override
    public void onUpdateMutedDarkColor(int mutedDarkColor) {
        this.mMediaPlayerBarView.onUpdateMutedDarkColor(mutedDarkColor);
    }
}
