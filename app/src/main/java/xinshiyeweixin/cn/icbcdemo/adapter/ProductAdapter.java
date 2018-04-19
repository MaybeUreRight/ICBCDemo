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
import xinshiyeweixin.cn.icbcdemo.activity.MainActivity;
import xinshiyeweixin.cn.icbcdemo.bean.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHoler> {
    int ResourceID;
    Context mContext;
    ArrayList<Product> mData;
//        private OnRecycleViewItemClickListener mOnItemClickListener;

    public ProductAdapter(Context context, int resourceID, ArrayList<Product> brings) {
        mContext = context;
        mData = brings;
        if (mData == null) {
            mData = new ArrayList<>();
        }
        ResourceID = resourceID;
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {

        //相当于listview的adapter中的getview方法

        holder.product_name.setText(mData.get(position).name);
        holder.product_introduction.setText(mData.get(position).introduction);
        holder.product_thum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击事件", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setTag(position);//将位置保存在tag中
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        View view = LayoutInflater.from(mContext).inflate(ResourceID, null);
        return new MyViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHoler extends RecyclerView.ViewHolder {
        private TextView product_name;
        private ImageView product_thum;
        private TextView product_introduction;

        public MyViewHoler(View itemView) {
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_introduction = (TextView) itemView.findViewById(R.id.item_procut_introduction);
            product_thum = itemView.findViewById(R.id.item_procut_thum);

        }
    }
}
