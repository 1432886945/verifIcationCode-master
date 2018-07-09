package com.hisan.verificationcode

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.support.annotation.StyleRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView



/**
 * 自定义验证码输入弹窗（带自定义键盘）
 * Created by imgod on 2017/7/11.
 */

class PayDialog @JvmOverloads constructor(context: Context, @StyleRes themeResId: Int = R.style.bottom_dialog_style) : Dialog(context, themeResId), View.OnClickListener, PasswordEditText.PasswordFullListener, CustomerKeyboard.CustomerKeyboardClickListener {
    private val root: View
    private var img_cancel: TextView? = null
    private var pet_top: PasswordEditText? = null
    private var code: TextView? = null
    private var code_phone: TextView? = null
    private var ckb_main: CustomerKeyboard? = null

    private var passwordListener: PasswordListener? = null

    init {
        root = LayoutInflater.from(context).inflate(R.layout.dialog_pay, null)
        setContentView(root)
        window!!.setGravity(Gravity.BOTTOM)
        initViews()
        initEvent()
    }

    private fun initEvent() {
        img_cancel!!.setOnClickListener(this)
        code!!.setOnClickListener(this)
        pet_top!!.setOnPasswordFullListener(this)
        ckb_main!!.setOnCustomerKeyboardClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        img_cancel = root.findViewById(R.id.cancel)
        pet_top = root.findViewById(R.id.pet_top)
        code = root.findViewById(R.id.code)
        code_phone = root.findViewById(R.id.code_phone)
        //code_phone!!.text = "验证码已发送至$SPUtils.getInstance().getString("username")"
        ckb_main = root.findViewById(R.id.ckb_main)
    }

    /**
     * 是否切换为密码模式
     */
    fun  setIsPass(isPass:Boolean){
        pet_top!!.setIsPass(isPass)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.cancel -> if (isShowing) {
                cancel()
            }
            R.id.code -> if (null != passwordListener) {
                passwordListener!!.forgetPwdClick()
            }
        }
    }

    override fun onNumberClick(number: String) {
        pet_top!!.addPassword(number)
    }

    override fun delete() {
        pet_top!!.deleteLastPassword()
    }

    override fun passwordFull(content: String) {
        if (null != passwordListener) {
            passwordListener!!.fullPwd(content)
        }
    }

    interface PasswordListener {
        fun forgetPwdClick()
        //输入框输入回调
        fun fullPwd(content: String)
    }

    fun setPasswordListener(passwordListener: PasswordListener) {
        this.passwordListener = passwordListener
    }
}
