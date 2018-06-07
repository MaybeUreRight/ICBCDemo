package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.listener.DetailItemClickListener;

public class GoodDetailAdapter extends RecyclerView.Adapter<GoodDetailAdapter.MyViewHoler> {
    private Context mContext;
    private ArrayList<GoodBean> detailBeans;
    private DetailItemClickListener detailItemClickListener;

    public GoodDetailAdapter(Context mContext, ArrayList<GoodBean> detailBeans) {
        this.mContext = mContext;
        this.detailBeans = detailBeans;
        if (detailBeans == null) {
            this.detailBeans = new ArrayList<>();
        }

        if (mContext instanceof DetailItemClickListener) {
            detailItemClickListener = (DetailItemClickListener) mContext;
        }

    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {

        //相当于listview的adapter中的getview方法
        final GoodBean goodBean = detailBeans.get(position);

        Glide.with(mContext).asBitmap().load(goodBean.image_url).into(holder.itemDetailPic);
        holder.itemDetailShortcut.setText(goodBean.name);
        holder.itemDetailDesc.setText(goodBean.content);

        holder.itemView.setTag(position);//将位置保存在tag中
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, GoodDetailActivity.class).putExtra("GOOD",GsonUtils.convertVO2String(goodBean)));
                detailItemClickListener.onDetailItemClick(goodBean);
            }
        });
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_detail, parent, false);
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

            itemDetailPic = (ImageView) itemView.findViewById(R.id.item_detail_pic);
            itemDetailShortcut = (TextView) itemView.findViewById(R.id.item_detail_shortcut);
            itemDetailDesc = (TextView) itemView.findViewById(R.id.item_detail_desc);

            Typeface pingFangRegulaer = Typeface.createFromAsset(mContext.getAssets(), "PingFangSCRegular.ttf");
            itemDetailShortcut.setTypeface(pingFangRegulaer);
            itemDetailDesc.setTypeface(pingFangRegulaer);
        }
    }
}