package com.greentech.dhakacitybusroute;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView linksTV = (TextView) findViewById(R.id.links);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("\nInformation Collected from \n\n");

        SpannableString sb = new SpannableString (getResources().getString(R.string.link1)+"\n");
        SpannableString sb2 = new SpannableString (getResources().getString(R.string.link2)+"\n");

        int color = ContextCompat.getColor(this, R.color.colorAccent);
        int bgColor = ContextCompat.getColor(this, R.color.colorPrimary);

        sb.setSpan(new TouchableSpan(color, bgColor, sb), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(sb);

        sb2.setSpan(new TouchableSpan(color, bgColor, sb2), 0, sb2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(sb2);

        linksTV.setText(spannableStringBuilder);
        linksTV.setMovementMethod(new LinkTouchMovementMethod());

    }


    private class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
//                Toast.makeText(getActivity(), "link touched", Toast.LENGTH_SHORT).show();
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
//                Toast.makeText(getActivity(), "link untouched", Toast.LENGTH_SHORT).show();
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }

    }

    public class TouchableSpan extends ClickableSpan {
        private boolean mIsPressed;
        private int mPressedBackgroundColor;
        private int mNormalTextColor;
        private SpannableString link;
//        private int mPressedTextColor;

        public TouchableSpan(int normalTextColor, int pressedBackgroundColor, SpannableString link) {
            mNormalTextColor = normalTextColor;
//            mPressedTextColor = pressedTextColor;
            mPressedBackgroundColor = pressedBackgroundColor;
            this.link = link;
        }

        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }


        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mNormalTextColor);
            ds.bgColor = mIsPressed ? mPressedBackgroundColor : 0x00000000;
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View widget) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link.toString())));
        }
    }
}
