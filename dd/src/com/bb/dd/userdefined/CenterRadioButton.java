package com.bb.dd.userdefined;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

import com.bb.dd.R;

/**
 * @author Administrator
 *
 */
public class CenterRadioButton extends RadioButton {

	Drawable buttonDrawable;

	private void init(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.CompoundButton, 0, 0);
			buttonDrawable = a.getDrawable(1);

			setButtonDrawable(android.R.color.transparent);
		}
	}

	public CenterRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CenterRadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (buttonDrawable != null) {
			buttonDrawable.setState(getDrawableState());
			final int verticalGravity = getGravity()
					& Gravity.VERTICAL_GRAVITY_MASK;
			final int height = buttonDrawable.getIntrinsicHeight();

			int y = 0;

			switch (verticalGravity) {
			case Gravity.BOTTOM:
				y = getHeight() - height;
				break;
			case Gravity.CENTER_VERTICAL:
				y = (int) ((getHeight() - height) * 0.24);
				break;
			}

			int buttonWidth = buttonDrawable.getIntrinsicWidth();
			int buttonLeft = (getWidth() - buttonWidth) / 2;
			buttonDrawable.setBounds(buttonLeft, y, buttonLeft + buttonWidth, y
					+ height);
			buttonDrawable.draw(canvas);
		}
	}
}
