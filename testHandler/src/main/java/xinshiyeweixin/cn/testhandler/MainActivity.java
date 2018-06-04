package xinshiyeweixin.cn.testhandler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

        String a = " {\"id\":9,\"name\":\"蓝宝石戒指\",\"cat_id\":8,\"cat_name\":\"分类1\",\"unit\":\"枚\",\"market_price\":6000,\"our_price\":5000,\"content\":\"自古以来蓝宝石就有“帝王之石”之称。蓝宝石让几乎每一个时代的皇室被其吸引，并将之视为保佑圣物和典藏珍品。传说蓝宝石可让佩戴者免于遭人妒忌，并可蒙受神灵垂爱，于是古代国王就在颈间配戴蓝宝石，作为避免受伤的强力防御物。\",\"image_url\":\"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a736b3ec3.png\",\"video_url\":\"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59b8a7599ca58.avi\",\"ercode_img_url\":\"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a3100557295c.jpg\",\"sale_url\":\"http://mall.icbc.com.cn/\"}";
        Log.i("testHandler", "\r\n" + a);
        Log.i("testHandler", "\r\n" + a.replace("\"id\"", "\"good_id\""));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SimpleActivity.class));
            }
        });
    }
}
