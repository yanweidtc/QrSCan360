package cn.qrcodecore;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.dell.qr_core.R;

/**
 * Created by yanweiGeorge on 2016/4/29.
 */
public class ScanBoxView extends View {

    private Paint mPaint;

    private int mTopOffset;
    private int mCornerSize;
    private int mCornerLength;
    private int mCornerColor;
    private int mRectWidth;


    public ScanBoxView(Context context){
        super(context);
        mPaint = new Paint();
    }

    public void initCustomAttrs(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QRCodeView);
        final int count = typedArray.getIndexCount();
        for(int i = 0; i < count; i++){
            //initCustomAttr(typedArray.getIndex(i), typedArray);

        }
        typedArray.recycle();

        //afterInitCustomAttrs();

    }

    private void initCustomAttr(int attr, TypedArray typedArray){
        if(attr == R.styleable.QRCodeView_qrcv_topOffset){
            mTopOffset = typedArray.getDimensionPixelSize(attr,mTopOffset);
        }else if (attr == R.styleable.QRCodeView_qrcv_cornerSize){
            mCornerSize = typedArray.getDimensionPixelSize(attr,mCornerSize);
        }else if (attr == R.styleable.QRCodeView_qrcv_cornerLength){
            mCornerLength = typedArray.getDimensionPixelSize(attr,mCornerLength);
        }else if (attr == R.styleable.QRCodeView_qrcv_cornerColor){
            mCornerColor = typedArray.getDimensionPixelSize(attr,mCornerColor);
        }else if (attr == R.styleable.QRCodeView_qrcv_rectWidth){
            mRectWidth = typedArray.getDimensionPixelSize(attr,mRectWidth);
        }

    }

}
