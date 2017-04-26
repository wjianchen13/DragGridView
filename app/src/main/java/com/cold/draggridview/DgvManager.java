package com.cold.draggridview;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

/**
 * name:DgvManager
 * func:管理显示内容
 * author:cold
 * date:2017/4/26 13:46
 * copyright:
 */
public class DgvManager {
	public static DgvManager dgvManager = null;
	public List<DgvItem> allItems; // 显示项集合
	private int actualCount = 0;
	private int itemCount = 0; // 显示总的数目，包括空白处的数目

	public DgvManager() {
		allItems = new ArrayList<DgvItem>();
		allItems = new ArrayList<DgvItem>();
		allItems.add(new DgvItem(1, "芝麻信用", 1, getResource("zmxy"), "zmxy"));
		allItems.add(new DgvItem(2, "我的快递", 2, getResource("wdkd"), "wdkd"));
		allItems.add(new DgvItem(3, "转账", 3, getResource("zz"), "zz"));
		allItems.add(new DgvItem(4, "余额宝", 4, getResource("yeb"), "yeb"));
		allItems.add(new DgvItem(5, "蚂蚁借呗", 5, getResource("myjb"), "myjb"));
		allItems.add(new DgvItem(6, "手机充值", 6, getResource("sjcz"), "sjcz"));
		allItems.add(new DgvItem(7, "红包快手", 7, getResource("hbks"), "hbks"));
		allItems.add(new DgvItem(8, "亲情账户", 8, getResource("qqzh"), "qqzh"));
		allItems.add(new DgvItem(9, "理财小工具", 9, getResource("lcxgj"), "lcxgj"));
		allItems.add(new DgvItem(10, "机票火车票", 10, getResource("jphcp"), "jphcp"));
		allItems.add(new DgvItem(11, "彩票", 11, getResource("cp"), "cp"));
		allItems.add(new DgvItem(12, "世界那么大", 12, getResource("sjnmd"), "sjnmd"));
	}

	private int getResource(String code) {
		if (code.equals("zmxy")) {
			return R.drawable.ic_zmxy;
		} else if (code.equals("wdkd")) {
			return R.drawable.ic_wdkd;
		} else if (code.equals("zz")) {
			return R.drawable.ic_zz;
		} else if (code.equals("yeb")) {
			return R.drawable.ic_yeb;
		} else if (code.equals("myjb")) {
			return R.drawable.ic_myjb;
		} else if (code.equals("sjcz")) {
			return R.drawable.ic_sjcz;
		} else if (code.equals("hbks")) {
			return R.drawable.ic_hbks;
		} else if (code.equals("qqzh")) {
			return R.drawable.ic_qqzh;
		} else if (code.equals("lcxgj")) {
			return R.drawable.ic_lcxgj;
		} else if (code.equals("jphcp")) {
			return R.drawable.ic_jphcp;
		} else if (code.equals("cp")) {
			return R.drawable.ic_cp;
		} else if (code.equals("sjnmd")) {
			return R.drawable.ic_sjnmd;
		} else {
			return -1;
		}
	}

	/**
	 * 获取管理对象
	 * @param
	 * @return 管理对象
	 */
	public static DgvManager getManage() {
		if (dgvManager == null)
			dgvManager = new DgvManager();
		return dgvManager;
	}

	/**
	 * 清除所有数据
	 * @param
	 * @return void
	 */
	public void deleteMjbhOfAll() {
		LocalItem.deleteAll();
	}

	/**
	 * 清除所有数据
	 * @param
	 * @return void
	 */
	public void deleteAllItems() {
		LocalItem.deleteAll();
	}

	/**
	 * 获取数据
	 * @param
	 * @return 数据列表
	 */
	public List<DgvItem> getItems() {
		List<LocalItem> localItems = LocalItem.getAll();
		if ((localItems != null) && (localItems.size() > 0)) {
			if ((localItems != null) && (!localItems.isEmpty())) {
				actualCount = localItems.size(); // 有效项
				int step = 1;
				int lastCode = 0;
				int all = itemCount;
				List<DgvItem> list = new ArrayList<DgvItem>();
				for (int i = 0; i < all; i++) {
					DgvItem jwyy = new DgvItem();
					if (i < actualCount) {
						jwyy.setId(localItems.get(i).getIid());
						jwyy.setName(localItems.get(i).getName());
						jwyy.setOrderId(localItems.get(i).getOrderid());
						jwyy.setCode(localItems.get(i).getCode());
						jwyy.setResId(getResource(localItems.get(i).getCode()));
						jwyy.setValid(true);
						lastCode = jwyy.getOrderId();
					} else {
						jwyy.setValid(false);
						jwyy.setOrderId(lastCode + step);
						jwyy.setId(i);
						step ++;
					}
					list.add(jwyy);
				}
				return list;
			} else {
				return allItems;	
			}	
		} else {
			return allItems;
		}
	}

	/**
	 * 获取实际显示项
	 * @param
	 * @return 实际显示项
	 */
	public int getActualCount() {
		return actualCount;
	}

	/**
	 * 保存数据
	 * @param items 数据列表
	 * @return void
	 */
	public void saveItems(List<DgvItem> items) {
		LocalItem localItem = null;
		for (int t = 0; t < items.size(); t++) {
			DgvItem item = (DgvItem) items.get(t);
			if (item.isValid()) {
				localItem = new LocalItem();
				localItem.setIid(item.getId());
				localItem.setName(item.getName());
				localItem.setOrderid(item.getOrderId());
				localItem.setResId(item.getResId());
				localItem.setCode(item.getCode());
				localItem.save();
			}
		}
	}

	/**
	 * 设置网格显示的项数
	 * @param userPower 原始的菜单，需要过滤，只显示本地的
	 * @param column 网格视图列数
	 * @return void
	 */
	public void setItemCount(String[] userPower, int column) {
		int count = 0;
		for (int i = 0; i < userPower.length; i++) {
			for (int t = 0; t < allItems.size(); t++) {
				DgvItem item = (DgvItem) allItems.get(t);
				if (!TextUtils.isEmpty(userPower[i]) && item.getCode().equals(userPower[i])) {
					count ++;
				}
			}
		}
		itemCount = (count % column == 0) ? count : ((count / column) + 1 ) * column; // 求出显示菜单项的数目
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	
}