package com.alexandre.marcq.spotiguessr.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun Fragment.goBack() = findNavController().popBackStack()