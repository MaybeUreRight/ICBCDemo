package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.bean.DetailBean;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHoler> {
    private Context mContext;
    private ArrayList<DetailBean> detailBeans;

    public DetailAdapter(Context mContext, ArrayList<DetailBean> detailBeans) {
        this.mContext = mContext;
        this.detailBeans = detailBeans;
        if (detailBeans == null) {
            detailBeans = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {

        //相当于listview的adapter中的getview方法
        final DetailBean detailBean = detailBeans.get(position);

        holder.itemDetailShortcut.setText(detailBean.shortcut);
        holder.itemDetailDesc.setText(detailBean.description);

        holder.itemView.setTag(position);//将位置保存在tag中
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail, null);
        return new MyViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return detailBeans.size();
    }

    class MyViewHoler extends RecyclerView.ViewHolder {
        private ImageView itemDetailPic;
        private TextView itemDetailShortcut;
        private TextView itemDetailDesc;

        public MyViewHoler(View itemView) {
            super(itemView);

            itemDetailPic = (ImageView)  itemView.findViewById(R.id.item_detail_pic);
            itemDetailShortcut = (TextView)  itemView.findViewById(R.id.item_detail_shortcut);
            itemDetailDesc = (TextView)  itemView.findViewById(R.id.item_detail_desc);
        }
    }
}