/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Robin Chutaux
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.andexert.rippleeffect;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorGroup;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.multimodalinput.event.TouchEvent;
import com.example.rippleeffect.ResourceTable;

/**
 * RippleView custom layout that allows to use Ripple UI pattern.
 *
 * @author Chutaux Robin
 * @version 2015.0512
 */
public class RippleView extends DependentLayout implements Component.DrawTask,
        Component.TouchEventListener {
    private static final String TAG = RippleView.class.getSimpleName();
    private int frameRate = 10;
    private int rippleDuration = 400;
    private int rippleAlpha = 40;
    private float radiusMax = 0;
    private boolean animationRunning = false;
    private int zoomDuration = 200;
    private float zoomScale = 1.03f;
    private AnimatorValue scaleAnimation;
    private boolean hasToZoom;
    private boolean isCentered;
    private int rippleType = 0;
    private Paint paint;
    private Color rippleColor = new Color(getContext().getColor(ResourceTable.Color_rippleColor));
    private int ripplePadding;
    private boolean downPose = false;
    private float touchDownX;
    private float touchDownY;
    private long downTime;
    private float ripplePosition;
    private float touchPointX;
    private float touchPointY;

    private OnRippleCompleteListener onCompletionListener;

    public RippleView(Context context) {
        super(context);
    }

    public RippleView(Context context, AttrSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RippleView(Context context, AttrSet attrs, String defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Method that initializes all fields and sets listeners.
     *
     * @param attrs Attribute used to initialize fields
     */
    private void init(final AttrSet attrs) {
        new Text.EditorActionListener() {
            @Override
            public boolean onTextEditorAction(int i) {
                return false;
            }
        };

        rippleColor = attrs.getAttr(RippleConstant.RV_COLOR).isPresent()
                ? attrs.getAttr(RippleConstant.RV_COLOR).get().getColorValue()
                : new Color(getContext().getColor(ResourceTable.Color_rippleColor));
        rippleType = attrs.getAttr(RippleConstant.RV_TYPE).isPresent()
                ? attrs.getAttr(RippleConstant.RV_TYPE).get().getIntegerValue() : 0;
        hasToZoom = attrs.getAttr(RippleConstant.RV_ZOOM).isPresent()
                ? attrs.getAttr(RippleConstant.RV_ZOOM).get().getBoolValue() : false;
        isCentered = attrs.getAttr(RippleConstant.RV_CENTERED).isPresent()
                ? attrs.getAttr(RippleConstant.RV_CENTERED).get().getBoolValue() : false;
        rippleDuration = attrs.getAttr(RippleConstant.RV_RIPPLE_DURATION).isPresent()
                ? attrs.getAttr(RippleConstant.RV_RIPPLE_DURATION).get().getIntegerValue()
                : rippleDuration;
        frameRate = attrs.getAttr(RippleConstant.RV_FRAME_RATE).isPresent()
                ? attrs.getAttr(RippleConstant.RV_FRAME_RATE).get().getIntegerValue() : frameRate;
        rippleAlpha = attrs.getAttr(RippleConstant.RV_ALPHA).isPresent()
                ? attrs.getAttr(RippleConstant.RV_ALPHA).get().getIntegerValue() : rippleAlpha;
        ripplePadding = attrs.getAttr(RippleConstant.RV_RIPPLE_PADDING).isPresent()
                ? attrs.getAttr(RippleConstant.RV_RIPPLE_PADDING).get().getDimensionValue() : 0;
        zoomScale = attrs.getAttr(RippleConstant.RV_ZOOM_SCALE).isPresent() ?
                attrs.getAttr(RippleConstant.RV_ZOOM_SCALE).get().getFloatValue() : 1.03f;
        zoomDuration = attrs.getAttr(RippleConstant.RV_ZOOM_DURATION).isPresent()
                ? attrs.getAttr(RippleConstant.RV_ZOOM_DURATION).get().getIntegerValue() : 200;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_STYLE);
        paint.setColor(rippleColor);
        paint.setAlpha(rippleAlpha);
        addDrawTask(this);
        setTouchEventListener(this);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (touchEvent.getPointerCount() == 1 && touchEvent.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
            touchDownX = getTouchDownX(touchEvent, 0);
            touchDownY = getTouchDownY(touchEvent, 0);
            downTime = System.currentTimeMillis();
            downPose = true;
        } else if (touchEvent.getPointerCount() == 1 && touchEvent.getAction() == TouchEvent.POINT_MOVE) {
            updateTouch(touchEvent);
        } else if (touchEvent.getPointerCount() == 1
                && touchEvent.getAction() == TouchEvent.PRIMARY_POINT_UP) {
            if (downPose
                    && Math.abs(getTouchDownX(touchEvent, 0) - touchDownX) < dp2px(50)
                    && Math.abs(getTouchDownY(touchEvent, 0) - touchDownY) < dp2px(50)) {
                downPose = false;
                animateRipple(getTouchDownX(touchEvent, 0), getTouchDownY(touchEvent, 0));
            } else {
                downPose = false;
            }
        }
        return true;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        } else {
            radiusMax = (float) Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight());
            if (rippleType != 2) {
                radiusMax /= 2;
            }
            radiusMax -= ripplePadding;
        }

        if (isCentered || rippleType == 1) {
            touchPointX = getWidth() / 2.0f;
            touchPointY = getHeight() / 2.0f;
        }
        paint.setAlpha((rippleAlpha - ((rippleAlpha) * ripplePosition)));

        float width = 0;
        if (rippleType == 1 && ripplePosition > 0.4f) {
            paint.setStyle(Paint.Style.STROKE_STYLE);
            width = radiusMax * ripplePosition - radiusMax * (ripplePosition - 0.4f) / 0.6f;
            paint.setStrokeWidth(radiusMax * ripplePosition - radiusMax * (ripplePosition - 0.4f) / 0.6f);
        } else {
            paint.setStyle(Paint.Style.FILL_STYLE);
        }
        canvas.drawCircle(touchPointX, touchPointY, radiusMax * ripplePosition - width / 2, paint);
    }

    private void updateTouch(TouchEvent touchEvent) {
        if (downPose
                && Math.abs(getTouchDownX(touchEvent, 0) - touchDownX) < dp2px(50)
                && Math.abs(getTouchDownY(touchEvent, 0) - touchDownY) < dp2px(50)) {
            if (System.currentTimeMillis() - downTime > 500) {
                downPose = false;
                animateRipple(getTouchDownX(touchEvent, 0), getTouchDownY(touchEvent, 0));
            }
        } else {
            downPose = false;
        }
    }

    private float getTouchDownX(TouchEvent touchEvent, int index) {
        float x = 0;
        if (touchEvent.getPointerCount() > index) {
            int[] xy = getLocationOnScreen();
            if (xy != null && xy.length == 2) {
                x = touchEvent.getPointerScreenPosition(index).getX() - xy[0];
            } else {
                x = touchEvent.getPointerPosition(index).getX();
            }
        }
        return x;
    }

    private float getTouchDownY(TouchEvent touchEvent, int index) {
        float y = 0;
        if (touchEvent.getPointerCount() > index) {
            int[] xy = getLocationOnScreen();
            if (xy != null && xy.length == 2) {
                y = touchEvent.getPointerScreenPosition(index).getY() - xy[1];
            } else {
                y = touchEvent.getPointerPosition(index).getY();
            }
        }
        return y;
    }

    private int dp2px(float dp) {
        return (int) (getResourceManager().getDeviceCapability().screenDensity / 160 * dp);
    }

    private void scaleAnimate() {
        AnimatorProperty scaleAnimation1 = new AnimatorProperty(this);
        scaleAnimation1.setDuration(zoomDuration);
        scaleAnimation1.scaleX(zoomScale).scaleY(zoomScale);
        AnimatorProperty scaleAnimation2 = new AnimatorProperty(this);
        scaleAnimation2.setDuration(zoomDuration);
        scaleAnimation2.scaleX(1.0f).scaleY(1.0f);
        AnimatorGroup animatorGroup = new AnimatorGroup();
        animatorGroup.build().addAnimators(scaleAnimation1).addAnimators(scaleAnimation2);
        animatorGroup.start();
    }

    /**
     * Launch Ripple animation for the current view centered at x and y position.
     *
     * @param x Horizontal position of the ripple center
     * @param y Vertical position of the ripple center
     */
    public void animateRipple(final float x, final float y) {
        createAnimation(x, y);
    }

    /**
     * Create Ripple animation centered at x, y.
     *
     * @param x Horizontal position of the ripple center
     * @param y Vertical position of the ripple center
     */
    private void createAnimation(final float x, final float y) {
        if (this.isEnabled() && !animationRunning) {
            if (hasToZoom) {
                scaleAnimate();
            }
            if (scaleAnimation == null) {
                scaleAnimation = new AnimatorValue();
                scaleAnimation.setDuration(rippleDuration);
                scaleAnimation.setValueUpdateListener(
                        (animatorValue, value) -> {
                            ripplePosition = value;
                            invalidate();
                        });
                scaleAnimation.setStateChangedListener(
                        new Animator.StateChangedListener() {
                            @Override
                            public void onStart(Animator animator) {
                                LogUtil.info(TAG, "Animator onStart()");
                            }

                            @Override
                            public void onStop(Animator animator) {
                                LogUtil.info(TAG, "Animator onStop()");
                            }

                            @Override
                            public void onCancel(Animator animator) {
                                LogUtil.info(TAG, "Animator onCancel()");
                            }

                            @Override
                            public void onEnd(Animator animator) {
                                if (onCompletionListener != null) {
                                    onCompletionListener.onComplete(RippleView.this);
                                }
                                animationRunning = false;
                            }

                            @Override
                            public void onPause(Animator animator) {
                                LogUtil.info(TAG, "Animator onPause()");
                            }

                            @Override
                            public void onResume(Animator animator) {
                                LogUtil.info(TAG, "Animator onResume()");
                            }
                        });
            }
            if (scaleAnimation.isRunning()) {
                return;
            }
            animationRunning = true;
            touchPointX = x;
            touchPointY = y;
            scaleAnimation.start();
        }
    }

    /**
     * Set Ripple color, default is #FFFFFF.
     *
     * @param rippleColor New color resource
     */
    public void setRippleColor(int rippleColor) {
        this.rippleColor = new Color(getContext().getColor(ResourceTable.Color_color_1));
    }

    public Color getRippleColor() {
        return rippleColor;
    }

    public RippleType getRippleType() {
        return RippleType.values()[rippleType];
    }

    /**
     * Set Ripple type, default is RippleType.SIMPLE.
     *
     * @param rippleType New Ripple type for next animation
     */
    public void setRippleType(final RippleType rippleType) {
        this.rippleType = rippleType.ordinal();
    }

    public boolean isCentered() {
        return isCentered;
    }

    /**
     * Set if ripple animation has to be centered in its parent view or not, default is False.
     *
     * @param isCentered is centered boolean
     */
    public void setCentered(final boolean isCentered) {
        this.isCentered = isCentered;
    }

    public int getRipplePadding() {
        return ripplePadding;
    }

    /**
     * Set Ripple padding if you want to avoid some graphic glitch.
     *
     * @param ripplePadding New Ripple padding in pixel, default is 0px
     */
    public void setRipplePadding(int ripplePadding) {
        this.ripplePadding = ripplePadding;
    }

    public Boolean isZooming() {
        return hasToZoom;
    }

    /**
     * At the end of Ripple effect, the child views has to zoom.
     *
     * @param hasToZoom Do the child views have to zoom ? default is False
     */
    public void setZooming(boolean hasToZoom) {
        this.hasToZoom = hasToZoom;
    }

    public float getZoomScale() {
        return zoomScale;
    }

    /**
     * Scale of the end animation.
     *
     * @param zoomScale Value of scale animation, default is 1.03f
     */
    public void setZoomScale(float zoomScale) {
        this.zoomScale = zoomScale;
    }

    public int getZoomDuration() {
        return zoomDuration;
    }

    /**
     * Duration of the ending animation in ms.
     *
     * @param zoomDuration Duration, default is 200ms
     */
    public void setZoomDuration(int zoomDuration) {
        this.zoomDuration = zoomDuration;
    }

    public int getRippleDuration() {
        return rippleDuration;
    }

    /**
     * Duration of the Ripple animation in ms.
     *
     * @param rippleDuration Duration, default is 400ms
     */
    public void setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public int getFrameRate() {
        return frameRate;
    }

    /**
     * Set framerate for Ripple animation.
     *
     * @param frameRate New framerate value, default is 10
     */
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getRippleAlpha() {
        return rippleAlpha;
    }

    /**
     * Set alpha for ripple effect color.
     *
     * @param rippleAlpha Alpha value between 0 and 255, default is 90
     */
    public void setRippleAlpha(int rippleAlpha) {
        this.rippleAlpha = rippleAlpha;
    }

    public void setOnRippleCompleteListener(OnRippleCompleteListener listener) {
        this.onCompletionListener = listener;
    }

    /**
     * Defines a callback called at the end of the Ripple effect.
     */
    public interface OnRippleCompleteListener {
        void onComplete(RippleView rippleView);
    }

    /**
     * Ripple type enum values.
     */
    public enum RippleType {
        SIMPLE(0),
        DOUBLE(1),
        RECTANGLE(2);
        int type;
        RippleType(int type) {
            this.type = type;
        }
    }
}
