package c_study.shd.com.pageturning;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PagerTurningView extends View {
    public PagerTurningView(Context context) {
        this(context, null);
    }

    public PagerTurningView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerTurningView(Context context,@Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {

    }
}
