package com.cold.draggridview;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.activeandroid.ActiveAndroid;

/**
 * name:MainActivity
 * func:draggridview test
 * author:cold
 * date:2017/4/26 13:46
 * copyright:
 */
@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnItemClickListener {

	private DragGridView dgvTest = null; // draggridview
	private DgvAdapter dgvAdapter = null; // adapter
	private ArrayList<DgvItem> dgvItems = new ArrayList<DgvItem>(); // show content

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ActiveAndroid.initialize(MainActivity.this);
		initView();
		initData();
	}

	/**
	 * 刷新通知通告角标
	 * @param
	 * @return void
	 */
	@Override
	protected void onResume() {
		refreshGridView();
		super.onResume();
	}


	/**
	 * 初始化数据
	 * @param
	 * @return void
	 */
	private void initData() {
		DgvManager.getManage().setItemCount(12);		
	
		dgvItems = (ArrayList<DgvItem>)DgvManager.getManage().getItems();//正式菜单环境
		dgvAdapter = new DgvAdapter(this, dgvItems);
		dgvTest.setAdapter(dgvAdapter);
		dgvTest.setActualCount(DgvManager.getManage().getActualCount() - 1);
		dgvTest.setOnItemClickListener(this);
	}

	/**
	 * 初始化布局
	 * @param
	 * @return void
	 */
	private void initView() {
		dgvTest = (DragGridView) findViewById(R.id.gv_test);
	}

	/**
	 * gridview的item点击监听接口
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 * @return void
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
		DgvItem item = dgvItems.get(position);
		switch (item.getOrderId()) {
		case 1:
			break;

		case 2:
		break;

		case 3:

			break;

		case 4:

			break;

		case 5:

			break;

		case 6:

			break;

		case 7:

			break;

		case 8:

			break;

		case 9:

			break;

		case 10:

			break;

		case 11:

			break;
		case 12:
			break;

		default:
			break;
		}
	}

	
	/**
	 * 刷新视图
	 * @param
	 * @return void
	 */
	private void refreshGridView() {
		dgvAdapter = new DgvAdapter(this, dgvItems);
		dgvTest.setAdapter(dgvAdapter);
		dgvTest.setActualCount(DgvManager.getManage().getActualCount() - 1);
		dgvAdapter.notifyDataSetChanged();
	}

	/**
	 * 退出时候保存选择后数据库的设置
	 * @param
	 * @return void
	 */
	private void saveChannel() {
		DgvManager.getManage().deleteMjbhOfAll();
		DgvManager.getManage().saveItems(dgvAdapter.getChannnelLst());
	}

	/**
	 * 后退键处理
	 * @param
	 * @return void
	 */
	@Override
	public void onBackPressed() {
		saveChannel();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActiveAndroid.dispose();
	}

}
