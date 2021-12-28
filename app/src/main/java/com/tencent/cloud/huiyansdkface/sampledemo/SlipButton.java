package com.tencent.cloud.huiyansdkface.sampledemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.tencent.cloud.huiyansdkface.sampledemo.R;


/**
 * Created by livili on 2015/8/11.
 */
public class SlipButton extends View implements View.OnTouchListener {

    private boolean isOpen;

    private boolean OnSlip = false;
    private float NowX;
    private Rect Btn_On, Btn_Off;

    private boolean isChgLsnOn = false;
    private onChangedCallBack ChgLsn;

    private Bitmap bg_on, bg_off, slip_btn;

    public SlipButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public SlipButton(Context context, boolean isOpen) {
        super(context);
        this.isOpen = isOpen;
        init();
    }

    public SlipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        bg_on = BitmapFactory.decodeResource(getResources(), R.mipmap.wbcf_demo_slip_bg_open);
        bg_off = BitmapFactory.decodeResource(getResources(), R.mipmap.wbcf_demo_slip_bg_close);
        slip_btn = BitmapFactory.decodeResource(getResources(), R.mipmap.wbcf_demo_slip_button);

        Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
        Btn_Off = new Rect(
                bg_off.getWidth() - slip_btn.getWidth(),
                0,
                bg_off.getWidth(),
                slip_btn.getHeight());
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        float x;

        if (isOpen) {
            NowX = bg_on.getWidth();
        } else {
            NowX = 0;
        }

        {
            if (NowX < (bg_on.getWidth() / 2))
                canvas.drawBitmap(bg_off, matrix, paint);
            else
                canvas.drawBitmap(bg_on, matrix, paint);

            if (OnSlip) {
                if (NowX >= bg_on.getWidth())
                    x = bg_on.getWidth() - slip_btn.getWidth() / 2;
                else
                    x = NowX - slip_btn.getWidth() / 2;
            } else {
                if (isOpen) {
                    x = Btn_Off.left;
                } else {
                    x = Btn_On.left;
                }

            }
            if (x < 0)
                x = 0;
            else if (x > bg_on.getWidth() - slip_btn.getWidth())
                x = bg_on.getWidth() - slip_btn.getWidth();
            canvas.drawBitmap(slip_btn, x, 0, paint);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                NowX = event.getX();
                break;
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > bg_on.getWidth() || event.getY() > bg_on.getHeight())
                    return false;
                OnSlip = true;
                float downX = event.getX();
                NowX = downX;
                break;
            case MotionEvent.ACTION_UP:
                OnSlip = false;
                boolean LastChoose = isOpen;
                isOpen = event.getX() >= (bg_on.getWidth() / 2);

                if (isChgLsnOn && (LastChoose != isOpen))
                    ChgLsn.OnChanged(isOpen);
                break;
            default:

        }
        invalidate();
        return true;
    }

    public void setCheck(boolean isChecked) {
        if (isOpen != isChecked) {
            isOpen = isChecked;
            invalidate();
        }
    }

    public void SetOnChangedCallBack(onChangedCallBack l) {
        isChgLsnOn = true;
        ChgLsn = l;
    }

    public interface onChangedCallBack {
        void OnChanged(boolean CheckState);
    }

}

