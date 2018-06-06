package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.activity.MainActivity;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.db.GoodDAOUtil;
import xinshiyeweixin.cn.icbcdemo.listener.GoodItemOnclickListener;

public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int RECOMMEND = 5;
    public static final int NORMAL = 6;

    //    private Context context;
    private Context context;
    private ArrayList<GoodBean> goodBeanList;
    private GoodItemOnclickListener productItemOnclickListener;
    private RecyclerView.ViewHolder holder;
    private int position;


    public GoodAdapter(Context context, ArrayList<GoodBean> goodBeanList) {
        this.context = context;
        this.goodBeanList = goodBeanList;
        productItemOnclickListener = (GoodItemOnclickListener) this.context;
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
            String imagePath;
            if (TextUtils.isEmpty(goodBean.image_url_local)) {
                imagePath = goodBean.image_url;
            } else {
                imagePath = goodBean.image_url_local;
            }
            Glide.with(context).asBitmap().load(imagePath).into(recommendViewHoler.product_thum);
//            recommendViewHoler.product_thum.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
            recommendViewHoler.itemView.setTag(position);//将位置保存在tag中

            recommendViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情界面
//                    context.startActivity(new Intent(context, GoodDetailActivity.class).putExtra("GOOD", GsonUtils.convertVO2String(goodBean)));
                    productItemOnclickListener.onGoodItemClick(goodBean);
                }
            });
            recommendViewHoler.product_thum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path;
                    if (GoodDAOUtil.contains(goodBean)) {
                        //数据库中有该条数据
                        if (!TextUtils.isEmpty(goodBean.video_url_local)) {
                            path = goodBean.video_url_local;
                        } else {
                            path = goodBean.video_url;
                        }
                    } else {
                        path = goodBean.video_url;
                    }
                    if (TextUtils.isEmpty(path)) {
                        ((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "未获取到视频地址", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        productItemOnclickListener.playItemVideo(path);
                    }
                }
            });
        } else if (holder instanceof NormalViewHoler) {//普通商品
            NormalViewHoler normalViewHoler = (NormalViewHoler) holder;
            normalViewHoler.product_name_normal.setText(goodBean.name);
            normalViewHoler.product_introduction_normal.setText(goodBean.content);


            String imagePath;
            if (TextUtils.isEmpty(goodBean.image_url_local)) {
                imagePath = goodBean.image_url;
            } else {
                imagePath = goodBean.image_url_local;
            }
            Glide.with(context).asBitmap().load(imagePath).into(normalViewHoler.product_thum_normal);
            normalViewHoler.product_thum_normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path;
                    if (GoodDAOUtil.contains(goodBean)) {
                        //数据库中有该条数据
                        if (!TextUtils.isEmpty(goodBean.video_url_local)) {
                            path = goodBean.video_url_local;
                        } else {
                            path = goodBean.video_url;
                        }
                    } else {
                        path = goodBean.video_url;
                    }
                    productItemOnclickListener.playItemVideo(path);
                }
            });
            normalViewHoler.itemView.setTag(position);//将位置保存在tag中

            normalViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到详情界面
//                    context.startActivity(new Intent(context, GoodDetailActivity.class).putExtra("GOOD", GsonUtils.convertVO2String(goodBean)));
                    productItemOnclickListener.onGoodItemClick(goodBean);
                }
            });
            normalViewHoler.product_thum_normal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productItemOnclickListener.playItemVideo(goodBean.video_url);
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
        LayoutInflater inflater = LayoutInflater.from(context);
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

            Typeface tf = Typeface.createFromAsset(context.getAssets(), "MicrosoftYaHei.ttc");
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
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "PingFangSCRegular.ttf");
            product_name_normal.setTypeface(tf);
            product_introduction_normal.setTypeface(tf);

        }
    }
}
