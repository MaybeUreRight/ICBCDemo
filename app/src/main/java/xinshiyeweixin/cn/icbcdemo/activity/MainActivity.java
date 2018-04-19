package xinshiyeweixin.cn.icbcdemo.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfo;

public class MainActivity extends FragmentActivity {

    //    private IndicatorViewPager indicatorViewPager;
    private RecyclerView product_cagetory;
    private RecyclerView product_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
//        initViewPager();


        ArrayList<ProductInfo> list = new ArrayList<>();
        initData(list);

        product_cagetory = findViewById(R.id.product_category);

        product_cagetory.setHasFixedSize(true);//设置固定大小
        product_cagetory.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        LinearLayoutManager mLayoutManage = new LinearLayoutManager(this);
        mLayoutManage.setOrientation(OrientationHelper.HORIZONTAL);//设置滚动方向，横向滚动
        product_cagetory.setLayoutManager(mLayoutManage);
        RecycleViewAdapter adapter = new RecycleViewAdapter(this, R.layout.item_category, list);
        product_cagetory.setAdapter(adapter);

        product_list = findViewById(R.id.product_list);
    }

    private void initData(ArrayList<ProductInfo> list) {
        for (int i = 0; i < 10; i++) {
            ProductInfo info = new ProductInfo();
            info.cagetory = getString(R.string.item_product_category) + " -> " + i;
            info.productList = new ArrayList<>();
            for (int j = 0; j < i + i / 2; j++) {
                Product product = new Product();
                product.name = getString(R.string.item_product_name) + " -> " + j;
                product.picUrl = "https://i03piccdn.sogoucdn.com/66766b011ffe1eac";
                product.introduction = "秋田犬（拉丁学名：Japanese Akita），别名日本秋田犬、日系秋田犬，原产地日本。其祖先被称呼为山地狩猎犬，是大型的熊猎犬。除了协助猎熊外，它还被利用来捕...";
                info.productList.add(product);
            }
            list.add(info);
        }
    }

    public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHoler> {
        int ResourceID;
        Context mContext;
        ArrayList<ProductInfo> mData;
//        private OnRecycleViewItemClickListener mOnItemClickListener;

        public RecycleViewAdapter(Context context, int resourceID, ArrayList<ProductInfo> brings) {
            mContext = context;
            mData = brings;
            ResourceID = resourceID;
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

                    product_list.setHasFixedSize(true);//设置固定大小
                    product_list.setItemAnimator(new DefaultItemAnimator());//设置默认动画
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
                    gridLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
//                    LinearLayoutManager mLayoutManage = new LinearLayoutManager(MainActivity.this);
//                    mLayoutManage.setOrientation(OrientationHelper.HORIZONTAL);//设置滚动方向，横向滚动
                    product_list.setLayoutManager(gridLayoutManager);
                    RecycleViewAdapter2 adapter = new RecycleViewAdapter2(MainActivity.this, R.layout.item_product, mData.get(position).productList);
                    product_list.setAdapter(adapter);
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


    }


    public class RecycleViewAdapter2 extends RecyclerView.Adapter<RecycleViewAdapter2.MyViewHoler> {
        int ResourceID;
        Context mContext;
        ArrayList<Product> mData;
//        private OnRecycleViewItemClickListener mOnItemClickListener;

        public RecycleViewAdapter2(Context context, int resourceID, ArrayList<Product> brings) {
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
                    Toast.makeText(MainActivity.this, "点击事件", Toast.LENGTH_SHORT).show();
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

//    private void initViewPager() {
//        ViewPager viewPager = (ViewPager) findViewById(R.id.moretab_viewPager);
//        ScrollIndicatorView scrollIndicatorView = (ScrollIndicatorView) findViewById(R.id.moretab_indicator);
//
//        float unSelectSize = 12;
//        float selectSize = unSelectSize * 1.3f;
//        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFF2196F3, Color.GRAY).setSize(selectSize, unSelectSize));
//
//        scrollIndicatorView.setScrollBar(new ColorBar(this, 0xFF2196F3, 4));
//
//        viewPager.setOffscreenPageLimit(2);
//        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
//        indicatorViewPager.setAdapter(new MyAdapter());
//    }


//    private class MyAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
//        private String[] versions = {"Cupcake", "Donut", "Éclair"};
//        private String[] names = {"纸杯蛋糕", "甜甜圈", "闪电泡芙"};
//
////        private String[] versions = {"Cupcake", "Donut", "Éclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lolipop", "Marshmallow"};
////        private String[] names = {"纸杯蛋糕", "甜甜圈", "闪电泡芙", "冻酸奶", "姜饼", "蜂巢", "冰激凌三明治", "果冻豆", "奇巧巧克力棒", "棒棒糖", "棉花糖"};
//
//        @Override
//        public int getCount() {
//            return versions.length;
//        }
//
//        @Override
//        public View getViewForTab(int position, View convertView, ViewGroup container) {
//            if (convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.tab_top, container, false);
//            }
//            TextView textView = (TextView) convertView;
//            textView.setText(versions[position]);
//
//            int witdh = getTextWidth(textView);
//            int padding = DisplayUtil.dipToPix(getApplicationContext(), 8);
//            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
//            //1.3f是根据上面字体大小变化的倍数1.3f设置
//            textView.setWidth((int) (witdh * 1.3f) + padding);
//
//            return convertView;
//        }
//
//        @Override
//        public View getViewForPage(int position, View convertView, ViewGroup container) {
//            if (convertView == null) {
//                convertView = new TextView(container.getContext());
//            }
//            TextView textView = (TextView) convertView;
//            textView.setText(names[position]);
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextColor(Color.GRAY);
//            return convertView;
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
//            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
//            return PagerAdapter.POSITION_UNCHANGED;
//        }
//
//        private int getTextWidth(TextView textView) {
//            if (textView == null) {
//                return 0;
//            }
//            Rect bounds = new Rect();
//            String text = textView.getText().toString();
//            Paint paint = textView.getPaint();
//            paint.getTextBounds(text, 0, text.length(), bounds);
//            int width = bounds.left + bounds.width();
//            return width;
//        }
//
//    }
}
