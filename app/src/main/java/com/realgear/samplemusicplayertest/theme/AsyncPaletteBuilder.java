package com.realgear.samplemusicplayertest.theme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.realgear.samplemusicplayertest.theme.interfaces.PaletteStateListener;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class AsyncPaletteBuilder {

    public interface Action {
        void onValueAnimated(int value);
    }

    private final int ANIM_DURATION = 650;

    private enum Vibrant_Type {
        VIBRANT, VIBRANT_DARK, VIBRANT_LIGHT
    }


    private HashMap<Vibrant_Type, ValueAnimator> mAnimators = new HashMap<>();
    private HashMap<Vibrant_Type, Integer> mDefColors = new HashMap<>();
    private HashMap<Vibrant_Type, Integer> mPrevColors = new HashMap<>();


    private final PaletteStateListener mStateListener;

    public AsyncPaletteBuilder(PaletteStateListener stateListener) {
        this.mStateListener = stateListener;

        this.mDefColors.put(Vibrant_Type.VIBRANT, Color.parseColor("#424242"));
        this.mPrevColors.put(Vibrant_Type.VIBRANT, this.mDefColors.get(Vibrant_Type.VIBRANT));
    }

    private ValueAnimator getColorAnimator(int fromColor, int toColor) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(
                (TypeEvaluator) new ArgbEvaluator(),
                new Object[] {Integer.valueOf(fromColor), Integer.valueOf(toColor)});
        valueAnimator.setDuration(ANIM_DURATION);
        return valueAnimator;
    }

    private void onStartColorAnimation(Vibrant_Type type, Action action, int colorFrom, int colorTo) {
        ValueAnimator animator;
        if (this.mAnimators.containsKey(type) && this.mAnimators.get(type) != null) {
            animator = this.mAnimators.get(type);
            animator.end();
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
        }

        animator = getColorAnimator(colorFrom, colorTo);

        this.mAnimators.put(type, animator);

        animator.addUpdateListener(valueAnimator -> {
            action.onValueAnimated((int)valueAnimator.getAnimatedValue());
        });

        animator.start();
    }

    public void onStartAnimation(Bitmap art) {
        if (art != null) {
            Palette.from(art).generate(palette -> {
                onStartColorAnimation(Vibrant_Type.VIBRANT, value -> {
                    AsyncPaletteBuilder.this.mStateListener.onUpdateVibrantColor(value);
                    AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.VIBRANT, value);
                }, this.mPrevColors.get(Vibrant_Type.VIBRANT), palette.getVibrantColor(mDefColors.get(Vibrant_Type.VIBRANT)));
            });
        }
        else {
            onStartColorAnimation(Vibrant_Type.VIBRANT, value -> {
                AsyncPaletteBuilder.this.mStateListener.onUpdateVibrantColor(value);
                AsyncPaletteBuilder.this.mPrevColors.put(Vibrant_Type.VIBRANT, value);
            }, this.mPrevColors.get(Vibrant_Type.VIBRANT), this.mDefColors.get(Vibrant_Type.VIBRANT));
        }
    }
}
