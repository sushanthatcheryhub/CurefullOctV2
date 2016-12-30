package toggle.button;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

public class SingleSelectToggleGroup extends ToggleButtonGroup {
    private static final String LOG_TAG = MultiSelectToggleGroup.class.getSimpleName();

    public SingleSelectToggleGroup(Context context) {
        this(context, null);
    }

    public SingleSelectToggleGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onToggleButtonClicked(int position) {
        uncheckAll();
        mButtons.get(position).setChecked(true, isAnimationEnabled());
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChange(position, true);
        }
        if (mOnCheckedPositionChangeListener != null) {
            mOnCheckedPositionChangeListener.onCheckedPositionChange(getCheckedPositions());
        }
    }

    @Override
    public void setButtons(List<String> text) {
        super.setButtons(text);
        if (text.size() > 0) {
            mButtons.get(0).setChecked(true);
        }
    }

    @Override
    public void setCheckedAt(int position, boolean isChecked) {
        uncheckAll();
        super.setCheckedAt(position, isChecked);
    }
}
