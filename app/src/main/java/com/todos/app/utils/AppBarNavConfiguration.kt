package com.todos.app.utils

import androidx.navigation.ui.AppBarConfiguration
import com.todos.R

private val topLevelIdsDestination: Set<Int> = setOf(
    R.id.fragmentTasksList,
    R.id.fragmentTasksDone
)
val appBarNavConfiguration = AppBarConfiguration(topLevelIdsDestination)