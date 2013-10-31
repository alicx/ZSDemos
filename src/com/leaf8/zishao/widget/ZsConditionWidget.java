package com.leaf8.zishao.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leaf8.zishao.R;
import com.leaf8.zishao.adapter.ZsConditionAdapter;
import com.leaf8.zishao.bean.ZsMenuItem;

/**
 * 店铺的选择条件组件
 * 
 * @author 紫韶
 * @date Jun 28, 2013
 **/
public class ZsConditionWidget extends RelativeLayout implements View.OnClickListener, OnItemClickListener {

	private View mView;
	private View mCategoryLayout;
	private ZsConditionWidget.DataSourceListener mListener;
	private TextView mTypeTv;
	private TextView mCityTv;
	private TextView mSortTv;

	private ZsConditionAdapter mAdapter;
	private ZsConditionAdapter mSubAdapter;

	private ListView mLv;
	private ListView mSubLv;
	private ListView mSingleLv;

	private SaveBundle mSaveBundle = new SaveBundle();

	private int mType;
	private int mPosition;
	private int mSubPosition;
	private ZsMenuItem mFirstLevelItem;// 保留路径的一级item

	private int mSelectType;

	private String defaultType = "";
	private String defaultArea = "";
	private String defaultSort = "";

	public ZsConditionWidget(Context context) {
		super(context);
		initView(context);
	}

	public ZsConditionWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ZsConditionWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 初始化组件
	 * 
	 * @author 紫韶
	 * @date Jun 28, 2013
	 * @param context
	 */
	private void initView(Context context) {
		LayoutInflater mInflater = (LayoutInflater) context.getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		mView = mInflater.inflate(R.layout.leaf_widget_condition_bar, this);
		mTypeTv = (TextView) mView.findViewById(R.id.tc_select1);
		mCityTv = (TextView) mView.findViewById(R.id.tc_select2);
		mSortTv = (TextView) mView.findViewById(R.id.tc_select3);
		mTypeTv.setOnClickListener(this);
		mCityTv.setOnClickListener(this);
		mSortTv.setOnClickListener(this);
		mLv = (ListView) mView.findViewById(R.id.tc_select_listview);
		mSubLv = (ListView) mView.findViewById(R.id.tc_select_subListView);
		mSingleLv = (ListView) mView.findViewById(R.id.tc_select_single_listview);
		mCategoryLayout = mView.findViewById(R.id.tc_select_category_layout);
		mCategoryLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideView();
			}
		});

		mAdapter = new ZsConditionAdapter(context, R.layout.leaf_widget_condition_bar_firstitem);
		mSubAdapter = new ZsConditionAdapter(context, R.layout.leaf_widget_condition_bar_seconditem);
		mLv.setAdapter(mAdapter);
		mLv.setOnItemClickListener(this);
		mSubLv.setAdapter(mSubAdapter);
		mSubLv.setOnItemClickListener(subListener);
		mSingleLv.setAdapter(mAdapter);
		mSingleLv.setOnItemClickListener(this);
	}

	/**
	 * 隐藏弹出的列表
	 * 
	 * @author 紫韶
	 * @date Jun 28, 2013
	 */
	public void hideView() {
		mPosition = mSubPosition = 0;
		// mLv.setVisibility(View.GONE);
		// mSubLv.setVisibility(View.GONE);
		// mSingleLv.setVisibility(View.GONE);
		setSelected(null);
		mCategoryLayout.setVisibility(View.GONE);
	}

	/**
	 * 是否选择条件是可视的
	 * 
	 * @author 紫韶
	 * @date Jun 28, 2013
	 * @return
	 */
	public boolean isConditionShow() {
		return mCategoryLayout.isShown();
	}

	@Override
	public void onClick(View v) {
		// 第二次点击同一个tab的时候收起
		if (v.getId() == mType && isConditionShow()) {
			hideView();
			return;
		}
		if (null != mSubAdapter && null != mSubAdapter.mData && mSubAdapter.mData.size() > 0) {
			mSubAdapter.refresh(new ArrayList<ZsMenuItem>(), null, false);
		}
		bringToFront();
		mCategoryLayout.setVisibility(View.VISIBLE);
		mLv.setVisibility(View.GONE);
		mSingleLv.setVisibility(View.GONE);
		mType = v.getId();
		switch (v.getId()) {
			case R.id.tc_select1:
				setSelected(mTypeTv);
				List<ZsMenuItem> mData1 = buildCategoryData();
				if (mData1.size() == 1 && mData1.get(0).getSub().size() > 0) {
					mAdapter.refresh(mData1.get(0).getSub(), null, false);
					mSingleLv.setVisibility(View.VISIBLE);
				} else {
					mAdapter.refresh(mData1, null, true);
					mLv.setVisibility(View.VISIBLE);
				}
				if (null != mSaveBundle.firstCondition) {
					onRestoreStatus(mSaveBundle.firstCondition.firstPosition, mSaveBundle.firstCondition.secondPosition);
				}
				break;
			case R.id.tc_select2:
				setSelected(mCityTv);
				List<ZsMenuItem> mData2 = TongChengSystemHelper.buildAreaNearData(mCity);
				mAdapter.refresh(mData2, null, false);
				mSingleLv.setVisibility(View.VISIBLE);
				if (null != mSaveBundle.secondCondition) {
					onRestoreStatus(mSaveBundle.secondCondition.firstPosition,
							mSaveBundle.secondCondition.secondPosition);
				}
				break;
			case R.id.tc_select3:
				setSelected(mSortTv);
				List<ZsMenuItem> mData3 = TongChengSystemHelper.buildSortNearData(mCity);
				mAdapter.refresh(mData3, null, false);
				mSingleLv.setVisibility(View.VISIBLE);
				if (null != mSaveBundle.thirdCondition) {
					onRestoreStatus(mSaveBundle.thirdCondition.firstPosition, mSaveBundle.thirdCondition.secondPosition);
				}
				break;
			default:
				hideView();
				return;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    ZsMenuItem info = (ZsMenuItem) mAdapter.getItem(position);
		mPosition = position;
		mFirstLevelItem = info;
		switch (mType) {
			case R.id.tc_select1:
				if (null != info.getSub() && 0 < info.getSub().size()) {
					mAdapter.setSelectedPosition(position);
					mAdapter.notifyDataSetInvalidated();
					mSubAdapter.refresh(info.getSub(), null, false);
					mSubLv.setVisibility(View.VISIBLE);
					return;
				}
				mSelectType = mType;
				mSaveBundle.firstCondition = new SelectItem();
				mSaveBundle.firstCondition.firstPosition = mPosition;
				mSaveBundle.firstCondition.secondPosition = 0;
				mSaveBundle.firstCondition.data = mFirstLevelItem;
				hideView();
				onClickListener();
				break;
			case R.id.tc_select2:
				if (null != info.getSub() && 0 < info.getSub().size()) {
					mAdapter.setSelectedPosition(position);
					mAdapter.notifyDataSetInvalidated();
					mSubAdapter.refresh(info.getSub(), null, false);
					mSubLv.setVisibility(View.VISIBLE);
					return;
				}
				mSelectType = mType;
				mSaveBundle.secondCondition = new SelectItem();
				mSaveBundle.secondCondition.firstPosition = mPosition;
				mSaveBundle.secondCondition.secondPosition = 0;
				mSaveBundle.secondCondition.data = mFirstLevelItem;
				hideView();
				onClickListener();
				break;
			case R.id.tc_select3:
				mSelectType = mType;
				mSaveBundle.thirdCondition = new SelectItem();
				mSaveBundle.thirdCondition.firstPosition = mPosition;
				mSaveBundle.thirdCondition.secondPosition = 0;
				mSaveBundle.thirdCondition.data = mFirstLevelItem;
				hideView();
				onClickListener();
				break;
		}
	}

	/**
	 * 二级菜单的itemClick监听
	 */
	private OnItemClickListener subListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    ZsMenuItem info = (ZsMenuItem) mSubAdapter.getItem(position);
			mSubPosition = position;
			switch (mType) {
				case R.id.tc_select1:
					mSelectType = mType;
					mSaveBundle.firstCondition = new SelectItem();
					mSaveBundle.firstCondition.firstPosition = mPosition;
					mSaveBundle.firstCondition.secondPosition = mSubPosition;
					mSaveBundle.firstCondition.data = info;
					hideView();
					onClickListener();
					break;
				case R.id.tc_select2:
					mSelectType = mType;
					mSaveBundle.secondCondition = new SelectItem();
					mSaveBundle.secondCondition.firstPosition = mPosition;
					mSaveBundle.secondCondition.secondPosition = mSubPosition;
					mSaveBundle.secondCondition.data = info;
					hideView();
					onClickListener();
					break;
				case R.id.tc_select3:
					hideView();
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 销毁
	 * 
	 * @author 紫韶
	 * @date Jul 2, 2013
	 */
	public void destory() {
		mType = mPosition = mSubPosition = mSelectType = 0;
		mListener = null;
		mView = mCategoryLayout = null;
		mAdapter = mSubAdapter = null;
		mLv = mSubLv = mSingleLv = null;
		mTypeTv = mCityTv = mSortTv = null;
		mFirstLevelItem = null;
		mSaveBundle = null;
	}

	/**
	 * 还原组件选择状态
	 * 
	 * @author 紫韶
	 * @date Jul 8, 2013
	 * @param bundle
	 */
	public void restoreSelectWidget(SaveBundle bundle) {
		mType = mPosition = mSubPosition = mSelectType = 0;
		if (null != bundle) {
			mSaveBundle = bundle;
			if (null != bundle.firstCondition && !TextUtils.isEmpty(bundle.firstCondition.tvShowContent)) {
				mTypeTv.setText(bundle.firstCondition.tvShowContent);
			}
			if (null != bundle.secondCondition && !TextUtils.isEmpty(bundle.secondCondition.tvShowContent)) {
				mCityTv.setText(bundle.secondCondition.tvShowContent);
			}
			if (null != bundle.thirdCondition && !TextUtils.isEmpty(bundle.thirdCondition.tvShowContent)) {
				mSortTv.setText(bundle.thirdCondition.tvShowContent);
			}
		}
	}

	/**
	 * 选择一个条件
	 * 
	 * @author 紫韶
	 * @date Jul 2, 2013
	 * @param info
	 */
	public void onClickListener() {
	    ZsMenuItem selected = null;
		switch (mSelectType) {
			case R.id.tc_select1:
				selected = mSaveBundle.firstCondition.data;
				mSaveBundle.firstCondition.tvShowContent = selected.getName();
				if (0 <= selected.getId().indexOf("-")) { // 如果是二级的全部，则展示一级的数据
					mSaveBundle.firstCondition.tvShowContent = mFirstLevelItem.getName();
				}
				mTypeTv.setText(mSaveBundle.firstCondition.tvShowContent);
				break;
			case R.id.tc_select2:
				selected = mSaveBundle.secondCondition.data;
				mSaveBundle.secondCondition.tvShowContent = selected.getName();
				if (0 <= selected.getId().indexOf("-")) { // 如果是二级的全部，则展示一级的数据
					mSaveBundle.secondCondition.tvShowContent = mFirstLevelItem.getName();
				}
				mCityTv.setText(mSaveBundle.secondCondition.tvShowContent);
				break;
			case R.id.tc_select3:
				selected = mSaveBundle.thirdCondition.data;
				mSaveBundle.thirdCondition.tvShowContent = selected.getName();
				mSortTv.setText(mSaveBundle.thirdCondition.tvShowContent);
				break;
		}
		mFirstLevelItem = null;
		if (null != mListener && null != selected && !mListener.isRejectClickEvent(mSelectType, selected)) {
			mListener.onSelectListener(mSelectType, selected, mSaveBundle);
		}
	}

	/**
	 * 设置item的点击事件
	 * 
	 * @author 紫韶
	 * @date Jun 28, 2013
	 * @param listener
	 */
	public void setItemSelectListener(ZsConditionWidget.DataSourceListener listener) {
		this.mListener = listener;
	}

	/**
	 * 设置点击的监听
	 * 
	 * @author 紫韶
	 * @date Jun 28, 2013
	 **/
	public interface DataSourceListener {
		/**
		 * 响应click事件
		 * 
		 * @param type
		 * @param info
		 * @param data
		 */
		public void onSelectListener(int id, Object info, SaveBundle data);

		/**
		 * 是否拒绝响应该click事件 返回true为拒绝响应
		 * 
		 * @param type
		 * @param info
		 * @return
		 */
		public boolean isRejectClickEvent(int id, Object info);
	}

	/**
	 * 重置组件
	 * 
	 * @author 紫韶
	 * @date Jul 4, 2013
	 */
	public void reset() {
		mType = mPosition = mSubPosition = mSelectType = 0;
		mFirstLevelItem = null;
		mSaveBundle = new SaveBundle();
		hideView();
	}

	/**
	 * 设置选中的颜色
	 * 
	 * @author 紫韶
	 * @date Jul 5, 2013
	 * @param view
	 */
	public void setSelected(TextView view) {
		Drawable rightDrawable;
		Drawable rightDrawableHight;
		Resources res = getResources();
		rightDrawable = res.getDrawable(R.drawable.arrow_down);
		rightDrawableHight = res.getDrawable(R.drawable.leaf_arrow_up_hilight);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
		rightDrawableHight.setBounds(0, 0, rightDrawableHight.getMinimumWidth(), rightDrawableHight.getMinimumHeight());
		mTypeTv.setCompoundDrawables(null, null, rightDrawable, null);
		mTypeTv.setTextColor(getResources().getColor(R.color.gray));
		mCityTv.setCompoundDrawables(null, null, rightDrawable, null);
		mCityTv.setTextColor(getResources().getColor(R.color.gray));
		mSortTv.setCompoundDrawables(null, null, rightDrawable, null);
		mSortTv.setTextColor(getResources().getColor(R.color.gray));

		if (null != view) {
			view.setCompoundDrawables(null, null, rightDrawableHight, null);
			view.setTextColor(getResources().getColor(R.color.orange));
		}
	}

	/**
	 * 重回列表的状态
	 * 
	 * @author 紫韶
	 * @date Jul 8, 2013
	 * @param posit
	 * @param subposit
	 */
	private void onRestoreStatus(int posit, int subposit) {
		mPosition = posit;
		mAdapter.setSelectedPosition(posit);
		mAdapter.notifyDataSetInvalidated();
		ZsMenuItem info = (ZsMenuItem) mAdapter.getItem(posit);
		mFirstLevelItem = info;
		if (null != info.getSub() && 0 < info.getSub().size()) {
			mSubAdapter.setSelectedPosition(subposit);
			mSubAdapter.refresh(info.getSub(), null, false);
			mSubLv.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置分类列表
	 * @param data
	 */
	public void setCategoryList(List<ZsMenuItem> data) {
	    
	}
	
	/**
	 * 设置选项
	 * 
	 * @author 紫韶
	 * @date Jul 8, 2013
	 **/
	public class SelectItem {
		public int firstPosition;
		public int secondPosition;
		public String tvShowContent;
		public ZsMenuItem data = new ZsMenuItem();
	}

	/**
	 * 提供使用者保存数据
	 * 
	 * @author 紫韶
	 * @date Jul 8, 2013
	 **/
	public class SaveBundle {
		public SelectItem firstCondition = new SelectItem();
		public SelectItem secondCondition = new SelectItem();
		public SelectItem thirdCondition = new SelectItem();
	}
}