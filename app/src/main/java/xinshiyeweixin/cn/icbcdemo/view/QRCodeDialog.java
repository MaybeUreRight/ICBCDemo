package xinshiyeweixin.cn.icbcdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.utils.DisplayUtil;

/**
 * 二维码扫描的弹框
 */
public class QRCodeDialog extends Dialog {

    private Context mContext;

    public QRCodeDialog(Context mContext) {
        super(mContext, R.style.QRCodeDialogStyle);
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_qrcode, null);
        setContentView(view);

        ImageView imageView = view.findViewById(R.id.imageView);
        int width = DisplayUtil.getScreenWidth(mContext) / 3;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        imageView.setLayoutParams(layoutParams);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeDialog.this.dismiss();
            }
        });
    }


}
