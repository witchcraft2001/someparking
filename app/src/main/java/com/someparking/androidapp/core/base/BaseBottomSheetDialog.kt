package com.someparking.androidapp.core.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Px
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.someparking.androidapp.domain.Message

abstract class BaseBottomSheetDialog : DaggerBottomSheetDialogFragment() {

    private companion object {
        // Если поставить 0.0f перестает работать canceledOnTouchOutside
        const val INVISIBLE_DIM_AMOUNT = 0.01f

        // Взято из backgroundDimAmount
        const val DEFAULT_DIM_AMOUNT = 0.6f
        const val UNKNOWN_OFFSET = -1.0f
        const val LAST_STATE_KEY = "LAST_STATE_KEY"
    }

    @BottomSheetBehavior.State
    private var lastState: Int = BottomSheetBehavior.STATE_EXPANDED
    private var initialOffset: Float = UNKNOWN_OFFSET
    private var currentOffset: Float = UNKNOWN_OFFSET
    private var onBackPressedListener: (() -> Unit)? = null
    private val callback: BottomSheetBehavior.BottomSheetCallback by lazy { initCallback() }
    private val settings: PanelSettings by lazy { getPanelSettings() }

    protected abstract val layoutIdRes: Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
//            override fun dismiss() {
////                hideKeyboard()
//                super.dismiss()
//            }

            override fun setCancelable(cancelable: Boolean) {
                super.setCancelable(cancelable)
                // После super восстанавливаем значение hideable, т.к. сами регулируем событием STATE_HIDDEN
                behavior.isHideable = true
            }

            override fun cancel() {
                if (onDecisionToCancel()) {
                    super.cancel()
                }
            }

            override fun onBackPressed() {
                onBackPressedListener?.invoke() ?: super.onBackPressed()
            }
        }.also { dialog ->
            initPanelSettings(dialog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutIdRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastState = savedInstanceState?.getInt(LAST_STATE_KEY) ?: getDefaultState()
        (dialog as? BottomSheetDialog)?.run {
            // Меняем высоту bottomSheet на всю длину экрана
            val bottomSheet = findViewById<ViewGroup>(R.id.design_bottom_sheet)
            if (bottomSheet != null && settings.isFullHeight) {
                val coordinatorParams = CoordinatorLayout.LayoutParams(bottomSheet.layoutParams)
                coordinatorParams.height = ViewGroup.LayoutParams.MATCH_PARENT

                val fullHeightTopOffset = settings.fullHeightTopOffset
                if (fullHeightTopOffset > 0) coordinatorParams.topMargin = fullHeightTopOffset

                bottomSheet.layoutParams = coordinatorParams
            }
            // Специально поставили setBottomSheetCallback для удаления внутреннего bottomSheetCallback
            behavior.setBottomSheetCallback(callback)
            // Отправляем сигнал вручную, т.к. setState не отправляет сигнал к callback если они одинаковые
            // Должен быть до setState чтобы сравнить текущий и новый
            if (behavior.state == lastState) {
                setLastState(lastState)
                onStateChanged(lastState)
            }
            behavior.state = lastState
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (dialog as? BottomSheetDialog)?.behavior?.removeBottomSheetCallback(callback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(LAST_STATE_KEY, lastState)
    }

    private fun initPanelSettings(dialog: BottomSheetDialog) {
        dialog.behavior.run {
            isDraggable = settings.isDraggable
            skipCollapsed = settings.statesOption.collapsedOption.isAvailable.not()
            isFitToContents = settings.statesOption.halfExpandedOption.isAvailable.not()
        }
        settings.cancelOption.run {
            this@BaseBottomSheetDialog.isCancelable = isCancelable
            dialog.setCanceledOnTouchOutside(isCancelOnTouchOutside)
        }
        dialog.setOnShowListener {
            onShowListener(dialog)
        }
    }

    @BottomSheetBehavior.State
    private fun getDefaultState(): Int {
        return settings.statesOption.run {
            when {
                collapsedOption.isAvailable && collapsedOption.isDefault -> {
                    BottomSheetBehavior.STATE_COLLAPSED
                }
                halfExpandedOption.isAvailable && halfExpandedOption.isDefault -> {
                    BottomSheetBehavior.STATE_HALF_EXPANDED
                }
                expandedOption.isAvailable && expandedOption.isDefault -> {
                    BottomSheetBehavior.STATE_EXPANDED
                }
                else -> {
                    BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    private fun initCallback() = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    if (settings.statesOption.expandedOption.isAvailable) {
                        setLastState(newState)
                    } else {
                        changeToState(
                            topState = lastState,
                            bottomState = BottomSheetBehavior.STATE_HALF_EXPANDED
                        )
                    }
                }
                BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    if (settings.statesOption.halfExpandedOption.isAvailable) {
                        setLastState(newState)
                    } else {
                        changeToState(
                            topState = BottomSheetBehavior.STATE_EXPANDED,
                            bottomState = BottomSheetBehavior.STATE_HIDDEN
                        )
                    }
                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    if (settings.statesOption.collapsedOption.isAvailable) {
                        setLastState(newState)
                    } else {
                        changeToState(
                            topState = BottomSheetBehavior.STATE_HALF_EXPANDED,
                            bottomState = BottomSheetBehavior.STATE_HIDDEN
                        )
                    }
                }
                BottomSheetBehavior.STATE_HIDDEN -> {
                    initialOffset = UNKNOWN_OFFSET
                    if (settings.cancelOption.isCancelOnSwipeDown) {
                        (dialog as? BottomSheetDialog)?.cancel()
                    } else {
                        // Возвращаем предыдущий state, не даем становиться STATE_HIDDEN
                        (dialog as? BottomSheetDialog)?.behavior?.state = lastState
                    }
                }
                else -> Unit
            }
            onStateChanged(newState)
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            if (initialOffset == UNKNOWN_OFFSET) {
                initialOffset = slideOffset
            }
            currentOffset = slideOffset
        }
    }

    private fun setLastState(@BottomSheetBehavior.State newState: Int) {
        setScrimVisible(newState)
        initialOffset = UNKNOWN_OFFSET
        lastState = newState
    }

    private fun changeToState(@BottomSheetBehavior.State topState: Int, @BottomSheetBehavior.State bottomState: Int) {
        val isFromTop = (initialOffset - currentOffset) > 0
        (dialog as? BottomSheetDialog)?.behavior?.state = if (isFromTop) {
            bottomState
        } else {
            topState
        }
    }

    private fun setScrimVisible(@BottomSheetBehavior.State newState: Int) {
        val isScrimVisible = settings.statesOption.run {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> collapsedOption.isScrimVisible
                BottomSheetBehavior.STATE_HALF_EXPANDED -> halfExpandedOption.isScrimVisible
                BottomSheetBehavior.STATE_EXPANDED -> expandedOption.isScrimVisible
                else -> false
            }
        }
        if (isScrimVisible) {
            dialog?.window?.setDimAmount(DEFAULT_DIM_AMOUNT)
        } else {
            dialog?.window?.setDimAmount(INVISIBLE_DIM_AMOUNT)
        }
    }

    fun updateTextEditIfNeed(view: TextView, text: String?) {
        if (view.text.toString() != text ?: "") {
            view.text = text
        }
    }

    fun showMessage(message: Message) {
        (requireActivity() as? BaseActivity)?.showMessage(message)
    }

    /**
     * Метод для получения настроек панели
     */
    protected open fun getPanelSettings(): PanelSettings = PanelSettings()

    /**
     * Метод для получения сигнала об изменении состояния панели
     */
    protected open fun onStateChanged(newState: Int) = Unit

    /**
     * Метод срабатывает при вызове OnShowListener.onShow
     * Рекомендуется использовать при вычислении размера view или работе с inset-ми
     */
    protected open fun onShowListener(dialog: BottomSheetDialog) = Unit

    /**
     * Метод для получения сигнала cancel и возможностью предотвратить вызов super.cancel()
     */
    protected open fun onDecisionToCancel(): Boolean = true

    /**
     * Устанавливает листенер, срабатывающий при тапе на кнопку "назад"
     * Если не переопределять, то диалог закрывается по дефолту
     */
    protected fun setOnBackPressedListener(listener: () -> Unit) {
        onBackPressedListener = listener
    }

    open class PanelSettings(
        open val statesOption: StatesOption = StatesOption(),
        open val isDraggable: Boolean = true,
        open val isFullHeight: Boolean = false,
        open val cancelOption: CancelOption = CancelOption(),
        @Px open val fullHeightTopOffset: Int = 0
    ) {

        open class StatesOption(
            open val collapsedOption: StateOption = StateOption(),
            open val halfExpandedOption: StateOption = StateOption(),
            open val expandedOption: StateOption = StateOption(
                isAvailable = true,
                isDefault = true,
                isScrimVisible = true
            ),
        )

        open class StateOption(
            open val isAvailable: Boolean = false,
            open val isDefault: Boolean = false,
            open val isScrimVisible: Boolean = false
        )

        open class CancelOption(
            open val isCancelOnSwipeDown: Boolean = true,
            open val isCancelOnTouchOutside: Boolean = true,
            open val isCancelable: Boolean = true,
        )
    }
}
