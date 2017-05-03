package co.vamospues.vamospues.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import co.vamospues.vamospues.R;

public class VideoPlayOnSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private MediaPlayer mediaPlayer;
    private boolean has_started = false;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoPlayOnSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public VideoPlayOnSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VideoPlayOnSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoPlayOnSurfaceView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mediaPlayer = new MediaPlayer();
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.party);
        try {
            if (!has_started) {
                has_started = true;
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            }

            mediaPlayer.prepare();
            mediaPlayer.setDisplay(holder);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override public void surfaceDestroyed(SurfaceHolder holder) {
        mediaPlayer.stop();
    }
}
