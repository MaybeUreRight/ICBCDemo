package xinshiyeweixin.cn.testhandler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * @author: liubo
 * @date: 2018/5/10/010
 * @description: $description$
 */
public class BaseActivity extends AppCompatActivity {
    protected Handler handler;
    protected String TAG = "BaseActivity";
    protected Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case 101:
                        Log.i(TAG, "" + activity.getLocalClassName());
                        sendEmptyMessageDelayed(101, 5 * 1000);
                        break;
//                    case 102:
//
//                        break;
                    default:
                        break;
                }
            }
        };
        handler.sendEmptyMessageDelayed(101, 5 * 1000);
    }
}
