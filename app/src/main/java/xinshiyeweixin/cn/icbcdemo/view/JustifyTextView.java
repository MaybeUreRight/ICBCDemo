package xinshiyeweixin.cn.icbcdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @ClassName [com.example.shao.textviewdemo].
 * @Descriptione[两端对齐  的textview ]
 * @Author [shao on 2017/7/17].
 * @Date [2017/7/17 20:47]
 * @Version [v1.0]
 *
 *
 *
 * 平均间距 =（四个字的宽度-两个字的宽度）/ (2-1)然后设置每个字的间距为平均间距
 */
public class JustifyTextView extends TextView {

    private int mLineY;
    private float mViewWidth = 0;

    public JustifyTextView(Context context) {
        super(context);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        String text = (String) getText();
        mLineY = 0;
        mLineY += getTextSize();
        float width = StaticLayout.getDesiredWidth(text, 0, text.length(), getPaint());
        drawScaledText(canvas, text, width);
        Paint.FontMetrics fm = paint.getFontMetrics();
        int textHeight = (int) fm.top;
        mLineY += textHeight;
    }

    private void drawScaledText(Canvas canvas, String line, float lineWidth) {
        float x = 0;
//        平均间距 =（四个字的宽度-两个字的宽度）/ (2-1)然后设置每个字的间距为平均间距
        float d = (mViewWidth - lineWidth) / (line.length() - 1);

        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }

    public void setTitleWidth( TextView tv ){
        String text = (String) tv.getText();
        float width = StaticLayout.getDesiredWidth(text, 0, text.length(), tv.getPaint());
        mViewWidth = width;
        setWidth((int) mViewWidth);
        invalidate();
    }
}
