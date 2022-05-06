package com.someparking.androidapp.core.base

import android.content.Context
import android.widget.TextView
import com.someparking.androidapp.core.interfaces.RequestProgress
import com.someparking.androidapp.domain.Message
import com.someparking.androidapp.navigation.ChainHolder
import dagger.android.support.DaggerFragment

open class BaseFragment: DaggerFragment() {

    private fun findChainHolder() : ChainHolder? {
        if (parentFragment is ChainHolder) {
            return parentFragment as ChainHolder
        } else if (parentFragment is BaseFragment) {
               return (parentFragment as BaseFragment).findChainHolder()
        }
        return null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        findChainHolder()?.addToChain(this)
    }

    override fun onDetach() {
        super.onDetach()
        findChainHolder()?.removeFromChain(this)
    }

    fun showMessage(message: Message) {
        (requireActivity() as? BaseActivity)?.showMessage(message)
    }

    fun setProgressVisibility(isVisible: Boolean) {
        (requireActivity() as RequestProgress?)?.setProgressVisibility(isVisible)
    }

    fun updateTextEditIfNeed(view: TextView, text: String?) {
        if (view.text.toString() != text ?: "") {
            view.text = text
        }
    }
}