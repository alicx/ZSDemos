package com.leaf8.zishao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leaf8.zishao.R;
import com.leaf8.zishao.bean.ZsMenuItem;

/**
 * 条件的适配器
 * 
 * @author 紫韶
 * @date Jun 28, 2013
 **/
public class ZsConditionAdapter extends BaseAdapter {

	private Context mContext;
	public List<ZsMenuItem> mData = new ArrayList<ZsMenuItem>();
	private boolean mIsCount = false;
	// private int[] mImages;

	private int mSelectedPosition = -1;
	private int mItemLayout;

	public ZsConditionAdapter(Context context, int layout) {
		this.mContext = context;
		mItemLayout = layout;
	}

	/**
	 * 刷新数据
	 * 
	 * @author 紫韶
	 * @date Jun 28, 2013
	 * @param mData
	 * @param images
	 */
	public void refresh(List<ZsMenuItem> mData, int[] images, boolean isCount) {
		this.mData = mData;
		// this.mImages = images;
		this.mIsCount = isCount;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mItemLayout, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.textview);
			holder.imageView = (ImageView) convertView.findViewById(R.id.image_line);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.colorlayout);
			// holder.countTv = (TextView) convertView.findViewById(R.id.tc_condition_count);
			holder.subView = (ImageView) convertView.findViewById(R.id.tc_condition_sub);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置选中效果
		if (mSelectedPosition == position) {
			holder.layout.setBackgroundColor(Color.parseColor("#efefef"));
			holder.imageView.setVisibility(View.VISIBLE);
		} else {
			holder.layout.setBackgroundColor(Color.TRANSPARENT);
			holder.imageView.setVisibility(View.GONE);
		}

		ZsMenuItem item = mData.get(position);
		holder.textView.setText(item.getName());
		// holder.countTv.setText(item.getNum());
		// holder.countTv.setVisibility(View.GONE);
		if (mIsCount) {
			if (null == item.getSub() || 0 >= item.getSub().size()) {
				holder.subView.setVisibility(View.GONE);
			} else {
				holder.subView.setVisibility(View.VISIBLE);
			}
		} else {
			holder.subView.setVisibility(View.GONE);
		}
		// if (null != mImages) {
		// holder.imageView.setBackgroundResource(mImages[position]);
		// }
		return convertView;
	}

	/**
	 * 
	 * @author 紫韶
	 * @date Jun 28, 2013
	 **/
	public static class ViewHolder {
		public TextView textView;
		public ImageView imageView;
		public LinearLayout layout;
		public ImageView subView;
		// public TextView countTv;
	}

	/**
	 * 设置选中的项
	 * 
	 * @author 紫韶
	 * @date Jun 28, 2013
	 * @param position
	 */
	public void setSelectedPosition(int position) {
		mSelectedPosition = position;
	}
}
