package com.todos.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.todos.R
import com.todos.app.utils.EventObserver
import com.todos.app.utils.EventTypes
import com.todos.app.utils.extensions.hideKeyboard
import com.todos.app.viewmodels.DialogFragmentTaskVM
import com.todos.databinding.FragmentDialogTaskBinding
import kotlinx.android.synthetic.main.fragment_dialog_task.*
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentDialogTask : DialogFragment() {

    private val dialogFragmentTaskVM: DialogFragmentTaskVM by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return DataBindingUtil.inflate<ViewDataBinding>(
            layoutInflater, R.layout.fragment_dialog_task, container, false
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NavigationUI.setupWithNavController(task_toolbar, findNavController())
        DataBindingUtil.findBinding<FragmentDialogTaskBinding>(view)?.apply {
            viewModel = dialogFragmentTaskVM
            lifecycleOwner = viewLifecycleOwner
        }

        setUpObservers()
    }

    private fun setUpObservers() {
        // TODO: Create a way similar to: override fun processEffect(event: ViewEffect) { when (event) { ... } }
        dialogFragmentTaskVM.event.observe(viewLifecycleOwner, EventObserver { eventTypes ->
            when(eventTypes) {
                is EventTypes.ShowMsgContextString -> {
                    this.view?.let {
                        Snackbar.make(it, eventTypes.msg.format(this.requireContext()), Snackbar.LENGTH_LONG).show()
                    }
                }
                is EventTypes.ShowErrorContextString -> {
                    this.view?.let {
                        Snackbar.make(it, eventTypes.errorMsg.format(this.requireContext()), Snackbar.LENGTH_LONG).show()
                    }
                }
                is EventTypes.CloseKeyboard -> {
                    eventTypes.view.hideKeyboard()
                }
                is EventTypes.Navigate -> {
                    val direction = eventTypes.directions ?: return@EventObserver
                    this.findNavController().navigate(direction)
                }
            }
        })
    }
}