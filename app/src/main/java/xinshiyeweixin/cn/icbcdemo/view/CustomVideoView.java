package xinshiyeweixin.cn.icbcdemo.view;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 *
 */
public class CustomVideoView extends VideoView {
    private Context context;

    public CustomVideoView(Context context) {
        super(context);
        this.context = context;
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 其实就是在这里做了一些处理。
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();
//        Display display = displays[0];
//        int width = display.getWidth();//1280
//        int height = display.getHeight();//960
//
//
        Display display1 = displays[1];
        int width1 = display1.getWidth();//1280
        int height1 = display1.getHeight();//720

        int width = wm.getDefaultDisplay().getWidth();
        setMeasuredDimension(width1, height1);
    }
}
