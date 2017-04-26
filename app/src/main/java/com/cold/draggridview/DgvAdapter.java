package com.cold.draggridview;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * name:DgvAdapter
 * func:
 * author:cold
 * date:2017/4/26 13:46
 * copyright:
 */
public class DgvAdapter extends BaseAdapter {

	private boolean isItemShow = false; // 是否显示底部的item
	private Context context;
	private int holdPosition; // 控制的postion
	private boolean isChanged = false; // 是否改变
	boolean isVisible = true; // 是否可见
	public List<DgvItem> dgvList; // 可以拖动的列表
	private TextView tvNormal; // 内容
	private RelativeLayout rlItem;
	private TextView tvMove;
	private ImageView imItem;

	/**
	 * GridView adapter
	 * @param context 上下文环境
	 * @param dgvList
	 * @return void
	 */
	public DgvAdapter(Context context, List<DgvItem> dgvList) {
		this.context = context;
		this.dgvList = dgvList;

	}

	/**
	 * 获取adapter的数据量
	 * @param
	 * @return adapter的数据量
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dgvList == null ? 0 : dgvList.size();
	}

	/**
	 * 获取数据
	 * @param position 位置
	 * @return 获取到的数据
	 */
	@Override
	public DgvItem getItem(int position) {
		// TODO Auto-generated method stub
		if (dgvList != null && dgvList.size() != 0) {
			return dgvList.get(position);
		}
		return null;
	}

	@Override
	public boolean isEnabled(int position) {
		if (!getItem(position).isValid()) {
			return false;
		}
		return super.isEnabled(position);
	}

	/**
	 * 获取id
	 * @param position 位置
	 * @return id
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 显示指定位置的子view
	 * @param position 位置
	 * @param convertView
	 * @param parent
	 * @return view
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_dgv, null);
		rlItem = (RelativeLayout) view.findViewById(R.id.rlyt_item);
		imItem = (ImageView) view.findViewById(R.id.icon_new);
		tvNormal = (TextView) view.findViewById(R.id.text_item);
		tvMove = (TextView) view.findViewById(R.id.tv_move);
		DgvItem channel = getItem(position);
		if (channel.isValid()) {
			tvNormal.setText(channel.getName());
			imItem.setBackgroundResource(channel.getResId());
			if ((position == 0) || (position == 1)) {
				tvNormal.setEnabled(false);
			}
			if (isChanged && (position == holdPosition) && !isItemShow) {
				tvNormal.setText("1");
				tvNormal.setVisibility(View.INVISIBLE);
				imItem.setVisibility(View.INVISIBLE);
				tvMove.setSelected(true);
				tvMove.setEnabled(true);
				// tvMove.setVisibility(View.VISIBLE);
				rlItem.getBackground().setAlpha(10);
			}
			if (!isVisible && (position == -1 + dgvList.size())) {
				tvNormal.setText("2");

				rlItem.setSelected(true);
				rlItem.setEnabled(true);
			}
		} else {
			tvNormal.setVisibility(View.INVISIBLE);
			imItem.setVisibility(View.INVISIBLE);
			tvMove.setVisibility(View.VISIBLE);
		}
		return view;
	}

	/**
	 * 拖动变更选项排序
	 * @param dragPostion 开始位置
	 * @param dropPostion 最后位置
	 * @return void
	 */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		DgvItem dragItem = getItem(dragPostion);
		if (dragPostion < dropPostion) {
			dgvList.add(dropPostion + 1, dragItem);
			dgvList.remove(dragPostion);
		} else {
			dgvList.add(dropPostion, dragItem);
			dgvList.remove(dragPostion + 1);
		}
		isChanged = true;
		notifyDataSetChanged();
	}

	/**
	 * 获取选项列表
	 * @param
	 * @return 选项列表
	 */
	public List<DgvItem> getChannnelLst() {
		return dgvList;
	}

	/**
	 * 显示放下的item
	 * @param show
	 * @return void
	 */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}