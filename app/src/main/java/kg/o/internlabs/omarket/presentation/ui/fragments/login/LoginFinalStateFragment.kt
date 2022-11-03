package kg.o.internlabs.omarket.presentation.ui.fragments.login

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import kg.o.internlabs.core.base.BaseFragment
import kg.o.internlabs.core.custom_views.OtpInputCustomView
import kg.o.internlabs.core.custom_views.OtpResend
import kg.o.internlabs.omarket.databinding.FragmentLoginFinalStateBinding

@AndroidEntryPoint
class LoginFinalStateFragment : BaseFragment<FragmentLoginFinalStateBinding, LoginViewModel>(), OtpResend {
    override val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentLoginFinalStateBinding {
         return FragmentLoginFinalStateBinding.inflate(inflater)
    }

    override fun initListener() {
        super.initListener()

        //binding.otp.setOtp("3450")
        //val otp = OtpInputCustomView(requireContext(), )
        binding.btnGet.setOnClickListener {
            println( binding.otp.getValues())
            binding.otp.setError("Niene")
        }
//        val navHostFragment = requireActivity().supportFragmentManager
//            .findFragmentById(R.id.nav_host) as NavHostFragment
//        val navController = navHostFragment.navController
//        navController.navigate(R.id.mainFragment)
    }

    override fun sendOtpAgain() {
        println("jhkfhgbgkj")
    }
}