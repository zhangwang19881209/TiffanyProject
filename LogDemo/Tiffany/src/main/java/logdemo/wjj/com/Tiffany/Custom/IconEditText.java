package logdemo.wjj.com.Tiffany.Custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import logdemo.wjj.com.Tiffany.R;

/**
 * Created by Ezreal on 2015/9/25.
 */
public class IconEditText extends LinearLayout {

    private static final String TAG = IconEditText.class.getSimpleName();

    /**
     * UI的参数
     */
    private static final float ICON_WEIGHT = 0.15f;
    private static final float EDIT_TEXT_WEIGHT = 0.85f;


    private static final String HINT_PREFIX = " ";

    /**
     * 图片素材
     */
    private Integer _iconResource;

    /**
     * 提示用的字体
     */
    private String _hint;

    /**
     * 是否为密码属性
     */
    private boolean _isPassword = false;

    /**
     * 空间组成
     */
    private ImageView _icon;
    private EditText _editText;

    /**
     * 构造函数
     * @param context
     */
    public IconEditText(Context context) {
        this(context, null);
    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     */
    public IconEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public IconEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.parseAttributes(context, attrs);
        this.initialize();
    }

    /**
     * 解析出的自定义属性。
     *
     * @param context
     * @param attrs
     */
    private void parseAttributes(Context context, AttributeSet attrs) {
        Log.d(TAG, "parseAttributes()");
        if (attrs == null) {
            return;
        }

        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.IconEditText, 0, 0);

        try {
            _iconResource = a.getResourceId(R.styleable.IconEditText_iconSrc, 0);
            _hint = a.getString(R.styleable.IconEditText_hint);
            _isPassword = a.getBoolean(R.styleable.IconEditText_isPassword, false);

            Log.d(TAG, "{ _iconResource: " + _iconResource + ", _hint: " + _hint + ", _isPassword: " + _isPassword + "}");
        } catch (Exception ex) {
            Log.e(TAG, "Unable to parse attributes due to: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            a.recycle();
        }
    }

    /**
     * 初始化
     */
    private void initialize() {
        Log.d(TAG, "initialize()");

        // 强制水平
        this.setOrientation(LinearLayout.HORIZONTAL);

        // 创建Icon
        if (_icon == null) {
            _icon = new ImageView(this.getContext());
            _icon.setLayoutParams(
                    new LayoutParams(0, LayoutParams.MATCH_PARENT, ICON_WEIGHT)
            );
            _icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            if (_iconResource != null && _iconResource != 0) {
                _icon.setImageResource(_iconResource);
            }

            this.addView(_icon);
        }

        // 创建EditText
        if (_editText == null) {
            _editText = new EditText(this.getContext());
            _editText.setInputType(
                    _isPassword ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
            );
            _editText.setLayoutParams(
                    new LayoutParams(0, LayoutParams.MATCH_PARENT, EDIT_TEXT_WEIGHT)
            );

            if (_hint != null) {
                //格式处理
                _editText.setHint(String.format("%s%s", HINT_PREFIX, _hint.toLowerCase()));
            }

            _editText.setHintTextColor(getResources().getColor(R.color.white));
            _editText.setTextColor(getResources().getColor(R.color.white));
            _editText.setGravity(Gravity.BOTTOM);

            this.addView(_editText);
        }
    }

    /**
     * 获取输入内容。
     *
     * @return
     */
    public Editable getText() {
        return _editText.getText();
    }

    /**
     * 返回一个EditText.
     *
     * @return
     */
    public EditText getEditText() {
        return _editText;
    }

    /**
     * 返回icon.
     *
     * @return
     */
    public ImageView getImageView() {
        return _icon;
    }

}