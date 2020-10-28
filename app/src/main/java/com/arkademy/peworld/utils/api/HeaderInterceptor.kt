package com.arkademy.peworld.utils.api

import android.content.Context
import com.arkademy.peworld.utils.sharedpreference.Constants
import com.arkademy.peworld.utils.sharedpreference.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(val mContext: Context) : Interceptor {
    private lateinit var sharedPref: PreferenceHelper
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        sharedPref = PreferenceHelper(mContext)

        val token = sharedPref.getString(Constants.KEY_TOKEN)
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        )
    }
}