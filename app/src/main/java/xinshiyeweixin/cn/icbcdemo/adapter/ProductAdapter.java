package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.activity.ProductDetailActivity;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.listener.ProductItemOnclickListener;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int RECOMMEND = 5;
    public static final int NORMAL = 6;

    private Context mContext;
    private ArrayList<Product> products;
    private ProductItemOnclickListener productItemOnclickListener;
    private RecyclerView.ViewHolder holder;
    private int position;


    public ProductAdapter(Context context, ArrayList<Product> brings) {
        mContext = context;
        products = brings;
        productItemOnclickListener = (ProductItemOnclickListener) mContext;
        if (products == null) {
            products = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //相当于listview的adapter中的getview方法
        final Product product = products.get(position);

        if (holder instanceof RecommendViewHoler) {//推荐商品
            RecommendViewHoler recommendViewHoler = (RecommendViewHoler) holder;
            recommendViewHoler.product_name.setText(product.getName());
            recommendViewHoler.product_introduction.setText(product.getIntroduction());
            recommendViewHoler.product_thum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "点击事件", Toast.LENGTH_SHORT).show();
                }
            });
            recommendViewHoler.itemView.setTag(position);//将位置保存在tag中

            recommendViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情界面
                    mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));
                }
            });
            recommendViewHoler.product_thum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productItemOnclickListener.onProductItemOnclick(product.getVideoPath());
                }
            });
        } else if (holder instanceof NormalViewHoler) {//普通商品
            NormalViewHoler normalViewHoler = (NormalViewHoler) holder;
            normalViewHoler.product_name_normal.setText(product.getName());
            normalViewHoler.product_introduction_normal.setText(product.getIntroduction());
            normalViewHoler.product_thum_normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "点击事件", Toast.LENGTH_SHORT).show();
                }
            });
            normalViewHoler.itemView.setTag(position);//将位置保存在tag中

            normalViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情界面
                    mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));
                }
            });
            normalViewHoler.product_thum_normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productItemOnclickListener.onProductItemOnclick(product.getVideoPath());
                }
            });
        } else {//其他类型ITEM

        }


    }

    @Override
    public int getItemViewType(int position) {
        boolean recommend = products.get(position).getRecommend();
        if (recommend) {//推荐
            return RECOMMEND;
        } else {//普通
            return NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = null;
        //TODO 不同ITEM的布局
        if (viewType == RECOMMEND) {//推荐
            view = inflater.inflate(R.layout.item_product_recommend, parent, false);
            return new RecommendViewHoler(view);
        } else {//普通
            view = inflater.inflate(R.layout.item_product_normal, parent, false);
            return new NormalViewHoler(view);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class RecommendViewHoler extends RecyclerView.ViewHolder {
        private TextView product_name;
        private ImageView product_thum;
        private TextView product_introduction;

        public RecommendViewHoler(View itemView) {
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_introduction = (TextView) itemView.findViewById(R.id.item_procut_introduction);
            product_thum = itemView.findViewById(R.id.item_procut_thum);
            int height = itemView.getHeight();
            int div = (height - product_name.getHeight() - product_introduction.getHeight() - product_thum.getHeight()) / 2;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) product_thum.getLayoutParams();
        }
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        private TextView product_name_normal;
        private ImageView product_thum_normal;
        private TextView product_introduction_normal;

        public NormalViewHoler(View itemView) {
            super(itemView);
            product_name_normal = (TextView) itemView.findViewById(R.id.product_name_normal);
            product_introduction_normal = (TextView) itemView.findViewById(R.id.item_procut_introduction_normal);
            product_thum_normal = itemView.findViewById(R.id.item_procut_thum_normal);
            int height = itemView.getHeight();
            int div = (height - product_name_normal.getHeight() - product_introduction_normal.getHeight() - product_thum_normal.getHeight()) / 2;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) product_thum_normal.getLayoutParams();
        }
    }
}
