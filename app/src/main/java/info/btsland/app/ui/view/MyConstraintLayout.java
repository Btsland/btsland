package info.btsland.app.ui.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/11/27.
 */

public class MyConstraintLayout extends ConstraintLayout {
    public boolean isForbidden=false;
    public MyConstraintLayout(Context context) {
        super(context);
    }

    public MyConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        if (isForbidden) {
            return true;
        } else {
            return super.onInterceptHoverEvent(event);
        }
    }
}
