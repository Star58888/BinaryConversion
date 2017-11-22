package com.star.binaryconversion

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.textChangedListener

class MainActivity : AppCompatActivity() {

    enum class TYPE {
        Bin, Oct, Dec, Hex
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initEvent()
    }

    private fun initEvent() {
        autoSetTextChangeListener(etBin, etOct, etDec, etHex)
    }

    fun autoSetTextChangeListener(vararg arrayOfEditText: EditText) {
        var isRunning: Boolean = false

        arrayOfEditText.forEach { editText ->
            editText.textChangedListener {
                afterTextChanged {

                    str ->
                    if (!isRunning) {
                        isRunning = true
                        str.toString().changeNum(getEditType(editText))

                        isRunning = false
                    }
                }
            }
        }
    }

    private fun getEditType(editText: EditText): TYPE
        = when (editText.id) {
            R.id.etBin ->TYPE.Bin
            R.id.etOct ->TYPE.Oct
            R.id.etDec ->TYPE.Dec
            R.id.etHex ->TYPE.Hex


        else ->
        {
            TYPE .Bin
        }
    }

    fun String.changeNum(type: TYPE) {
        val string:String = this

        if (string.isEmpty()) {
            setTipIconAndText(R.drawable.ic_info ,"輸入數字，自動轉換其他進制")
            clearAllEditTextContent()
            return
        }

        when (type) {
            TYPE.Bin ->
            {
                if (checkValidate(string ,TYPE.Bin))
                {
                    string.setOtherEditTextContent(TYPE.Bin , etOct , etDec ,etHex)

                }
                else
                {
                    setWrongTipIconAndText("請輸入1或0")
                }
            }
            TYPE.Oct ->
            {
                if (checkValidate(string ,TYPE.Oct))
                {
                    string.setOtherEditTextContent(TYPE.Oct ,etBin ,etDec ,etHex)
                }
                else
                {
                    setWrongTipIconAndText("請輸入0 ~ 7")
                }
            }
            TYPE.Dec ->
            {
                if (checkValidate(string , TYPE.Dec))
                {
                    string.setOtherEditTextContent(TYPE.Dec , etBin ,etOct , etHex)
                }
                else
                {
                    setWrongTipIconAndText("請輸入0 ~ 9")
                }
            }
            TYPE.Hex ->
            {
                if (checkValidate(string , TYPE.Hex))
                {
                    string.setOtherEditTextContent(TYPE.Hex , etBin ,etOct ,etDec)
                }
                else
                {
                    setWrongTipIconAndText("請輸入數字及A ~ F")
                }
            }
            else ->
            {
                setWrongTipIconAndText("發生未知異常")
            }
        }
    }

     fun setWrongTipIconAndText(wrongText: String)
    {
        ivIcon.background = resources.getDrawable(R.drawable.ic_warn)
        tvTip.text = wrongText
    }

    fun setTipIconAndText(drawableId: Int, tipText: String)
    {
        ivIcon.background = resources.getDrawable(drawableId)
        tvTip.text = tipText
    }

     fun checkValidate(str: String, type: TYPE): Boolean
    {
        when(type)
        {
            TYPE.Bin ->
            {
                val list = listOf<Char>('0' ,'1')
                return considerInRange(str , list)
            }
            TYPE.Oct ->
            {
                val list = listOf<Char>('0' ,'1' ,'2' ,'3' ,'4' ,'5' ,'6' ,'7')
                return considerInRange(str ,list)
            }
            TYPE.Dec ->
            {
                val list = listOf<Char>('0' ,'1' ,'2' ,'3' ,'4' ,'5' ,'6' ,'7' ,'8' ,'9')
                return considerInRange(str, list)
            }
            TYPE.Hex ->
            {
                val list = listOf<Char>('0' ,'1' ,'2' ,'3' ,'4' ,'5' ,'6' ,'7' ,'8' ,'9' ,'A' ,'B' ,'C' ,'D' ,'E' ,'F')
                return considerInRange(str, list)
            }
            else -> return false
        }
    }

     fun considerInRange(str: String, list: List<Char>): Boolean
    {
        str.toCharArray().forEachIndexed { _,char ->
            if (char.toUpperCase() !in  list.toCharArray())
                return false
        }
        return true
    }

     fun clearAllEditTextContent() {
        etBin.setText("")
        etOct.setText("")
        etDec.setText("")
        etHex.setText("")
    }

     fun String.setOtherEditTextContent(type: TYPE, vararg arrayOfEditText: EditText)
    {
        val string: String = this

        val str: String = when (type)
        {
            TYPE.Bin -> Integer.parseInt(string ,2).toString()
            TYPE.Oct -> Integer.parseInt(string ,8).toString()
            TYPE.Dec -> Integer.parseInt(string ,10).toString()
            TYPE.Hex -> Integer.parseInt(string ,16).toString()
        }
        arrayOfEditText.forEach { editText ->
            when (editText.id)
            {
                R.id.etBin -> etBin.setText("" + Integer.toBinaryString(Integer.parseInt(str)))
                R.id.etOct -> etOct.setText("" + Integer.toOctalString(Integer.parseInt(str)))
                R.id.etDec -> etDec.setText("" + Integer.parseInt(str))
                R.id.etHex -> etHex.setText("" + Integer.toHexString(Integer.parseInt(str)).toUpperCase())
                else -> setWrongTipIconAndText("發生未知異常")
            }
        }
        setTipIconAndText(R.drawable.ic_success ,"轉換完成")
    }
}

fun Activity.toast(text: String)
{
    Toast.makeText(this , text , Toast.LENGTH_LONG).show()
}