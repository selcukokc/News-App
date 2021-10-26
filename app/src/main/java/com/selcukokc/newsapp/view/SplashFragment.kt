package com.selcukokc.newsapp.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.selcukokc.newsapp.R


class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var timer : CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        timer = object: CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToFeedFragment())
            }
        }

        timer.start()

    }




}