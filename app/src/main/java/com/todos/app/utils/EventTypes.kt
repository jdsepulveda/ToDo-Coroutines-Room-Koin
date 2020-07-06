package com.todos.app.utils

import android.view.View
import androidx.navigation.NavDirections

sealed class EventTypes {

    //class ShowError(val error: Throwable, val errorMsg: String? = null) : EventTypes()
    class ShowMsg(val msg: String) : EventTypes()
    class ShowMsgContextString(val msg: ContextString) : EventTypes()
    class ShowError(val errorMsg: String) : EventTypes()
    class ShowErrorContextString(val errorMsg: ContextString) : EventTypes()
    object CloseDialog : EventTypes()
    class CloseKeyboard(val view: View) : EventTypes()
    data class Navigate(val directions: NavDirections) : EventTypes()
}