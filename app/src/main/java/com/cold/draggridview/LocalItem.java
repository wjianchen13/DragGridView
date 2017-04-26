package com.cold.draggridview;

import java.util.List;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

/**
 * name:LocalItem
 * func:本地存储Item
 * author:cold
 * date:2017/4/26 13:46
 * copyright:
 */
@Table(name = "LOCALITEM")
public class LocalItem extends Model {

	@Column(name = "IID")
	private Integer iid; // Item id

	@Column(name = "NAME")
	private String name; // 名称

	@Column(name = "ORDERID")
	private Integer orderid; // 显示顺序

	@Column(name = "RESID")
	private Integer resId; // 显示图片资源id

	@Column(name = "CODE")
	private String code; // 菜单代码
	
	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getResId() {
		return resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static List<LocalItem> getAll() {
		return new Select().from(LocalItem.class).orderBy("Id ASC").execute();
	}
	
	public static void deleteAll() {
		new Delete().from(LocalItem.class).execute();
	}

}
