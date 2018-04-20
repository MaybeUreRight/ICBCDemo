package xinshiyeweixin.cn.icbcdemo.utils;

import android.app.Presentation;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.widget.VideoView;

import xinshiyeweixin.cn.icbcdemo.R;

public class MyPresentation extends Presentation {
    private VideoView videoView;
    private String videoFile = "";
    private Uri uri = null;

    public MyPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }


    /**
     * 添加布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_videoview);
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    /**
     * 添加方法来播放视频
     *
     * @param filePath 文件的路径
     */
    public void startVideo(String filePath) {
        this.videoFile = filePath;
//        this.uri = Uri.fromFile(new File(this.videoFile));
//        this.videoView.setVideoURI(this.uri);
        this.videoView.setVideoURI(Uri.parse(filePath));
        this.videoView.requestFocus();
        this.videoView.start();
        /**
         * 设置重播
         */
        this.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                MyPresentation.this.videoView.start();
            }
        });
    }
}
