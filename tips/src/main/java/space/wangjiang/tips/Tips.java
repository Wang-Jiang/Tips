package space.wangjiang.tips;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by WangJiang on 2017/4/15.
 * Tips是参考Android的Toast源码开发的，本质作用和Toast类似，用于向用户提示一些信息
 * 但是Tips是单例的，这意味着不会像Toast那样，不断弹出
 * 如果当前屏幕存在一个Tips，其他的显示Tips的代码都不会被执行，也不会等待当前的Tips消失后依次弹出
 */
public class Tips {

    private static final Tips mTips = new Tips();

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    private static View mView;
    private static ImageView mIcon;
    private static TextView mMessage;
    private static Context mContext;
    private static int mDuration;
    private static boolean mShowing = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mShowing) {
                mWM.removeView(mView);
                mShowing = false;
            }
        }
    };

    private static WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private static WindowManager mWM;

    static {
        mParams = new WindowManager.LayoutParams(); //初始化
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  //高
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;   //宽
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.Tips;  // 设置进入退出动画效果
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;   //如果屏幕上已经存在Toast的话，TYPE_TOAST在7.0的系统会报错
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.BOTTOM;        //对其方式
        mParams.y = 0;      //下间距
    }

    private Tips() {
        //禁止实例化
    }

    public static Tips success(Context context, CharSequence message, int duration) {
        return success(context, getDrawable(context, R.drawable.ic_tips_success), message, duration);
    }

    public static Tips success(Context context, Drawable icon, CharSequence message, int duration) {
        return success(context, icon, message, duration, Gravity.BOTTOM, 0);
    }

    public static Tips success(Context context, Drawable icon, CharSequence message, int duration, int gravity, int offsetY) {
        return makeText(context, icon, getColor(context, R.color.tipsColorSuccess), message, getColor(context, R.color.tipsTextColor), duration, gravity, offsetY);
    }

    public static Tips error(Context context, CharSequence message, int duration) {
        return error(context, getDrawable(context, R.drawable.ic_tips_error), message, duration);
    }

    public static Tips error(Context context, Drawable icon, CharSequence message, int duration) {
        return error(context, icon, message, duration, Gravity.BOTTOM, 0);
    }

    public static Tips error(Context context, Drawable icon, CharSequence message, int duration, int gravity, int offsetY) {
        return makeText(context, icon, getColor(context, R.color.tipsColorError), message, getColor(context, R.color.tipsTextColor), duration, gravity, offsetY);
    }

    public static Tips warning(Context context, CharSequence message, int duration) {
        return warning(context, getDrawable(context, R.drawable.ic_tips_warning), message, duration);
    }

    public static Tips warning(Context context, Drawable icon, CharSequence message, int duration) {
        return warning(context, icon, message, duration, Gravity.BOTTOM, 0);
    }

    public static Tips warning(Context context, Drawable icon, CharSequence message, int duration, int gravity, int offsetY) {
        return makeText(context, icon, getColor(context, R.color.tipsColorWarning), message, getColor(context, R.color.tipsTextColor), duration, gravity, offsetY);
    }

    public static Tips normal(Context context, CharSequence message, int duration) {
        return normal(context, getDrawable(context, R.drawable.ic_tips_info), message, duration);
    }

    public static Tips normal(Context context, Drawable icon, CharSequence message, int duration) {
        return normal(context, icon, message, duration, Gravity.BOTTOM, 0);
    }

    public static Tips normal(Context context, Drawable icon, CharSequence message, int duration, int gravity, int offsetY) {
        return makeText(context, icon, getColor(context, R.color.tipsColorNormal), message, getColor(context, R.color.tipsTextColor), duration, gravity, offsetY);
    }

    public static Tips info(Context context, CharSequence message, int duration) {
        return info(context, getDrawable(context, R.drawable.ic_tips_info), message, duration);
    }

    public static Tips info(Context context, Drawable icon, CharSequence message, int duration) {
        return info(context, icon, message, duration, Gravity.BOTTOM, 0);
    }

    public static Tips info(Context context, Drawable icon, CharSequence message, int duration, int gravity, int offsetY) {
        return makeText(context, icon, getColor(context, R.color.tipsColorInfo), message, getColor(context, R.color.tipsTextColor), duration, gravity, offsetY);
    }

    /**
     * 完全自定义Tips
     */
    public static Tips makeText(Context context, Drawable icon, @ColorInt int toasterColor, CharSequence message, @ColorInt int textColor, int duration, int gravity, int offsetY) {
        if (mShowing) return mTips;

        mContext = context;
        mDuration = duration;
        if (mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.tips_layout, null);
            mIcon = (ImageView) mView.findViewById(R.id.tips_icon);
            mMessage = (TextView) mView.findViewById(R.id.tips_text);
        }
        mView.setBackgroundColor(toasterColor);
        mIcon.setImageDrawable(icon);
        mMessage.setText(message);
        mMessage.setTextColor(textColor);

        //设置位置及偏移
        mParams.gravity = gravity;
        mParams.y = offsetY;
        return mTips;
    }

    public void show() {
        if (!mShowing) {
            mWM = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            //当前没有Tips显示
            mShowing = true;
            try {
                mWM.addView(mView, mParams);
                int delay;
                if (mDuration == LENGTH_LONG) {
                    delay = 3000;
                } else {
                    delay = 1600;
                }
                mHandler.sendEmptyMessageDelayed(0, delay);
            } catch (Exception e) {
                //android7.0系统会出错，当屏幕存在toast的时候，addView会导致抛出异常，其他系统正常
                //但是try并没有解决当前屏幕存在Tips，弹出一个Toast程序崩溃的问题
                e.printStackTrace();
                mShowing = false;
            }
        }
    }

    /**
     * 兼容处理
     * Use Support V4 Library
     */
    private static Drawable getDrawable(Context context, @DrawableRes int drawableId) {
        return ContextCompat.getDrawable(context, drawableId);
    }

    /**
     * 使用兼容包
     * Use Support V4 Library
     */
    private static
    @ColorInt
    int getColor(Context context, @ColorRes int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

}
