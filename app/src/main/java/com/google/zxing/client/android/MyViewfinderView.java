/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.util.ScreenUtil;


/**
 * 自定义组件实现,扫描功能
 */
public class MyViewfinderView extends RelativeLayout {

    private static final String TAG = "MyViewfinderView";
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;
    // 扫描线
    private Drawable scanLine;
    private ImageView imageScanLine;
    //扫描框
    private Drawable scanFrame;
    private ImageView imageScanFrame;
    private TranslateAnimation animation;
    //识别框的高度
    private int scanHeight;
    //识别框距顶部的高度
    private int marginTop;
    //识别框距左边的距离
    private int marginLeft;
    //识别框距右边的距离
    private int marginRight;
    //扫描线的周期
    private int scanPeriod;
    private RelativeLayout relativeLayout;

    public MyViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        initScanView(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyViewfinderView);
        marginTop = (int) ta.getDimension(R.styleable.MyViewfinderView_marginTop, ScreenUtil.dp2px(context, 0));
        marginLeft = (int) ta.getDimension(R.styleable.MyViewfinderView_left_Margin, ScreenUtil.dp2px(context, 24));
        marginRight = (int) ta.getDimension(R.styleable.MyViewfinderView_right_Margin, ScreenUtil.dp2px(context, 24));
        scanHeight = (int) ta.getDimension(R.styleable.MyViewfinderView_scan_height, ScreenUtil.dp2px(context, 140));
        scanPeriod = ta.getInt(R.styleable.MyViewfinderView_scanPeriod, 3000);
        scanLine = ta.getDrawable(R.styleable.MyViewfinderView_scanLine);
        ta.recycle();
        CameraManager.TOP_MARGIN = marginTop;
        CameraManager.LEFT_MARGIN = marginLeft;
        CameraManager.RIGHT_MARGIN = marginRight;
        CameraManager.HEIGHT = scanHeight;
    }

    private void initScanView(Context context) {
        // 扫描框的宽度
        if (scanLine == null) {
            scanLine = getResources().getDrawable(R.drawable.ic_scan_line);
        }
        scanFrame = getResources().getDrawable(R.drawable.bg_scan);
        relativeLayout = new RelativeLayout(context);
        int scanWidth = ScreenUtil.getScreenWidth(context) - ScreenUtil.dp2px(context, marginLeft) - ScreenUtil.dp2px(context, marginRight);
        LayoutParams params = new LayoutParams(scanWidth, scanHeight);
        params.topMargin = marginTop;
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        addView(relativeLayout, params);
        imageScanFrame = new ImageView(context);
        imageScanFrame.setBackgroundDrawable(scanFrame);
        LayoutParams scanFrameLayoutParams = new LayoutParams(RMP, RMP);
        relativeLayout.addView(imageScanFrame, scanFrameLayoutParams);
        imageScanLine = new ImageView(context);
        imageScanLine.setBackgroundDrawable(scanLine);
        LayoutParams scanLineLayoutParams = new LayoutParams(RMP, RWC);
        scanLineLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        relativeLayout.addView(imageScanLine, scanLineLayoutParams);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.94f);
        animation.setDuration(scanPeriod);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        imageScanLine.startAnimation(animation);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animation != null) {
            animation.cancel();
        }
    }

    public void restartAnimation() {
        if (animation != null) {
            animation.cancel();
        }
        animation.start();
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
        CameraManager.TOP_MARGIN = marginTop;
        RelativeLayout.LayoutParams layoutParams= (LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.topMargin=marginTop;
        relativeLayout.setLayoutParams(layoutParams);
    }
}
