package com.gayyedfam.grainsmartkarga.ui.main

import androidx.lifecycle.ViewModel
import com.gayyedfam.grainsmartkarga.domain.usecase.SetupDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by emgayyed on 17/7/20.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val setupDataUseCase: SetupDataUseCase
) : ViewModel()  {

}