package info.btsland.app.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/10/14.
 */

public class RowLinearLayout extends LinearLayout {
    private String leftCoin;
    private String rightCoin;

    public RowLinearLayout(Context context) {
        super(context);
    }

    public RowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RowLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RowLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public String getLeftCoin() {
        return leftCoin;
    }

    public RowLinearLayout setLeftCoin(String leftCoin) {
        this.leftCoin = leftCoin;
        return this;
    }

    public String getRightCoin() {
        return rightCoin;
    }

    public RowLinearLayout setRightCoin(String rightCoin) {
        this.rightCoin = rightCoin;
        return this;
    }


}
