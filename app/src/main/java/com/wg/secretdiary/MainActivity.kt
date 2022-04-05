package com.wg.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var firstNumberPicker: NumberPicker
    private lateinit var secondNumberPicker: NumberPicker
    private lateinit var thirdNumberPicker: NumberPicker
    private lateinit var openButton: AppCompatButton
    private lateinit var changePasswordButton: AppCompatButton

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initialized()
        numberPickerInit()

        openButton.setOnClickListener {

            if(changePasswordMode){
                Toast.makeText(this, "현재 비밀번호 변경중입니다.", Toast.LENGTH_LONG).show()
            }
            //다른앱과 공유하지않고 우리앱에서만 사용하도록 하기위해 MODE_PRIVATE을 사용
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${firstNumberPicker.value}" +
                    "${secondNumberPicker.value}" +
                    "${thirdNumberPicker.value}"

            if(passwordPreferences.getString("password", "000").equals(passwordFromUser)){
                startActivity(Intent(this, DiaryActivity::class.java))
            }else{
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener {

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser = "${firstNumberPicker.value}" +
                    "${secondNumberPicker.value}" +
                    "${thirdNumberPicker.value}"

            if(changePasswordMode){
                // 번호를 저장하는 기능
                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                }

                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)

            }else{
                // changePasswordMode 가 활성화
                if(passwordPreferences.getString("password", "000").equals(passwordFromUser)){
                    changePasswordMode = true

                    Toast.makeText(this, "변경할 패스워드를 입력해주세요", Toast.LENGTH_LONG).show()
                    changePasswordButton.setBackgroundColor(Color.RED)

                }else{
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun initialized() {
        firstNumberPicker = findViewById(R.id.np_first_password)
        secondNumberPicker = findViewById(R.id.np_second_password)
        thirdNumberPicker = findViewById(R.id.np_third_password)
        openButton = findViewById(R.id.btn_open)
        changePasswordButton = findViewById(R.id.btn_change_password)
    }

    private fun numberPickerInit() {
        firstNumberPicker.apply {
            minValue = 0
            maxValue = 9
        }

        secondNumberPicker.apply {
            minValue = 0
            maxValue = 9
        }

        thirdNumberPicker.apply {
            minValue = 0
            maxValue = 9
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { dialog, which -> }
            .create()
            .show()
    }
}