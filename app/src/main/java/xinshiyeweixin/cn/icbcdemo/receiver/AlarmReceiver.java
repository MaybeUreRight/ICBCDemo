package xinshiyeweixin.cn.icbcdemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import xinshiyeweixin.cn.icbcdemo.service.HorizonService;

/**
 * @author: liubo
 * @date: 2018/5/10/010
 * @description: $description$
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, HorizonService.class);
        context.startService(i);
    }

}
