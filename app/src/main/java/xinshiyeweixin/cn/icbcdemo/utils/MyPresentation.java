package xinshiyeweixin.cn.icbcdemo.utils;

import android.app.Presentation;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.listener.CompleteListener;
import xinshiyeweixin.cn.icbcdemo.view.CustomVideoView;

public class MyPresentation extends Presentation implements SurfaceHolder.Callback {
    private VideoView videoView;
    private String videoFile = "";
    private Uri uri = null;
    private Context context;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private String path;
    private CompleteListener completeListener;

    public MyPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        context = outerContext;
    }

    public void setCompleteListener(CompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    /**
     * 添加布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1280*720
        setContentView(R.layout.play_videoview);
        videoView = (VideoView) findViewById(R.id.videoView);
        surfaceView = findViewById(R.id.surfaceview);
        holder = surfaceView.getHolder();
        holder.addCallback(this);
    }

    public void play(final String path) {
        this.path = path;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (holder != null) {
            holder.removeCallback(this);
        }
        try {
            if (holder == null) {
                holder = surfaceView.getHolder();
            }
            holder.addCallback(this);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
//            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.setDataSource(context, Uri.parse(path));
            // 设置显示视频的SurfaceHolder
            mediaPlayer.setDisplay(surfaceView.getHolder());
//            LogUtils.i("开始装载");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    // 按照初始位置播放
//                    mediaPlayer.seekTo(msec);
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                    int duration = mp.getDuration();
//                    Log.i("Demo", "duration = " + duration);
//                    Log.i("Demo", "(duration / 1000) = " + duration / 1000);
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
//                    Log.i("Demo", "MyPresentation >> 播放完毕");
                    mp.start();
                    completeListener.onComplete(path);
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新开始播放
     */
    public void replay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            return;
        }
//        play(0);
        play(this.path);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
