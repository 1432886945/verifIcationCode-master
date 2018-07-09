package com.hisan.verificationcode

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


/**
 * 自定义键盘
 */
class CustomerKeyboard @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private var mListener: CustomerKeyboardClickListener? = null

    init {
        View.inflate(context, R.layout.ui_customer_keyboard, this)
        setChildViewOnclick(this)
    }

    /**
     * 设置键盘子View的点击事件
     */
    private fun setChildViewOnclick(parent: ViewGroup) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            // 不断的递归设置点击事件
            val view = parent.getChildAt(i)
            if (view is ViewGroup) {
                setChildViewOnclick(view)
                continue
            }
            view.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        if (v is TextView) {
            // 如果点击的是TextView
            val number = v.text.toString()
            if (!TextUtils.isEmpty(number)) {
                if (mListener != null) {
                    // 回调
                    mListener!!.onNumberClick(number)
                }
            }
        } else if (v is ImageView) {
            // 如果是图片那肯定点击的是删除
            if (mListener != null) {
                mListener!!.delete()
            }
        }
    }

    /**
     * 设置键盘的点击回调监听
     */
    fun setOnCustomerKeyboardClickListener(listener: CustomerKeyboardClickListener) {
        this.mListener = listener
    }

    /**
     * 点击键盘的回调监听
     */
    interface CustomerKeyboardClickListener {
        //输入完成回调
        fun onNumberClick(number: String)
        //删除回调
        fun delete()
    }
}