package cn.qrcodecore;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.dell.qr_core.R;

/**
 * Created by yanweiGeorge on 2016/4/29.
 */
public class ScanBoxView extends View {

    private Paint mPaint;
    private static int mMoveStepDistance;
    private Rect mFramingRect;

    private int mTopOffset;
    //扫描框边角线的宽度
    private int mCornerSize;
    private int mCornerLength;
    private int mCornerColor;
    //扫描框的宽度
    private int mRectWidth;
    private int mMaskColor;
    private int mScanLineSize;
    private int mScanLineColor;
    private int mScanLineHorizontalMargin;
    private boolean mIsShowDefaultScanLineDrawable;
    private Drawable mCustomScanLineDrawable;
    //扫描边框的宽度，默认值为1dp
    private int mBorderSize;
    private int mAnimtime;
    private Bitmap mScanLineBitmap;
    private int mBorderColor;
    private int mAnimDelayTime;

    private float mHalfCornerSize;



    public ScanBoxView(Context context){
        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mMaskColor = Color.parseColor("#33FFFFFF");
        mCornerColor = Color.WHITE;
        mCornerLength = DisplayUtils.dp2px(context,20);
        mCornerSize = DisplayUtils.dp2px(context,3);
        mScanLineSize = DisplayUtils.dp2px(context,1);
        mScanLineColor = Color.WHITE;
        mTopOffset = DisplayUtils.dp2px(context,90);
        mRectWidth = DisplayUtils.dp2px(context,200);
        mScanLineHorizontalMargin = 0;
        mIsShowDefaultScanLineDrawable = false;
        mCustomScanLineDrawable = null;
        mScanLineBitmap = null;
        mBorderSize = DisplayUtils.dp2px(context,1);
        mBorderColor = Color.WHITE;
        mAnimtime = 1000;

        mMoveStepDistance = DisplayUtils.dp2px(context,2);



    }

    public void initCustomAttrs(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QRCodeView);
        final int count = typedArray.getIndexCount();
        for(int i = 0; i < count; i++){
            initCustomAttr(typedArray.getIndex(i), typedArray);

        }
        typedArray.recycle();

        //afterInitCustomAttrs();

    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.QRCodeView_qrcv_topOffset) {
            mTopOffset = typedArray.getDimensionPixelSize(attr, mTopOffset);
        } else if (attr == R.styleable.QRCodeView_qrcv_cornerSize) {
            mCornerSize = typedArray.getDimensionPixelSize(attr, mCornerSize);
        } else if (attr == R.styleable.QRCodeView_qrcv_cornerLength) {
            mCornerLength = typedArray.getDimensionPixelSize(attr, mCornerLength);
        } else if (attr == R.styleable.QRCodeView_qrcv_scanLineSize) {
            mScanLineSize = typedArray.getDimensionPixelSize(attr, mScanLineSize);
        } else if (attr == R.styleable.QRCodeView_qrcv_rectWidth) {
            mRectWidth = typedArray.getDimensionPixelSize(attr, mRectWidth);
        } else if (attr == R.styleable.QRCodeView_qrcv_maskColor) {
            mMaskColor = typedArray.getColor(attr, mMaskColor);
        } else if (attr == R.styleable.QRCodeView_qrcv_cornerColor) {
            mCornerColor = typedArray.getColor(attr, mCornerColor);
        } else if (attr == R.styleable.QRCodeView_qrcv_scanLineColor) {
            mScanLineColor = typedArray.getColor(attr, mScanLineColor);
        } else if (attr == R.styleable.QRCodeView_qrcv_scanLineHorizontalMargin) {
            mScanLineHorizontalMargin = typedArray.getDimensionPixelSize(attr, mScanLineHorizontalMargin);
        } else if (attr == R.styleable.QRCodeView_qrcv_isShowDefaultScanLineDrawable) {
            mIsShowDefaultScanLineDrawable = typedArray.getBoolean(attr, mIsShowDefaultScanLineDrawable);
        } else if (attr == R.styleable.QRCodeView_qrcv_customScanLineDrawable) {
            mCustomScanLineDrawable = typedArray.getDrawable(attr);
        } else if (attr == R.styleable.QRCodeView_qrcv_borderSize) {
            mBorderSize = typedArray.getDimensionPixelSize(attr, mBorderSize);
        } else if (attr == R.styleable.QRCodeView_qrcv_borderColor) {
            mBorderColor = typedArray.getColor(attr, mBorderColor);
        } else if (attr == R.styleable.QRCodeView_qrcv_animTime) {
            mAnimtime = typedArray.getInteger(attr, mAnimtime);
        }
    }


        private void afterInitCustomAttr(){
            if (mIsShowDefaultScanLineDrawable){
                Bitmap defaultScanLineBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.qrcode_default_scan_line);
                mScanLineBitmap = makeTintBitmap(defaultScanLineBitmap,mScanLineColor);
            }
            if (mCustomScanLineDrawable != null){
                mScanLineBitmap = ((BitmapDrawable) mCustomScanLineDrawable).getBitmap();
            }

            mAnimDelayTime = (int) ((1.0f * mAnimtime * mMoveStepDistance));
            mHalfCornerSize = 1.0f *mCornerSize/2;

    }


    public static Bitmap makeTintBitmap(Bitmap src,int tintColor){
        Bitmap result = Bitmap.createBitmap(src.getWidth(),src.getHeight(),src.getConfig());
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src,0,0,paint);
        return result;
    }


    @Override
    public void onDraw(Canvas canvas){
        if (mFramingRect == null){
            return;
        }

        // 画遮罩层
        drawMask(canvas);
    }

    /**
     * 画遮罩层
     */
    private void drawMask(Canvas canvas){
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if (mMask)
    }




}
