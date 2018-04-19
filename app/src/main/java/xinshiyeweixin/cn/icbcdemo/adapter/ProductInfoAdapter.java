package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.activity.MainActivity;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfo;

public class ProductInfoAdapter extends RecyclerView.Adapter<ProductInfoAdapter.MyViewHoler> {
    int ResourceID;
    Context mContext;
    ArrayList<ProductInfo> mData;
    private OnRecycleViewItemClickListener mOnItemClickListener;

    public ProductInfoAdapter(Context context, int resourceID, ArrayList<ProductInfo> brings, OnRecycleViewItemClickListener mOnItemClickListener) {
        mContext = context;
        mData = brings;
        ResourceID = resourceID;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, final int position) {

        //相当于listview的adapter中的getview方法

        holder.category.setText(mData.get(position).cagetory);

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList list = mData.get(position).productList;
                if (list != null) {
                    Log.i("Demo", "list.size = " + list.size());
                } else {

                    Log.i("Demo", "list是空");
                }

                mOnItemClickListener.onItemClick(mData.get(position).productList, position);
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
        private final TextView category;

        public MyViewHoler(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.item_category);

        }
    }

    public interface OnRecycleViewItemClickListener {
        void onItemClick(ArrayList<Product> list, int position);
    }
}
