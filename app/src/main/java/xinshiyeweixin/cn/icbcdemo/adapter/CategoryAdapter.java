package xinshiyeweixin.cn.icbcdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.bean.CategoryBean;
import xinshiyeweixin.cn.icbcdemo.listener.CategoryItemOnclickListener;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHoler> {
    private Context mContext;
    private ArrayList<CategoryBean> categoryList;
    private CategoryItemOnclickListener categoryItemOnclickListener;
    private int currentPosition;

    public CategoryAdapter(Context mContext, ArrayList<CategoryBean> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.categoryItemOnclickListener = (CategoryItemOnclickListener) mContext;
        currentPosition = 0;
    }

    @Override
    public void onBindViewHolder(final MyViewHoler holder, final int position) {

        //相当于listview的adapter中的getview方法

        holder.category.setText(categoryList.get(position).name);
        if (position == currentPosition) {
            holder.item_category_flag.setVisibility(View.VISIBLE);
        } else {
            holder.item_category_flag.setVisibility(View.INVISIBLE);
        }

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                categoryItemOnclickListener.onCategoryItemOnclick(categoryList.get(position).cat_id, position);
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
        return categoryList.size();
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
