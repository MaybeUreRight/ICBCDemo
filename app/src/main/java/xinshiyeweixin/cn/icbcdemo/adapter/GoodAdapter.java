package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.activity.GoodDetailActivity;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.db.DAOUtil;
import xinshiyeweixin.cn.icbcdemo.listener.GoodItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.utils.GsonUtils;

public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int RECOMMEND = 5;
    public static final int NORMAL = 6;

    private Context mContext;
    private ArrayList<GoodBean> goodBeanList;
    private GoodItemOnclickListener productItemOnclickListener;
    private RecyclerView.ViewHolder holder;
    private int position;


    public GoodAdapter(Context context, ArrayList<GoodBean> goodBeanList) {
        mContext = context;
        this.goodBeanList = goodBeanList;
        productItemOnclickListener = (GoodItemOnclickListener) mContext;
        if (this.goodBeanList == null) {
            this.goodBeanList = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //相当于listview的adapter中的getview方法
        final GoodBean goodBean = goodBeanList.get(position);

        if (holder instanceof RecommendViewHoler) {//推荐商品
            RecommendViewHoler recommendViewHoler = (RecommendViewHoler) holder;
            recommendViewHoler.product_name.setText(goodBean.name);
            recommendViewHoler.product_introduction.setText(goodBean.content);
            Glide.with(mContext).asBitmap().load(goodBean.image_url).into(recommendViewHoler.product_thum);
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
                    mContext.startActivity(new Intent(mContext, GoodDetailActivity.class).putExtra("GOOD", GsonUtils.convertVO2String(goodBean)));
                }
            });
            recommendViewHoler.product_thum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path;
                    if (DAOUtil.contains(goodBean)) {
                        //数据库中有该条数据
                        if (!TextUtils.isEmpty(goodBean.video_url_local)) {
                            path = goodBean.video_url_local;
                        } else {
                            path = goodBean.video_url;
                        }
                    } else {
                        path = goodBean.video_url;
                    }
                    productItemOnclickListener.onGoodItemOnclick(path);
                }
            });
        } else if (holder instanceof NormalViewHoler) {//普通商品
            NormalViewHoler normalViewHoler = (NormalViewHoler) holder;
            normalViewHoler.product_name_normal.setText(goodBean.name);
            normalViewHoler.product_introduction_normal.setText(goodBean.content);


            Glide.with(mContext).asBitmap().load(goodBean.image_url).into(normalViewHoler.product_thum_normal);
            normalViewHoler.product_thum_normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path;
                    if (DAOUtil.contains(goodBean)) {
                        //数据库中有该条数据
                        if (!TextUtils.isEmpty(goodBean.video_url_local)) {
                            path = goodBean.video_url_local;
                        } else {
                            path = goodBean.video_url;
                        }
                    } else {
                        path = goodBean.video_url;
                    }
                    productItemOnclickListener.onGoodItemOnclick(path);
                }
            });
            normalViewHoler.itemView.setTag(position);//将位置保存在tag中

            normalViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情界面
                    mContext.startActivity(new Intent(mContext, GoodDetailActivity.class).putExtra("GOOD", GsonUtils.convertVO2String(goodBean)));
                }
            });
            normalViewHoler.product_thum_normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productItemOnclickListener.onGoodItemOnclick(goodBean.video_url);
                }
            });
        } else {//其他类型ITEM

        }


    }

    @Override
    public int getItemViewType(int position) {
//        boolean recommend = goodBeanList.get(position).getRecommend();
        boolean recommend;
        if (position == 0) {
            recommend = true;
        } else {
            recommend = false;
        }
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
        return goodBeanList.size();
    }

    class RecommendViewHoler extends RecyclerView.ViewHolder {
        //推荐Item的布局
        private TextView product_name;
        private ImageView product_thum;
        private TextView product_introduction;

        public RecommendViewHoler(View itemView) {
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_introduction = (TextView) itemView.findViewById(R.id.item_procut_introduction);
            product_thum = itemView.findViewById(R.id.item_procut_thum);

            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "MicrosoftYaHei.ttc");
            product_name.setTypeface(tf);
            product_introduction.setTypeface(tf);
        }
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        //普通商品的Item
        private TextView product_name_normal;
        private ImageView product_thum_normal;
        private TextView product_introduction_normal;

        public NormalViewHoler(View itemView) {
            super(itemView);
            product_name_normal = (TextView) itemView.findViewById(R.id.product_name_normal);
            product_introduction_normal = (TextView) itemView.findViewById(R.id.item_procut_introduction_normal);
            product_thum_normal = itemView.findViewById(R.id.item_procut_thum_normal);
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "PingFangSCRegular.ttf");
            product_name_normal.setTypeface(tf);
            product_introduction_normal.setTypeface(tf);

        }
    }
}
