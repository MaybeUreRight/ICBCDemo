package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfo;
import xinshiyeweixin.cn.icbcdemo.listener.ProductCategoryItemOnclickListener;

public class ProductInfoAdapter extends RecyclerView.Adapter<ProductInfoAdapter.MyViewHoler> {
    private Context mContext;
    private ArrayList<ProductInfo> productInfos;
    private ProductCategoryItemOnclickListener productCategoryItemOnclickListener;
    private int currentPosition;

    public ProductInfoAdapter(Context mContext, ArrayList<ProductInfo> productInfos) {
        this.mContext = mContext;
        this.productInfos = productInfos;
        this.productCategoryItemOnclickListener = (ProductCategoryItemOnclickListener) mContext;
        currentPosition = 0;
    }

    @Override
    public void onBindViewHolder(final MyViewHoler holder, final int position) {

        //相当于listview的adapter中的getview方法

        holder.category.setText(productInfos.get(position).cagetory);
        if (position == currentPosition) {
            holder.item_category_flag.setVisibility(View.VISIBLE);
        } else {
            holder.item_category_flag.setVisibility(View.INVISIBLE);
        }

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList list = productInfos.get(position).productList;
                if (list != null) {
                    Log.i("Demo", "list.size = " + list.size());
                } else {
                    Log.i("Demo", "list是空");
                }
                currentPosition = position;
                productCategoryItemOnclickListener.onProductCategoryItemOnclick(productInfos.get(position).productList, position);
            }
        });

        holder.itemView.setTag(position);//将位置保存在tag中
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new MyViewHoler(view);
    }


    @Override
    public int getItemCount() {
        return productInfos.size();
    }


    class MyViewHoler extends RecyclerView.ViewHolder {
        private final TextView category;
        private View item_category_flag;

        public MyViewHoler(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.item_category);
            item_category_flag = itemView.findViewById(R.id.item_category_flag);
        }
    }

}
