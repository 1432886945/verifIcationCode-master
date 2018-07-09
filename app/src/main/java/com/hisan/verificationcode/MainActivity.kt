package com.hisan.verificationcode

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.king.thread.nevercrash.NeverCrash

class MainActivity : AppCompatActivity() {
    lateinit var payDialog: PayDialog
    var show:TextView?=null
    var show1:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NeverCrash.init { _, e -> Log.v("xyy","系统异常$e.message") }
        show=findViewById(R.id.show)
        show1=findViewById(R.id.show1)
        payDialog = PayDialog(this)
        payDialog.setPasswordListener(object : PayDialog.PasswordListener {
            //自定义按钮点击事件
            override fun forgetPwdClick() {
                Toast.makeText(this@MainActivity,"点我干嘛!!,搞事情",Toast.LENGTH_LONG).show()
            }
            //输入完成事件
            override fun fullPwd(content: String) {
                Toast.makeText(this@MainActivity,"输入完成",Toast.LENGTH_LONG).show()
                payDialog.dismiss()
            }
        })
        show!!.setOnClickListener({
            if (payDialog.isShowing) payDialog.dismiss()
            //显示纯数字
            payDialog.setIsPass(false)
            payDialog.show()
        })
        show1!!.setOnClickListener({
            if (payDialog.isShowing) payDialog.dismiss()
            //显示加密数据
            payDialog.setIsPass(true)
            payDialog.show()
        })
    }
}
