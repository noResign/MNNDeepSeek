package com.example.mnndeepseek.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

open class FragmentViewCallback : PageViewCallback, LifecycleOwner {
    protected var fragment: Fragment

    constructor(fragment: Fragment): this(fragment, false)

    constructor(fragment: Fragment, enableViewLifecycle: Boolean) {
        this.fragment = fragment
        fragment.lifecycle.addObserver(lifecycleObserver)
        if (enableViewLifecycle) {
            fragment.viewLifecycleOwnerLiveData.observe(fragment) { lifecycleOwner ->
                lifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_CREATE) {
                            onCreateView()
                        } else if (event == Lifecycle.Event.ON_DESTROY) {
                            onDestroyView()
                        }
                    }

                })
            }
        }
    }

    protected fun onCreateView() {}

    protected fun onDestroyView() {}

    override fun onDetach() {
        super.onDetach()
        fragment.lifecycle.removeObserver(lifecycleObserver)
    }

    protected fun requireActivity(): FragmentActivity = fragment.requireActivity()

    protected fun getActivity(): FragmentActivity? = fragment.activity

    protected fun getContext(): Context? = fragment.context

    protected fun requireContext(): Context = fragment.requireContext()

    protected fun getViewLifecycleOwner() = fragment.viewLifecycleOwner


    override val lifecycle: Lifecycle
        get() = fragment.lifecycle
}