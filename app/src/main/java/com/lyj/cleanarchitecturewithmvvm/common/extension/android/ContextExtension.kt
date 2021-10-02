package com.lyj.cleanarchitecturewithmvvm.common.extension.android

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes


fun Context.longToast(
    @StringRes res : Int
) = Toast.makeText(this,res, Toast.LENGTH_LONG).show()


fun Context.longToast(
    str: String
) = Toast.makeText(this,str,Toast.LENGTH_LONG).show()


fun Context.shortToast(
    @StringRes res : Int
) = Toast.makeText(this,res,Toast.LENGTH_SHORT).show()


fun Context.shortToast(
    str : String
) = Toast.makeText(this,str,Toast.LENGTH_SHORT).show()