package com.vs.bullbe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * @author yuyh.
 * @date 2016/8/25.
 */
public class BubblePopupWindow extends PopupWindow {



    private BubbleRelativeLayout bubbleView;
    private BubbleRelativeLayout bubbleView2;
    private Context context;

    public BubblePopupWindow(Context context) {
        this.context = context;
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setOutsideTouchable(false);
        setClippingEnabled(false);

        ColorDrawable dw = new ColorDrawable(0);
        setBackgroundDrawable(dw);
    }

    public void setBubbleView(View view) {
        bubbleView = new BubbleRelativeLayout(context);
        bubbleView.setBackgroundColor(Color.TRANSPARENT);
        bubbleView.addView(view);
        setContentView(bubbleView);
    }

//    public void setTwoBubbleView(View leftView,View rightView) {
//
//        bubbleView = new BubbleRelativeLayout(context);
//        bubbleView.setBackgroundColor(Color.TRANSPARENT);
//        bubbleView.addView(leftView);
//
//        bubbleView2 = new BubbleRelativeLayout(context);
//        bubbleView2.setBackgroundColor(Color.TRANSPARENT);
//        bubbleView2.addView(rightView);
//
//        LinearLayout linearLayout = new LinearLayout(context);
//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.
//                LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        linearLayout.setLayoutParams(param);
//        linearLayout.setFitsSystemWindows(true);
//        linearLayout.addView(bubbleView);
//        linearLayout.addView(bubbleView2);
//        setContentView(linearLayout);
//
//    }

    public void setParam(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void show(View parent) {
        show(parent, Gravity.TOP, getMeasuredWidth() / 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void show(View parent, int gravity) {
        show(parent, gravity, getMeasuredWidth() / 2);
    }

    /**
     * 显示弹窗
     *
     * @param parent
     * @param gravity
     * @param bubbleOffset 气泡尖角位置偏移量。默认位于中间
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void show(View parent, int gravity, float bubbleOffset) {
        BubbleRelativeLayout.BubbleLegOrientation orientation = BubbleRelativeLayout.BubbleLegOrientation.LEFT;
        if (!this.isShowing()) {
            switch (gravity) {
                case Gravity.BOTTOM:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.TOP;
                    break;
                case Gravity.TOP:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.BOTTOM;
                    break;
                case Gravity.RIGHT:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.LEFT;
                    break;
                case Gravity.LEFT:
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.RIGHT;
                    break;
                default:
                    break;
            }

            //适配屏幕位置不足够显示window
            if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP){
                PopupWindowUtil.ShowPosition showPosition = PopupWindowUtil.GetShowPosition(parent, bubbleView);
                if (showPosition == PopupWindowUtil.ShowPosition.TOP){//需要向上弹
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.BOTTOM;
                    bubbleView.setBubbleParams(orientation, bubbleOffset); // 设置气泡布局方向及尖角偏移
                }else if(showPosition == PopupWindowUtil.ShowPosition.BOTTOM){//需要向下弹
                    orientation = BubbleRelativeLayout.BubbleLegOrientation.TOP;
                    bubbleView.setBubbleParams(orientation, bubbleOffset); // 设置气泡布局方向及尖角偏移
                }
            }else{
                bubbleView.setBubbleParams(orientation, bubbleOffset); // 设置气泡布局方向及尖角偏移
            }

            int[] location = new int[2];
            parent.getLocationOnScreen(location);

            // 设置好参数之后再show
            int windowPos[] = PopupWindowUtil.calculatePopWindowPos(parent, bubbleView);
            int xOff = 10; // 可以自己调整偏移
            windowPos[0] -= xOff;
            showAtLocation(parent, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);

//            switch (gravity) {
//                case Gravity.BOTTOM:
//                    showAsDropDown(parent);
////                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - 100, 0);
//                    break;
//                case Gravity.TOP:
//                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1] - getMeasureHeight());
//                    break;
//                case Gravity.RIGHT:
//                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] + parent.getWidth(), location[1] - (parent.getHeight() / 2));
//                    break;
//                case Gravity.LEFT:
//                    showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - getMeasuredWidth(), location[1] - (parent.getHeight() / 2));
//                    break;
//                default:
//                    break;
//            }
        } else {
            this.dismiss();
        }
    }

    /**
     * 测量高度
     * @return
     */
    public int getMeasureHeight() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popHeight = getContentView().getMeasuredHeight();
        return popHeight;
    }

    /**
     * 测量宽度
     * @return
     */
    public int getMeasuredWidth() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popWidth = getContentView().getMeasuredWidth();
        return popWidth;
    }
}
