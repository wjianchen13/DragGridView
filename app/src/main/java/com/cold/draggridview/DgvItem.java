package com.cold.draggridview;

import java.io.Serializable;

/**
 * name:DgvItem
 * func:
 * author:cold
 * date:2017/4/26 13:46
 * copyright:
 */
public class DgvItem implements Serializable {
	private static final long serialVersionUID = -6465237897027410019L;
	private Integer id; // id
	private String name; // name
	private Integer orderId; // 菜单编号
	private int resId; // 显示图片id
	private boolean valid; // 是否有效
	private String code; // 菜单代码
	
	public DgvItem() {

	}

	public DgvItem(int id, String name, int orderId, int resId, String code) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.resId = resId;
		this.code = code;
		this.valid = true;
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String toString() {
		return "ChannelItem [id=" + this.id + ", name=" + this.name + ", selected=" + "]";
	}
}