package com.gayyedfam.grainsmartkarga.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.domain.usecase.SetupDataUseCase

/**
 * Created by emgayyed on 17/7/20.
 */
class MainViewModel @ViewModelInject constructor(
    private val setupDataUseCase: SetupDataUseCase
) : ViewModel()  {

    fun load() {
        setupDataUseCase()
    }
}