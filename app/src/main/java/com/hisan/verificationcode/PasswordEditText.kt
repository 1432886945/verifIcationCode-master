package com.hisan.verificationcode

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue

/**
 * 自定义验证码\密码输入框
 */
class PasswordEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : android.support.v7.widget.AppCompatEditText(context, attrs) {
    // 画笔
    private var mPaint: Paint? = null
    // 一个字符所占的宽度
    private var mPasswordItemWidth: Int = 0
    // 验证码的个数默认为6位数
    private val mPasswordNumber = 6
    // 背景边框颜色
    private var mBgColor = R.color.home_titel
    // 背景边框大小
    private var mBgSize = 1
    // 背景边框圆角大小
    private var mBgCorner = 0
    // 分割线的颜色
    private var mDivisionLineColor = mBgColor
    // 分割线的大小
    private var mDivisionLineSize = 1
    // 密码颜色
    private var mPasswordColor = mDivisionLineColor
    private var isPass:Boolean?=false


    private var passwordFullListener: PasswordFullListener? = null

    init {
        initPaint()
        initAttributeSet(context, attrs)
        // 不显示光标
        isCursorVisible = false
    }

    /**
     * 初始化属性
     */
    @SuppressLint("ResourceAsColor")
    private fun initAttributeSet(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText)
        isPass=array.getBoolean(R.styleable.PasswordEditText_isPass, false)
        // 获取大小
        mDivisionLineSize = array.getDimension(R.styleable.PasswordEditText_divisionLineSize,dip2px(mDivisionLineSize).toFloat()).toInt()
        //mPasswordRadius = (int) array.getDimension(R.styleable.PasswordEditText_passwordRadius, dip2px(mPasswordRadius));
        mBgSize = array.getDimension(R.styleable.PasswordEditText_bgSize, dip2px(mBgSize).toFloat()).toInt()
        mBgCorner = array.getDimension(R.styleable.PasswordEditText_bgCorner, 0f).toInt()
        // 获取颜色
        mBgColor = array.getColor(R.styleable.PasswordEditText_bgColor, mBgColor)
        mDivisionLineColor = array.getColor(R.styleable.PasswordEditText_divisionLineColor, mDivisionLineColor)
        mPasswordColor = array.getColor(R.styleable.PasswordEditText_passwordColor, mDivisionLineColor)
        array.recycle()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true
    }

    /**
     * dip 转 px
     */
    private fun dip2px(dip: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip.toFloat(), resources.displayMetrics).toInt()
    }

    //绘制
    override fun onDraw(canvas: Canvas) {
        val passwordWidth = width - (mPasswordNumber - 1) * mDivisionLineSize
        mPasswordItemWidth = passwordWidth / mPasswordNumber
        // 绘制背景
        drawBg(canvas)
        // 绘制分割线
        drawDivisionLine(canvas)
        // 绘制密码
        drawHidePassword(canvas)
    }


    /**
     * 绘制背景
     */
    @SuppressLint("ResourceAsColor")
    private fun drawBg(canvas: Canvas) {
        mPaint!!.color = mBgColor
        // 设置画笔为空心
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = mBgSize.toFloat()
        val rectF = RectF(mBgSize.toFloat(), mBgSize.toFloat(), (width - mBgSize).toFloat(), (height - mBgSize).toFloat())
        // 如果没有设置圆角，就画矩形
        if (mBgCorner == 0) {
            canvas.drawRect(rectF, mPaint!!)
        } else {
            // 如果有设置圆角就画圆矩形
            canvas.drawRoundRect(rectF, mBgCorner.toFloat(), mBgCorner.toFloat(), mPaint!!)
        }
    }

    /**
     * 绘制密码
     */
    @SuppressLint("ResourceAsColor")
    private fun drawHidePassword(canvas: Canvas) {
        val passwordLength = text.length
        if (isPass!!)mPaint!!.textSize = 48f
        else mPaint!!.textSize = 24f
        mPaint!!.color = mPasswordColor
        mPaint!!.style = Paint.Style.FILL
        var pass = ""
        for (i in 0 until passwordLength) {
            val cx = i * mDivisionLineSize + i * mPasswordItemWidth + mPasswordItemWidth / 2 + mBgSize-10
            if (isPass!!){
                pass = "*"
                canvas.drawText(pass, cx.toFloat(), (height / 2+30).toFloat(), mPaint!!)
            }else{
                pass = text.toString().substring(i, i + 1)
                canvas.drawText(pass, cx.toFloat(), (height / 2 + 10).toFloat(), mPaint!!)
            }
        }
    }

    /**
     * 绘制分割线
     */
    @SuppressLint("ResourceAsColor")
    private fun drawDivisionLine(canvas: Canvas) {
        mPaint!!.strokeWidth = mDivisionLineSize.toFloat()
        mPaint!!.color = mDivisionLineColor
        for (i in 0 until mPasswordNumber - 1) {
            val startX = (i + 1) * mDivisionLineSize + (i + 1) * mPasswordItemWidth + mBgSize
            canvas.drawLine(startX.toFloat(), mBgSize.toFloat(), startX.toFloat(), (height - mBgSize).toFloat(), mPaint!!)
        }
    }

    /**
     * 添加密码
     */
    fun addPassword(number: String) {
        var number = number
        number = text.toString().trim { it <= ' ' } + number
        if (number.length > mPasswordNumber) {
            return
        }
        setText(number)
        if (number.length == mPasswordNumber) {
            if (null != passwordFullListener) {
                passwordFullListener!!.passwordFull(number)
            }
        }
    }

    /**
     * 删除最后一位密码
     */
    fun deleteLastPassword() {
        var currentText = text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(currentText)) {
            return
        }
        currentText = currentText.substring(0, currentText.length - 1)
        setText(currentText)
    }

    interface PasswordFullListener {
        fun passwordFull(content: String)
    }

    fun setOnPasswordFullListener(passwordFullListener: PasswordFullListener) {
        this.passwordFullListener = passwordFullListener
    }

    fun setIsPass(mIsPass:Boolean){
        isPass=mIsPass
        if (text.isNotEmpty()) setText("")
        invalidate()
    }

}