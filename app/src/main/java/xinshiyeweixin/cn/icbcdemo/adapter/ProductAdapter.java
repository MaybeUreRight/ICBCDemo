package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.activity.ProductDetailActivity;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.listener.ProductItemOnclickListener;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHoler> {
    private Context mContext;
    private ArrayList<Product> mData;
    private ProductItemOnclickListener productItemOnclickListener;

    public ProductAdapter(Context context, ArrayList<Product> brings) {
        mContext = context;
        mData = brings;
        productItemOnclickListener = (ProductItemOnclickListener) mContext;
        if (mData == null) {
            mData = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {

        //相当于listview的adapter中的getview方法
        final Product product = mData.get(position);

        holder.product_name.setText(product.name);
        holder.product_introduction.setText(product.introduction);
        holder.product_thum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击事件", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setTag(position);//将位置保存在tag中

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到详情界面
                mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));
            }
        });
        holder.product_thum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productItemOnclickListener.onProductItemOnclick(product.videoPath);
            }
        });
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_product,parent,false);
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
