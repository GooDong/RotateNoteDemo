package com.dong.goood.rotatenotedemo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dong.goood.rotatenotedemo.AnimatorPath.AnimatorPath;
import com.dong.goood.rotatenotedemo.AnimatorPath.PathEvaluator;
import com.dong.goood.rotatenotedemo.AnimatorPath.PathPoint;

import java.util.ArrayList;

public class RotateNoteView extends FrameLayout {

    private static final String TAG = "AiBan_RotateNoteView";

    private AnimatorPath path;//声明动画集合

    private ArrayList<ObjectAnimator> mObjectAnimators = new ArrayList();

    private ConstraintLayout outSideCL;

    private ImageView music_info_iv;

    private MusicButtonLayout music_button_rl;

    private ImageView note_quaver_1;

    private ImageView note_quaver_2;

    private ImageView note_quaver_3;

    private ImageView note_quaver_4;

    private Rect outSideCL__rect = null;

    private Rect note_quaver_1_rect = null;

    private boolean isAnimPlaying = false;

    public RotateNoteView(@NonNull Context context) {
        super(context);
    }

    public RotateNoteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateNoteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {

        outSideCL = findViewById(R.id.out_side_cl);
        music_button_rl = findViewById(R.id.music_button_mb_rl);
        music_info_iv = findViewById(R.id.music_info_iv);
        note_quaver_1 = findViewById(R.id.note_quaver_1);
        note_quaver_2 = findViewById(R.id.note_quaver_2);
        note_quaver_3 = findViewById(R.id.note_quaver_3);
        note_quaver_4 = findViewById(R.id.note_quaver_4);
    }

    /**
     * 设置动画路径
     */
    public void setPath() {
        path = new AnimatorPath();
        path.moveTo(note_quaver_1_rect.left - outSideCL__rect.left, note_quaver_1_rect.top - outSideCL__rect.top);
        path.secondBesselCurveTo(0, outSideCL__rect.centerY() - outSideCL__rect.top, (outSideCL__rect.centerX() - outSideCL__rect.left) >> 1, 0);
    }

    public void stopAnim() {
        if (null != mObjectAnimators) {

            for (ObjectAnimator mObjectAnimator : mObjectAnimators) {
                mObjectAnimator.end();
            }
            note_quaver_1.setVisibility(View.INVISIBLE);
            note_quaver_2.setVisibility(View.INVISIBLE);
            note_quaver_3.setVisibility(View.INVISIBLE);
            note_quaver_4.setVisibility(View.INVISIBLE);
        }
        music_button_rl.changeAnimStatus();

        isAnimPlaying = false;
    }

    public void setMusicInfoAvatar(Drawable musicInfoAvatarDrawable) {
        music_info_iv.setImageDrawable(musicInfoAvatarDrawable);
    }

    public void startAnim() {

        if (isAnimPlaying) {
            stopAnim();
            return;
        }

        music_button_rl.changeAnimStatus();

        if (null != mObjectAnimators && mObjectAnimators.size() > 0) {
            for (ObjectAnimator mObjectAnimator : mObjectAnimators) {
                mObjectAnimator.start();
            }
            note_quaver_1.setVisibility(View.VISIBLE);
            note_quaver_2.setVisibility(View.VISIBLE);
            note_quaver_3.setVisibility(View.VISIBLE);
            note_quaver_4.setVisibility(View.VISIBLE);

            isAnimPlaying = true;
        } else {

            outSideCL__rect = new Rect();
            outSideCL.getGlobalVisibleRect(outSideCL__rect);

            note_quaver_1_rect = new Rect();
            note_quaver_1.getGlobalVisibleRect(note_quaver_1_rect);

            setPath();

            PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("Rotation", -90, 90);

            PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1, 0.6f, 0.2f, 0);

            PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("ScaleX", 0f, 1);

            PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("ScaleY", 0f, 1);

            mObjectAnimators = new ArrayList<>();
            mObjectAnimators.add(startObjectAnim(note_quaver_1, MainActivity.DURATION_TIME, 0, rotationHolder, alphaHolder, scaleXHolder, scaleYHolder));
            mObjectAnimators.add(startPathAnim("note_quaver_1",MainActivity.DURATION_TIME, 0, path));

            mObjectAnimators.add(startObjectAnim(note_quaver_2, MainActivity.DURATION_TIME, MainActivity.NOTE_DELAY_TIME, rotationHolder, alphaHolder, scaleXHolder, scaleYHolder));
            mObjectAnimators.add(startPathAnim("note_quaver_2", MainActivity.DURATION_TIME, MainActivity.NOTE_DELAY_TIME, path));

            mObjectAnimators.add(startObjectAnim(note_quaver_3, MainActivity.DURATION_TIME, MainActivity.NOTE_DELAY_TIME * 2, rotationHolder, alphaHolder, scaleXHolder, scaleYHolder));
            mObjectAnimators.add(startPathAnim("note_quaver_3", MainActivity.DURATION_TIME, MainActivity.NOTE_DELAY_TIME * 2, path));

            mObjectAnimators.add(startObjectAnim(note_quaver_4, MainActivity.DURATION_TIME, MainActivity.NOTE_DELAY_TIME * 3, rotationHolder, alphaHolder, scaleXHolder, scaleYHolder));
            mObjectAnimators.add(startPathAnim("note_quaver_4", MainActivity.DURATION_TIME, MainActivity.NOTE_DELAY_TIME* 3, path));

            isAnimPlaying = true;
        }
    }

    private ObjectAnimator startPathAnim(String note_quaver, long durationTime, long delayTime, AnimatorPath path) {
        ObjectAnimator anim4 = ObjectAnimator.ofObject(this, note_quaver, new PathEvaluator(), path.getPoints().toArray());
        anim4.setStartDelay(delayTime);
        anim4.setDuration(durationTime);
        anim4.setRepeatMode(ObjectAnimator.RESTART);
        anim4.setRepeatCount(ObjectAnimator.INFINITE);
        anim4.start();
        return anim4;
    }

    private ObjectAnimator startObjectAnim(ImageView note_quaver, long durationTime, long delayTime, PropertyValuesHolder... mPropertyValuesHolders) {
        ObjectAnimator anim_note_quaver_4 = ObjectAnimator.ofPropertyValuesHolder(note_quaver, mPropertyValuesHolders);
        anim_note_quaver_4.setInterpolator(new DecelerateInterpolator());
        anim_note_quaver_4.setStartDelay(delayTime);
        anim_note_quaver_4.setDuration(durationTime);
        anim_note_quaver_4.setRepeatMode(ObjectAnimator.RESTART);
        anim_note_quaver_4.setRepeatCount(ObjectAnimator.INFINITE);
        anim_note_quaver_4.start();
        return anim_note_quaver_4;
    }

    /**
     * 设置View的属性通过ObjectAnimator.ofObject()的反射机制来调用
     */
    public void setNote_quaver_1(PathPoint newLoc) {
        if (View.INVISIBLE == note_quaver_1.getVisibility()) {
            note_quaver_1.setVisibility(View.VISIBLE);
        }
        note_quaver_1.setX(newLoc.mX);
        note_quaver_1.setY(newLoc.mY);
    }

    public void setNote_quaver_2(PathPoint newLoc) {
        if (View.INVISIBLE == note_quaver_2.getVisibility()) {
            note_quaver_2.setVisibility(View.VISIBLE);
        }
        note_quaver_2.setX(newLoc.mX);
        note_quaver_2.setY(newLoc.mY);
    }

    public void setNote_quaver_3(PathPoint newLoc) {
        if (View.INVISIBLE == note_quaver_3.getVisibility()) {
            note_quaver_3.setVisibility(View.VISIBLE);
        }
        note_quaver_3.setX(newLoc.mX);
        note_quaver_3.setY(newLoc.mY);
    }

    public void setNote_quaver_4(PathPoint newLoc) {
        if (View.INVISIBLE == note_quaver_4.getVisibility()) {
            note_quaver_4.setVisibility(View.VISIBLE);
        }
        note_quaver_4.setX(newLoc.mX);
        note_quaver_4.setY(newLoc.mY);
    }

}
