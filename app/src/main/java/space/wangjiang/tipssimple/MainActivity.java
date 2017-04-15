package space.wangjiang.tipssimple;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import space.wangjiang.tips.Tips;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.system_default_toast).setOnClickListener(this);
        findViewById(R.id.success_tips).setOnClickListener(this);
        findViewById(R.id.error_tips).setOnClickListener(this);
        findViewById(R.id.warning_tips).setOnClickListener(this);
        findViewById(R.id.error_tips).setOnClickListener(this);
        findViewById(R.id.normal_tips).setOnClickListener(this);
        findViewById(R.id.info_tips).setOnClickListener(this);
        findViewById(R.id.custom_tips).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_default_toast:
                Toast.makeText(getApplicationContext(), "系统默认的Toast", Toast.LENGTH_SHORT).show();
                break;
            case R.id.success_tips:
                Tips.success(getApplicationContext(), "数据更新完成", Toast.LENGTH_SHORT).show();
                break;
            case R.id.warning_tips:
                Tips.warning(getApplicationContext(), "一个小小的警告", Toast.LENGTH_SHORT).show();
                break;
            case R.id.error_tips:
                Tips.error(getApplicationContext(), "当前网络异常", Toast.LENGTH_SHORT).show();
                break;
            case R.id.normal_tips:
                Tips.normal(getApplicationContext(), getIcon(R.drawable.ic_tips_info), "位于上方且偏移100", Tips.LENGTH_LONG, Gravity.TOP, 100).show();
                break;
            case R.id.info_tips:
                Tips.info(getApplicationContext(), getIcon(R.drawable.ic_tips_info), "位于下方且偏移100", Tips.LENGTH_LONG, Gravity.BOTTOM, 100).show();
                break;
            case R.id.custom_tips:
                Tips.makeText(getApplicationContext(), getIcon(R.drawable.ic_coffee_cup), Color.parseColor("#885734"), "位于中间且向上偏移200", Color.parseColor("#FFFFFF"), Tips.LENGTH_LONG, Gravity.CENTER, -200).show();
                break;
        }
    }

    public Drawable getIcon(@DrawableRes int drawableId) {
        return ContextCompat.getDrawable(getApplicationContext(), drawableId);
    }
}
