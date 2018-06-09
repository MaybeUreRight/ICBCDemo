package xinshiyeweixin.cn.icbcdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.utils.DisplayUtil;

/**
 * 二维码扫描的弹框
 */
public class QRCodeDialog extends Dialog {

    private Context mContext;
    private String url;


    public QRCodeDialog(Context mContext) {
        super(mContext, R.style.QRCodeDialogStyle);
        this.mContext = mContext;
    }

    public QRCodeDialog(Context mContext, String url) {
        super(mContext, R.style.QRCodeDialogStyle);
        this.mContext = mContext;
        this.url = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_qrcode, null);
        int height = (int) (DisplayUtil.getScreenHeight(mContext) * 2.0 / 3);
        int width = (int) (DisplayUtil.getScreenWidth(mContext) * 2.0 / 3);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(height, width);
        view.setLayoutParams(layoutParams);
        setContentView(view);

        WebView webView = view.findViewById(R.id.webview);
        webView.loadUrl(url);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeDialog.this.dismiss();
            }
        });
    }


}
