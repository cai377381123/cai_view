package cn.edu.nuc.csce.dcldims.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class TextMoveLayout extends ViewGroup {
    public TextMoveLayout(Context context) {
        super(context);
    }

    public TextMoveLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextMoveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
}
