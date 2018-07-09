＃自定义弹窗验证码+自定义输入键盘

//第三方依赖
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	
  	dependencies {
	        implementation 'com.github.1432886945:verifIcationCode:V1.0.0'
	}
//使用方法
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
	
![image](https://github.com/1432886945/verifIcationCode-master/blob/master/1530770475298.gif)
