package com.cold.draggridview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * name:DragGridView
 * func:拖动gridview
 * author:cold
 * date:2017/4/26 13:46
 * copyright:
 */
public class DragGridView extends GridView {
	
	public int screenX; // 点击时候对应整个界面的X位置
	public int screenY; // 点击时候对应整个界面的Y位置
	private int touchItemX; // 按下点和按下item左上角x坐标偏移
	private int touchItemY; // 按下点和按下item左上角y坐标偏移

	public int dragPosition; // 当前正在拖动的位置
	private int dropPosition; // 拖动结束后对应的item的Position
	private int startPosition; // 开始拖动的item的Position
	private int itemHeight; // item高
	private int itemWidth; // item宽
	private View dragImageView = null; // 拖动的时候对应item的显示视图
	private WindowManager windowManager = null; // WindowManager管理器
	private WindowManager.LayoutParams windowParams = null;
	
	private int nColumns = 3; // 一行的item显示数量
	private boolean isMoving = false; // 是否在移动
	private int holdPosition; // 正在移动的位置
	private double dragScale = 1.2D; // 拖动的时候放大的倍数
	private Vibrator mVibrator; // 震动器
	private String LastAnimationID; // 移动时候最后个动画的ID
	private int actualCount = 0; // 实际显示的内容数目

	public DragGridView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造
	 * @param context 上下文
	 * @param attrs 属性集
	 * @param defStyle 主题
	 * @return void
	 */
	public DragGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 构造
	 * @param context 上下文
	 * @param attrs  属性集
	 * @return void
	 */
	public DragGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * @param context 上下文
	 * @return void
	 */
	public void init(Context context) {
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	/**
	 * 事件拦截
	 * @param ev 事件
	 * @return 是否拦截
	 * @see android.widget.AbsListView#onInterceptTouchEvent(MotionEvent ev)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			screenX = (int) ev.getX();
			screenY = (int) ev.getY();
			setOnItemClickListener(ev); // 设置监听
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 事件处理
	 * @param ev 事件
	 * @return 是否处理事件
	 * @see android.widget.AbsListView#onTouchEvent(MotionEvent ev)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (dragImageView != null && dragPosition != AdapterView.INVALID_POSITION) {
			// 移动时候的对应x,y位置
			super.onTouchEvent(ev);
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				screenX = (int) ev.getX();
				screenY = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				onDrag(x, y, (int) ev.getRawX(), (int) ev.getRawY());
				if (!isMoving) {
					OnMove(x, y);
				}
				if (pointToPosition(x, y) != AdapterView.INVALID_POSITION) {
					break;
				}
				break;
			case MotionEvent.ACTION_UP:
				stopDrag();
				onDrop(x, y);
				requestDisallowInterceptTouchEvent(false);
				break;

			default:
				break;
			}
		}
		return super.onTouchEvent(ev);
	}

	/**
	 *  拖动处理
	 * @param x 相对于控件左上角
	 * @param y
	 * @param rawx 相对于屏幕左上角
	 * @param rawy
	 * @return void
	 */
	private void onDrag(int x, int y, int rawx, int rawy) {
		if (dragImageView != null) {
			windowParams.alpha = 0.6f;
			windowParams.x = rawx - touchItemX; // 获取最新的
			windowParams.y = rawy - touchItemY;
			windowManager.updateViewLayout(dragImageView, windowParams); // 让dragImageView随手指拖动
		}
	}

	/**
	 * 松手下放的情况
	 * @param x 相对于控件左上角
	 * @param y
	 * @return void
	 */
	private void onDrop(int x, int y) {
		int tempPostion = pointToPosition(x, y);
		dropPosition = tempPostion;
		DgvAdapter mDragAdapter = (DgvAdapter) getAdapter();
		mDragAdapter.setShowDropItem(true); // 显示刚拖动的item
		mDragAdapter.notifyDataSetChanged(); // 刷新适配器，让对应的item显示
	}

	/**
	 *  长按点击监听
	 * @param ev 点击事件
	 * @return void
	 */
	public void setOnItemClickListener(final MotionEvent ev) {
		setOnItemLongClickListener(new OnItemLongClickListener() { // 设置长按监听

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				startPosition = position;
				dragPosition = position;
				if (startPosition > actualCount) {
					return false;
				}
				ViewGroup dragItem = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());
				itemHeight = dragItem.getHeight();
				itemWidth = dragItem.getWidth();

				if (dragPosition != AdapterView.INVALID_POSITION) {
					touchItemX = screenX - dragItem.getLeft(); // 得到长按item项按下点x坐标和左上角x坐标的偏移
					touchItemY = screenY - dragItem.getTop(); // 得到长按item项按下点y坐标和左上角y坐标的偏移

					dragItem.destroyDrawingCache(); // 获取View组件显示的内容可以通过cache机制保存为bitmap
					dragItem.setDrawingCacheEnabled(true); // 若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的
					Bitmap dragBitmap = Bitmap.createBitmap(dragItem.getDrawingCache()); // 保存cache
					mVibrator.vibrate(50); // 震动效果
					startDrag(dragBitmap, (int) ev.getRawX(), (int) ev.getRawY());
					hideDropItem();
					dragItem.setVisibility(View.INVISIBLE);
					isMoving = false;
					requestDisallowInterceptTouchEvent(true); // 设置控件不拦截事件
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 开始拖动，根据传入的bitmap，生成了一个比较大的可以拖动的ImageView
	 * @param dragBitmap 拖动显示的图片
	 * @param x
	 * @param y
	 * @return void
	 */
	public void startDrag(Bitmap dragBitmap, int x, int y) {
		stopDrag();
		windowParams = new WindowManager.LayoutParams();// 获取WINDOW界面的参数
		windowParams.gravity = Gravity.TOP | Gravity.LEFT; // 对其方式
		windowParams.x = x - touchItemX; // 设置按下点位置
		windowParams.y = y - touchItemY;
		windowParams.width = (int) (dragScale * dragBitmap.getWidth()); // 放大图片
		windowParams.height = (int) (dragScale * dragBitmap.getHeight());
		this.windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		this.windowParams.format = PixelFormat.TRANSLUCENT;
		this.windowParams.windowAnimations = 0;
		ImageView iv = new ImageView(getContext());
		iv.setImageBitmap(dragBitmap); // 设置bitmap
		windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(iv, windowParams);
		dragImageView = iv; // 拖动的Item
	}

	/**
	 * 停止拖动 ，释放并初始化
	 * @param
	 * @return void
	 */
	private void stopDrag() {
		if (dragImageView != null) {
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}

	/**
	 * 计算高度
	 * @param widthMeasureSpec 水平要求
	 * @param heightMeasureSpec 垂直要求
	 * @return void
	 * @see View#onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/**
	 * 隐藏放下的item
	 * @param
	 * @return void
	 */
	private void hideDropItem() {
		((DgvAdapter) getAdapter()).setShowDropItem(false);
	}

	/**
	 * 获取移动动画
	 * @param toXValue 水平位移
	 * @param toYValue 垂直位移
	 * @return 获取的动画
	 */
	public Animation getMoveAnimation(float toXValue, float toYValue) {
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, toXValue, Animation.RELATIVE_TO_SELF, 0.0F,
				Animation.RELATIVE_TO_SELF, toYValue);// 当前位置移动到指定位置
		mTranslateAnimation.setFillAfter(true);// 设置一个动画效果执行完毕后，View对象保留在终止的位置。
		mTranslateAnimation.setDuration(600L);
		return mTranslateAnimation;
	}

	/**
	 * 后去实际显示数量
	 * @param
	 * @return 实际显示数量
	 */
	public int getActualCount() {
		return actualCount;
	}

	/**
	 * 设置实际显示数量
	 * @param actualCount 实际显示数量
	 * @return void
	 */
	public void setActualCount(int actualCount) {
		this.actualCount = actualCount;
	}
	
	/**
	 * 获取列数
	 * @param
	 * @return 列数
	 */
	public int getnColumns() {
		return nColumns;
	}

	/**
	 * 设置列数
	 * @param nColumns 列数 
	 * @return void
	 */
	public void setnColumns(int nColumns) {
		this.nColumns = nColumns;
	}

	/**
	 * 移动的时候触发
	 * @param x
	 * @param y
	 * @return void
	 */
	@SuppressLint("NewApi")
	public void OnMove(int x, int y) {
		int dPosition = pointToPosition(x, y); // 获取x，y坐标对应列表位置
		if ((dPosition >= 0) && (dPosition <= actualCount)) { // 必须为有效数字
			if ((dPosition == -1) || (dPosition == dragPosition)) { // 判断item移动的地方是否就是item本身的位置
				return;
			}
			// 下面需要根据item移动的位置计算列表其他item移动
			dropPosition = dPosition; // 当前x，y坐标对应的位置
			if (dragPosition != startPosition) {
				dragPosition = startPosition;
			}
			int movecount;
			// 拖动的 = 开始拖的，并且 拖动的 不等于放下的
			if ((dragPosition == startPosition) || (dragPosition != dropPosition)) {
				// 移需要移动的动ITEM数量
				movecount = dropPosition - dragPosition;
			} else {
				// 移需要移动的动ITEM数量为0
				movecount = 0;
			}
			if (movecount == 0) { // 说明不需要移动任何项
				return;
			}
			int movecount_abs = Math.abs(movecount); // 取绝对值
			if (dPosition != dragPosition) { // 拖动位置和最新位置不一致
				// dragGroup设置为不可见
//				ViewGroup dragGroup = (ViewGroup) getChildAt(dragPosition); // 
//				dragGroup.setVisibility(View.INVISIBLE); // 设置当前x，y坐标的项不可见
				float to_x = 1; // 当前下方positon
				float to_y; // 当前下方右边positon
				float x_vlaue = ((float) getHorizontalSpacing() / (float) itemWidth) + 1.0f; // 计算列表项和间距总和
				float y_vlaue = ((float) getVerticalSpacing() / (float) itemHeight) + 1.0f;
				
				for (int i = 0; i < movecount_abs; i++) { // 向右拖动
					to_x = x_vlaue;
					to_y = y_vlaue;
					// 像左
					if (movecount > 0) {
						holdPosition = dragPosition + i + 1; // 移动的位置项
						if (dragPosition / nColumns == holdPosition / nColumns) { // 同一行向右拖动时处理
							to_x = -x_vlaue;
							to_y = 0;
						} else if (holdPosition % 3 == 0) { // 处理第一行的第一个显示项，往上一行移动到最后
							to_x = 2 * x_vlaue;
							to_y = -y_vlaue;
						} else {
							to_x = -x_vlaue;
							to_y = 0;
						}
					} else { // 向左拖动
						// 向右,下移到上，右移到左
						holdPosition = dragPosition - i - 1;
						if (dragPosition / nColumns == holdPosition / nColumns) {
							to_x = x_vlaue;
							to_y = 0;
						} else if ((holdPosition + 1) % 3 == 0) {
							to_x = -2 * x_vlaue;
							to_y = y_vlaue;
						} else {
							to_x = x_vlaue;
							to_y = 0;
						}
					}
					ViewGroup moveViewGroup = (ViewGroup) getChildAt(holdPosition);
					Animation moveAnimation = getMoveAnimation(to_x, to_y);
					moveViewGroup.startAnimation(moveAnimation);
					// 如果是最后一个移动的，那么设置他的最后个动画ID为LastAnimationID
					if (holdPosition == dropPosition) {
						LastAnimationID = moveAnimation.toString();
					}
					moveAnimation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							isMoving = true;
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							// 如果为最后个动画结束，那执行下面的方法
							if (animation.toString().equalsIgnoreCase(LastAnimationID)) {
								DgvAdapter mDragAdapter = (DgvAdapter) getAdapter();
								mDragAdapter.exchange(startPosition, dropPosition);
								startPosition = dropPosition;
								dragPosition = dropPosition;
								isMoving = false;
							}
						}
					});
				}
			}
		}
	}
}