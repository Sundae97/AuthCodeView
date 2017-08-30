package com.sundae.authcodeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by daijiyuan on 2017/8/30.
 * 邮箱 948820549@qq.com
 *
 * @Sundae
 */

public class AuthCodeView extends RelativeLayout {

    private Context mContext;

    private LinearLayout editTextContainer;
    private EditText mEditText;
    private LinearLayout.LayoutParams mLayoutParams;

    private int count = 0;

    private int mEtNumber;
    private int mEtWidth;
    private int mEtTextSize;
    private int mEtTextColor;

    private int marginLeft , marginRight , marginTop , marginBottom;

    private Drawable mEtDividerDrawable;
    private Drawable mEtBackgroundDrawableFocus;
    private Drawable mEtBackgroundDrawableNormal;

    private TextView mTextViews[];

    private AuthCodeTextChangeListener authCodeTextChangeListener;

    public AuthCodeView(Context context) {
        this(context , null);
    }

    public AuthCodeView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public AuthCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context , attrs , defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;

        LayoutInflater.from(context).inflate(R.layout.authcode_layout , this);

        editTextContainer = (LinearLayout) this.findViewById(R.id.container_et);
        mEditText = (EditText) this.findViewById(R.id.authcode_edit);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AuthCodeView, defStyleAttr, 0);
        mEtNumber = typedArray.getInteger(R.styleable.AuthCodeView_edit_number, 1);
        mEtWidth = typedArray.getDimensionPixelSize(R.styleable.AuthCodeView_edit_width, 42);
        mEtDividerDrawable = typedArray.getDrawable(R.styleable.AuthCodeView_edit_divider_drawable);
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.AuthCodeView_edit_text_size, 16);
        mEtTextColor = typedArray.getColor(R.styleable.AuthCodeView_edit_text_color, Color.WHITE);
        mEtBackgroundDrawableFocus = typedArray.getDrawable(R.styleable.AuthCodeView_edit_bg_focus);
        mEtBackgroundDrawableNormal = typedArray.getDrawable(R.styleable.AuthCodeView_edit_bg_normal);

        marginLeft = typedArray.getDimensionPixelOffset(R.styleable.AuthCodeView_edit_marginLeft , 0);
        marginTop = typedArray.getDimensionPixelOffset(R.styleable.AuthCodeView_edit_marginTop , 0);
        marginRight = typedArray.getDimensionPixelOffset(R.styleable.AuthCodeView_edit_marginRight , 0);
        marginBottom = typedArray.getDimensionPixelOffset(R.styleable.AuthCodeView_edit_marginBottom , 0);

        typedArray.recycle();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        build();
    }

    private void initTextViews(Context context, int etNumber, int etWidth, Drawable etDividerDrawable, float etTextSize, int etTextColor) {
        // 设置 editText 的输入长度
        //将光标隐藏
        mEditText.setCursorVisible(false);
        //最大输入长度
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(etNumber)});
        // 设置分割线的宽度
        if (etDividerDrawable != null) {
            etDividerDrawable.setBounds(0, 0, etDividerDrawable.getMinimumWidth(), etDividerDrawable.getMinimumHeight());
            editTextContainer.setDividerDrawable(etDividerDrawable);
        }

        mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(marginLeft,marginTop,marginRight,marginBottom);

        mTextViews = new TextView[etNumber];
        for (int i = 0; i < mTextViews.length; i++) {
            TextView textView = new EditText(context);
            textView.setTextSize(etTextSize);
            textView.setTextColor(etTextColor);
            textView.setWidth(etWidth);
            textView.setHeight(etWidth);
            // 设置外边距
            textView.setLayoutParams(mLayoutParams);

//            //默认设置第一个为选中样式
//            if (i == 0) {
//                textView.setBackgroundDrawable(mEtBackgroundDrawableFocus);
//            } else {
//                textView.setBackgroundDrawable(mEtBackgroundDrawableNormal);
//            }
            textView.setBackgroundDrawable(mEtBackgroundDrawableNormal);

            textView.setGravity(Gravity.CENTER);

            textView.setFocusable(false);

            mTextViews[i] = textView;
        }

    }

    private void initEtContainer() {
        for (int i = 0; i < mTextViews.length; i++) {
            editTextContainer.addView(mTextViews[i]);
        }
    }

    private void setListener() {
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputStr = editable.toString();
                if (inputStr != null && !inputStr.equals("")) {
                    setText(inputStr);
                    mEditText.setText("");
                }
            }
        });

        // 监听删除按键
        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onKeyDelete();
                    return true;
                }
                return false;
            }
        });

        mEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG" , "setOnClickListener count=" + count);
                if(count < mEtNumber)
                    mTextViews[count].setBackgroundDrawable(mEtBackgroundDrawableFocus);
            }
        });

    }

    // 给TextView 设置文字
    public void setText(String inputContent) {
        for (int i = 0; i < mTextViews.length; i++) {
            TextView tv = mTextViews[i];
            if (tv.getText().toString().trim().equals("")) {
                tv.setText(inputContent);
                // 添加输入完成的监听
                //TODO
                count++;
                if(authCodeTextChangeListener != null) {
                    authCodeTextChangeListener.onInput(inputContent.charAt(0));
                    if(i == mEtNumber-1)
                        authCodeTextChangeListener.onInputFinish(getText());
                }
                tv.setBackgroundDrawable(mEtBackgroundDrawableNormal);
                if (i < mEtNumber - 1) {
                    mTextViews[i + 1].setBackgroundDrawable(mEtBackgroundDrawableFocus);
                }
                break;
            }
        }
    }


    // 监听删除
    public void onKeyDelete() {
        if(count == 0)
            mTextViews[0].setBackgroundDrawable(mEtBackgroundDrawableNormal);

        for (int i = mTextViews.length - 1; i >= 0; i--) {
            TextView tv = mTextViews[i];
            if (!tv.getText().toString().trim().equals("")) {
                tv.setText("");
                // 添加删除完成监听
                count--;
                if(authCodeTextChangeListener != null) authCodeTextChangeListener.onDelete();
                tv.setBackgroundDrawable(mEtBackgroundDrawableFocus);
                if (i < mEtNumber - 1) {
                    mTextViews[i + 1].setBackgroundDrawable(mEtBackgroundDrawableNormal);
                }
                break;
            }
        }
    }

    public String getText(){
        String s = "";
        for (TextView v : mTextViews) {
            s+= v.getText().toString().trim();
        }
        return s.trim();
    }

    public AuthCodeView setmEtNumber(int mEtNumber) {
        this.mEtNumber = mEtNumber;
        return AuthCodeView.this;
    }

    public AuthCodeView setmEtTextSize(int mEtTextSize) {
        this.mEtTextSize = mEtTextSize;
        return AuthCodeView.this;
    }

    public AuthCodeView setmEtTextColor(int mEtTextColor) {
        this.mEtTextColor = mEtTextColor;
        return AuthCodeView.this;
    }

    public AuthCodeView setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
        return AuthCodeView.this;
    }

    public AuthCodeView setMarginRight(int marginRight) {
        this.marginRight = marginRight;
        return AuthCodeView.this;
    }

    public AuthCodeView setMarginTop(int marginTop) {
        this.marginTop = marginTop;
        return AuthCodeView.this;
    }

    public AuthCodeView setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
        return AuthCodeView.this;
    }

    public AuthCodeView setmEtDividerDrawable(Drawable mEtDividerDrawable) {
        this.mEtDividerDrawable = mEtDividerDrawable;
        return AuthCodeView.this;
    }

    public AuthCodeView setmEtBackgroundDrawableFocus(Drawable mEtBackgroundDrawableFocus) {
        this.mEtBackgroundDrawableFocus = mEtBackgroundDrawableFocus;
        return AuthCodeView.this;
    }

    public AuthCodeView setmEtBackgroundDrawableNormal(Drawable mEtBackgroundDrawableNormal) {
        this.mEtBackgroundDrawableNormal = mEtBackgroundDrawableNormal;
        return AuthCodeView.this;
    }

    public AuthCodeView setAuthCodeTextChangeListener(AuthCodeTextChangeListener authCodeTextChangeListener)
    {
        this.authCodeTextChangeListener = authCodeTextChangeListener;
        return AuthCodeView.this;
    }

    public void build()
    {
        initTextViews(mContext , mEtNumber , mEtWidth , mEtDividerDrawable , mEtTextSize , mEtTextColor);
        initEtContainer();
        setListener();
    }

    public interface AuthCodeTextChangeListener
    {
        void onInput(char code);
        void onDelete();
        void onInputFinish(String code);
    }

}
