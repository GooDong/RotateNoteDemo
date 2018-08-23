package com.dong.goood.rotatenotedemo;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.dong.goood.rotatenotedemo.AnimatorPath.AnimatorPath;
import com.dong.goood.rotatenotedemo.AnimatorPath.PathEvaluator;
import com.dong.goood.rotatenotedemo.AnimatorPath.PathPoint;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static final int DURATION_TIME = 4 * 1000;

    public static final int NOTE_COUNT = 4;

    public static final int NOTE_DELAY_TIME = DURATION_TIME / NOTE_COUNT;


    ConstraintLayout outSideCL;
    RotateNoteView mRotateNoteView;

    ImageView note_quaver_1;

    ImageView note_quaver_2;

    ImageView note_quaver_3;

    ImageView note_quaver_4;

    Rect outSideCL__rect = new Rect();

    Rect note_quaver_1_rect = new Rect();

    private AnimatorPath path;//声明动画集合

    private ArrayList<ObjectAnimator> mObjectAnimators = new ArrayList();

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.dioramaloop);
        mPlayer.setLooping(true);

        //final MusicButton btnMusic = (MusicButton) findViewById(R.id.musicButton);

        final MusicButtonLayout btnMusic = findViewById(R.id.musicButtonRl);

        outSideCL = findViewById(R.id.outSideCL);
        mRotateNoteView = findViewById(R.id.rotate_note_fl);

        note_quaver_1 = findViewById(R.id.note_quaver_1);
        note_quaver_2 = findViewById(R.id.note_quaver_2);
        note_quaver_3 = findViewById(R.id.note_quaver_3);
        note_quaver_4 = findViewById(R.id.note_quaver_4);

        btnMusic.setOnClickListener(new View.OnClickListener() {//单击播放或暂停
            @Override
            public void onClick(View v) {
                btnMusic.changeAnimStatus();

                mRotateNoteView.initView();
                mRotateNoteView.setMusicInfoAvatar(getResources().getDrawable(R.drawable.ic_launcher_foreground));

                try {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                            stopAnim();
                            mRotateNoteView.stopAnim();
                        } else {
                            mPlayer.start();
                            startAnim();
                            mRotateNoteView.startAnim();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnMusic.setOnLongClickListener(new View.OnLongClickListener() {//长按停止
            @Override
            public boolean onLongClick(View v) {
                try {
                    if (mPlayer != null) {
                        mPlayer.stop();
                        mPlayer.prepare();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                btnMusic.stopMusic();
                return true;//消费此长按事件，不再向下传递
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /*设置动画路径*/
    public void setPath() {
        path = new AnimatorPath();
        path.moveTo(note_quaver_1_rect.left - outSideCL__rect.left, note_quaver_1_rect.top - outSideCL__rect.top);
        path.secondBesselCurveTo(0, outSideCL__rect.centerY() - outSideCL__rect.top, (outSideCL__rect.centerX() - outSideCL__rect.left) >> 1, 0);
    }

    private void stopAnim() {
        if (null != mObjectAnimators) {

            note_quaver_1.setVisibility(View.INVISIBLE);
            note_quaver_2.setVisibility(View.INVISIBLE);
            note_quaver_3.setVisibility(View.INVISIBLE);
            note_quaver_4.setVisibility(View.INVISIBLE);

            for (ObjectAnimator mObjectAnimator : mObjectAnimators) {
                mObjectAnimator.end();
            }
        }
    }

    private void startAnim() {

        if (null != mObjectAnimators && mObjectAnimators.size() > 0) {

            note_quaver_1.setVisibility(View.VISIBLE);
            note_quaver_2.setVisibility(View.VISIBLE);
            note_quaver_3.setVisibility(View.VISIBLE);
            note_quaver_4.setVisibility(View.VISIBLE);

            for (ObjectAnimator mObjectAnimator : mObjectAnimators) {
                mObjectAnimator.start();
            }
            return;
        }

        outSideCL.getGlobalVisibleRect(outSideCL__rect);
        note_quaver_1.getGlobalVisibleRect(note_quaver_1_rect);

        setPath();

        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("Rotation", -90, 90);

        PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1, 0.6f, 0.2f, 0);

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("ScaleX", 0f, 1);

        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("ScaleY", 0f, 1);

        mObjectAnimators.clear();
        mObjectAnimators.add(startObjectAnim(note_quaver_1, DURATION_TIME, 0, rotationHolder, alphaHolder, scaleXHolder, scaleYHolder));
        mObjectAnimators.add(startPathAnim("note_quaver_1", DURATION_TIME, 0, path));

        mObjectAnimators.add(startObjectAnim(note_quaver_2, DURATION_TIME, NOTE_DELAY_TIME, rotationHolder, alphaHolder, scaleXHolder, scaleYHolder));
        mObjectAnimators.add(startPathAnim("note_quaver_2", DURATION_TIME, NOTE_DELAY_TIME, path));

        mObjectAnimators.add(startObjectAnim(note_quaver_3, DURATION_TIME, NOTE_DELAY_TIME * 2, rotationHolder, alphaHolder, scaleXHolder, scaleYHolder));
        mObjectAnimators.add(startPathAnim("note_quaver_3", DURATION_TIME, NOTE_DELAY_TIME * 2, path));

        mObjectAnimators.add(startObjectAnim(note_quaver_4, DURATION_TIME, NOTE_DELAY_TIME * 3, rotationHolder, alphaHolder, scaleXHolder, scaleYHolder));
        mObjectAnimators.add(startPathAnim("note_quaver_4", DURATION_TIME, NOTE_DELAY_TIME * 3, path));

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
