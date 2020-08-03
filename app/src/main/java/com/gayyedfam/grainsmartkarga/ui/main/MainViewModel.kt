package com.gayyedfam.grainsmartkarga.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.domain.usecase.SetupDataUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by emgayyed on 17/7/20.
 */
class MainViewModel @ViewModelInject constructor(
    private val setupDataUseCase: SetupDataUseCase
) : ViewModel()  {

}