package me.zbl.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import me.zbl.dictionary.R;

/**
 * 带有删除功能的输入框
 * <p>
 * Created by James on 18-1-12.
 */

public class ClearableEdittext extends AppCompatEditText {

  private Drawable mDrawClear;

  public ClearableEdittext(Context context) {
    super(context);
  }

  public ClearableEdittext(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView(attrs);
  }

  public ClearableEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(attrs);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (null != event && event.getAction() == MotionEvent.ACTION_UP) {
      // 获取点击的位置
      int x = (int) event.getRawX();
      int y = (int) event.getRawY();
      // 矩形
      Rect r = new Rect();
      // 设置矩形大小为当前 Edittext 的可视大小
      getGlobalVisibleRect(r);
      // 将矩形的左边界设置为 清除按钮图片的左边界
      r.left = r.right - mDrawClear.getIntrinsicWidth();
      if (r.contains(x, y)) {
        // 如果点击位置在此矩形中，则判断为点击了清除按钮
        clear();
      }
    }
    return super.onTouchEvent(event);
  }

  /**
   * 初始化
   *
   * @param attrs 属性
   */
  private void initView(AttributeSet attrs) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attrs, me.zbl.dictionary.R.styleable.ClearableEdittext);
    int count = typedArray.getIndexCount();
    for (int i = 0; i < count; i++) {
      int attr = typedArray.getIndex(i);
      switch (i) {
        case R.styleable.ClearableEdittext_drawbleRight: {
          mDrawClear = typedArray.getDrawable(attr);
          break;
        }
        default: {
        }
        typedArray.recycle();
      }
    }
    addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        flushClearStatus();
      }
    });
    flushClearStatus();
  }

  /**
   * 刷新清除按钮的状态
   */
  private void flushClearStatus() {
    if (length() == 0) {
      // 长度为零不显示清除按钮
      setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    } else {
      // 文字长度不为零显示清除按钮
      setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawClear, null);
    }
  }

  /**
   * 清除输入的文字
   */
  private void clear() {
    setText("");
  }
}
