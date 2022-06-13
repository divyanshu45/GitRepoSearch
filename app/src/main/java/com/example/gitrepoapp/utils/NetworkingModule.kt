package com.example.gitrepoapp.utils

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.requestly.rqinterceptor.api.RQCollector
import io.requestly.rqinterceptor.api.RQInterceptor
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val collector = RQCollector(
            context = appContext,
            sdkKey = "8Krw0Jypwovy79fl48VC"
        )
        val rqInterceptor = RQInterceptor.Builder(appContext)
            .collector(collector)
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(rqInterceptor)
            .build()
    }
}