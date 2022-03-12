
package com.hundsun.zjfae.activity.scan.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.scan.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 */
public final class ViewfinderView extends View {
    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;

    /**
     * 四个绿色边角对应的长度
     */
    private int ScreenRate;

    /**
     * 四个绿色边角对应的宽度
     */
    private static final int CORNER_WIDTH = 6;
    /**
     * 扫描框中的中间线的宽度
     */
    private static final int MIDDLE_LINE_WIDTH = 6;

    /**
     * 扫描框中的中间线的与扫描框左右的间隙
     */
    private static final int MIDDLE_LINE_PADDING = 5;

    /**
     * 中间那条线每次刷新移动的距离
     */
    private static final int SPEEN_DISTANCE = 5;

    /**
     * 手机的屏幕密度
     */
    private static float density;
    /**
     * 字体大小
     */
    private static final int TEXT_SIZE = 14;
    /**
     * 字体距离扫描框下面的距离
     */
    private static final int TEXT_PADDING_TOP = 30;

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 中间滑动线的最顶端位置
     */
    private int slideTop;

    private boolean isUpMove = true;//中间滑动线向上移动

    /**
     * 中间滑动线的最底端位置
     */
    private int slideBottom;

    /**
     * 将扫描的二维码拍下来，这里没有这个功能，暂时不考虑
     */
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;

    private final int resultPointColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    boolean isFirst;

    private Bitmap scanLine;

    private int scanLineHeight;

    //手电筒相关
    private Bitmap flashLightBitmap;
    private Bitmap openFlashLightBitmap;
    private Bitmap scanLineBitmap;
    private String flashLightOpenText;
    private String flashLightCloseText;
    private Paint flashLightBgPaint;
    private Paint flashLightTextPaint;
    private boolean isOpenFlashLight;
    private static final int BG_WIDTH = 80;
    private static final int BG_HEIGHT = 20;
    private static final int BG_MARGIN_BOTTOM = 10;
    private static final int BG_RADIUS = 10;
    private static final int FLASH_TEXT_SIZE = 12;
    private float bgWidth;
    private float bgHeight;
    private float bgMarginBottom;
    private float bgRadius;
    private float leftRightPadding;
    private float flashTextBottomDistance;
    private RectF bgRect;
    private Rect flashRect;
    private Rect flashOpenRect;
    private Rect frame;
    private Rect lineRect;
    private Rect flashTextRect;
    private onFlashLightStateChangeListener mOnFlashLightStateChangeListener;
    public static boolean isWeakLight = false;


    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        density = context.getResources().getDisplayMetrics().density;
        //将像素转换成dp
        ScreenRate = (int) (19 * density);

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);

        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new HashSet<ResultPoint>(5);

        scanLine = ((BitmapDrawable) (getResources().getDrawable(R.drawable.qrcode_scan_line))).getBitmap();
        scanLineHeight = scanLine.getHeight();

        //flashLight
        flashLightBitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.scan_flashlight)).getBitmap();
        openFlashLightBitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.scan_open_flashlight)).getBitmap();
        scanLineBitmap = ((BitmapDrawable) resources.getDrawable(R.drawable.qrcode_scan_line)).getBitmap();
        bgRect = new RectF();
        flashRect = new Rect();
        flashOpenRect = new Rect();
        flashTextRect = new Rect();
        lineRect = new Rect();
        flashLightOpenText = "轻点照亮";
        flashLightCloseText = "轻点关闭";
        flashLightTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        flashLightTextPaint.setColor(Color.WHITE);
        flashLightTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, FLASH_TEXT_SIZE * density, context.getResources().getDisplayMetrics()));
        flashLightTextPaint.setTextAlign(Paint.Align.CENTER);

        flashLightBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        flashLightBgPaint.setColor(Color.BLACK);
        flashLightBgPaint.setAlpha(102);
        bgWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, BG_WIDTH * density, context.getResources().getDisplayMetrics());
        bgHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, BG_HEIGHT * density, context.getResources().getDisplayMetrics());
        bgMarginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, BG_MARGIN_BOTTOM * density, context.getResources().getDisplayMetrics());
        bgRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, BG_RADIUS * density, context.getResources().getDisplayMetrics());
        leftRightPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 8 * density, context.getResources().getDisplayMetrics());
        flashTextBottomDistance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 6 * density, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onDraw(Canvas canvas) {
        //中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
        if (frame == null) {
            frame = CameraManager.get().getFramingRect();
        }
        if (frame == null) {
            return;
        }

        //初始化中间线滑动的最上边和最下边
        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top;
            slideBottom = frame.bottom - scanLineHeight;
        }

        //获取屏幕的宽和高
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setColor(resultBitmap != null ? resultColor : maskColor);

        //画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
        //扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);


        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            //画扫描框边上的角，总共8个部分
            paint.setColor(getResources().getColor(R.color.cBgHomeTop));
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
                    + ScreenRate, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - ScreenRate,
                    frame.left + CORNER_WIDTH, frame.bottom, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
                    frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
                    frame.right, frame.bottom, paint);


            //画扫描框下面的字
            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE * density);
//            String text = getResources().getString(R.string.scan_text);
//            float textWidth = paint.measureText(text);

//            canvas.drawText(text, (width - textWidth) / 2, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density), paint);

            if (isWeakLight) {
                if (!isOpenFlashLight) {
                    bgRect.left = (int) ((width - bgWidth) / 2);
                    bgRect.right = (int) ((width + bgWidth) / 2);
                    bgRect.bottom = (int) (frame.bottom - bgMarginBottom);
                    bgRect.top = (int) (frame.bottom - bgMarginBottom - bgHeight);
                    canvas.drawRoundRect(bgRect, bgRadius, bgRadius, flashLightBgPaint);        //绘制圆角矩形

                    flashRect.left = (int) (bgRect.left + leftRightPadding);
                    flashRect.right = (int) (bgRect.left + leftRightPadding + flashLightBitmap.getWidth());
                    flashRect.bottom = (int) (bgRect.bottom - bgHeight / 2 + flashLightBitmap.getHeight() / 2);
                    flashRect.top = (int) (bgRect.bottom - bgHeight / 2 - flashLightBitmap.getHeight() / 2);
                    canvas.drawBitmap(flashLightBitmap, null, flashRect, paint);

                    flashTextRect.left = (int) ((bgRect.right - leftRightPadding - flashLightTextPaint.measureText(flashLightOpenText)));
                    flashTextRect.right = (int) (bgRect.right - leftRightPadding);
                    flashTextRect.bottom = (int) bgRect.bottom;
                    flashTextRect.top = (int) bgRect.top;
                    Paint.FontMetrics fontMetrics = flashLightTextPaint.getFontMetrics();
                    float top = fontMetrics.top;
                    float bottom = fontMetrics.bottom;
                    int baseLineY = (int) (flashTextRect.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式
                    canvas.drawText(flashLightOpenText, flashTextRect.centerX(), baseLineY, flashLightTextPaint);
                }
            }
            if (!isWeakLight && !isOpenFlashLight) {
                //绘制中间的线
                slideTop += SPEEN_DISTANCE;
                if (slideTop >= frame.bottom) {
                    slideTop = frame.top;
                }

                lineRect.left = frame.left + MIDDLE_LINE_PADDING;
                lineRect.right = frame.right - MIDDLE_LINE_PADDING;
                lineRect.top = slideTop - scanLineBitmap.getHeight() / 2;
                lineRect.bottom = slideTop + scanLineBitmap.getHeight() / 2;
                canvas.drawBitmap(scanLineBitmap, null, lineRect, paint);
            }
            if (isOpenFlashLight) {
                bgRect.left = (int) ((width - bgWidth) / 2);
                bgRect.right = (int) ((width + bgWidth) / 2);
                bgRect.bottom = (int) (frame.bottom - bgMarginBottom);
                bgRect.top = (int) (frame.bottom - bgMarginBottom - bgHeight);
                canvas.drawRoundRect(bgRect, bgRadius, bgRadius, flashLightBgPaint);        //绘制圆角矩形

                flashOpenRect.left = (int) (bgRect.left + leftRightPadding);
                flashOpenRect.right = (int) (bgRect.left + leftRightPadding + openFlashLightBitmap.getWidth());
                flashOpenRect.bottom = (int) (bgRect.bottom - bgHeight / 2 + openFlashLightBitmap.getHeight() / 2);
                flashOpenRect.top = (int) (bgRect.bottom - bgHeight / 2 - openFlashLightBitmap.getHeight() / 2);
                canvas.drawBitmap(openFlashLightBitmap, null, flashOpenRect, paint);

                flashTextRect.left = (int) ((bgRect.right - leftRightPadding - flashLightTextPaint.measureText(flashLightCloseText)));
                flashTextRect.right = (int) (bgRect.right - leftRightPadding);
                flashTextRect.bottom = (int) bgRect.bottom;
                flashTextRect.top = (int) bgRect.top;
                Paint.FontMetrics fontMetrics = flashLightTextPaint.getFontMetrics();
                float top = fontMetrics.top;
                float bottom = fontMetrics.bottom;
                int baseLineY = (int) (flashTextRect.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式

                canvas.drawText(flashLightCloseText, flashTextRect.centerX(), baseLineY, flashLightTextPaint);
            }

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }


            //只刷新扫描框的内容，其他地方不刷新
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);

        }
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        isOpenFlashLight = !isOpenFlashLight && openFlashLight(x, y);
        if (null != mOnFlashLightStateChangeListener) {
            mOnFlashLightStateChangeListener.openFlashLight(isOpenFlashLight);
        }
        return super.onTouchEvent(event);
    }


    private boolean openFlashLight(int x, int y) {
        return bgRect.contains(x, y);
    }

    public void reDraw() {
        if (null != frame) {
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);
        }
    }

    public interface onFlashLightStateChangeListener {
        void openFlashLight(boolean open);
    }

    public void setOnFlashLightStateChangeListener(onFlashLightStateChangeListener onFlashLightStateChangeListener) {
        mOnFlashLightStateChangeListener = onFlashLightStateChangeListener;
    }
}