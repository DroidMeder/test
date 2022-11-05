package kg.o.internlabs.core.custom_views

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import kg.o.internlabs.core.R
import kg.o.internlabs.core.databinding.CustomOtpInputViewBinding

class CustomOtpInputView : ConstraintLayout {
    private var otpResend: OtpResend? = null
    private var timer = 0L
    private var hasFirstValue = false
    private var hasSecondValue = false
    private var hasThirdValue = false
    private var hasFourthValue = false

    private val binding = CustomOtpInputViewBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        context.obtainStyledAttributes(attrs, R.styleable.CustomOtpInputView).run {
            getText(R.styleable.CustomOtpInputView_set_otp)?.let {
                setOtp(it.toString())
            }
            getInteger(R.styleable.CustomOtpInputView_set_timer, 10_000).run {
                timer = this.toLong()
                startTimer()
            }
            initWatcher()
            initClickers()
            recycle()
        }
    }

    fun setTimer(value: Long) {
        timer = value
        startTimer()
    }

    private fun startTimer() = with(binding) {
        tvResentButton.isVisible = false
        tvTimer.isVisible = true
        object : CountDownTimer(timer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val totalTimer = millisUntilFinished / 1000
                val hours: Int = (totalTimer / 3_600).toInt()
                val time: String = if (hours > 0) {
                    String.format(
                        "%02d:%02d:%02d", hours,
                        ((totalTimer % 3_600) / 60).toInt(), (totalTimer % 60).toInt()
                    )
                } else {
                    String.format(
                        "%02d:%02d", ((totalTimer % 3_600) / 60).toInt(),
                        (totalTimer % 60).toInt()
                    )
                }
                tvTimer.text = time
            }

            override fun onFinish() {
                tvTimer.isVisible = false
                tvResentButton.isVisible = true
            }
        }.start()
    }!!

    private fun initClickers() = with(binding) {
        clicked(etOtp1)
        clicked(etOtp2)
        clicked(etOtp3)
        clicked(etOtp4)

        tvResentButton.setOnClickListener {
            otpResend?.sendOtpAgain()
            startTimer()
        }
    }

    private fun clicked(et: EditText) {
        et.setOnClickListener {
            with(it) {
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
            }
        }
    }

    fun setInterface(otpResend: OtpResend) {
        this.otpResend = otpResend
    }

    private fun initWatcher() = with(binding) {
        watch(etOtp1, etOtp2, 1)
        watch(etOtp2, etOtp3, 2)
        watch(etOtp3, etOtp4, 3)
        watch(etOtp4)
    }

    private fun watch(et1: EditText, et2: EditText, cellsPosition: Int) {
        et1.addTextChangedListener {
            et1.isFocusable = it.toString().isEmpty()
            with(et2) {
                isFocusable = !et1.isFocusable
                requestFocus()
                when (cellsPosition) {
                    1 -> hasFirstValue = isFocusable
                    2 -> hasSecondValue = isFocusable
                    3 -> hasThirdValue = isFocusable
                }
                watcher()
            }
        }
    }

    private fun watch(et: EditText) {
        et.addTextChangedListener {
            et.isFocusable = it.toString().isEmpty()
            hasFourthValue = !et.isFocusable
            watcher()
        }
    }

    private fun watcher() {
        otpResend?.watcher(hasFirstValue && hasSecondValue && hasThirdValue && hasFourthValue)
    }

    fun setOtp(values: String) = with(binding) {
        if (values.length > 4 || values.length < 4) return
        etOtp1.setText(values[0].toString())
        etOtp2.setText(values[1].toString())
        etOtp3.setText(values[2].toString())
        etOtp4.setText(values[3].toString())
    }

    fun setError(error: String) = with(binding) {
        with(tvResponseMessage) {
            text = error
            isVisible = true
        }
        setErrorBackground()
    }

    private fun setErrorBackground() = with(binding) {
        changeBackgroundColor(fl1)
        changeBackgroundColor(fl2)
        changeBackgroundColor(fl3)
        changeBackgroundColor(fl4)
    }

    private fun changeBackgroundColor(fl: FrameLayout) {
        fl.background = ResourcesCompat.getDrawable(
            resources,
            R.drawable.bg_custom_view_error_sms, null
        )
    }

    fun getValues() = with(binding) {
        "${etOtp1.text}${etOtp2.text}" +
                "${etOtp3.text}${etOtp4.text}"
    }
}
