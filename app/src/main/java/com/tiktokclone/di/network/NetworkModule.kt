package com.tiktokclone.di.network

import android.content.Context
import android.content.SharedPreferences
 import com.tiktokclone.BuildConfig
import com.tiktokclone.data.datasource.BASE_URL
import com.tiktokclone.data.datasource.SoundApi
import com.tiktokclone.data.datasource.TikTokApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun getColleaguesApiInterface(retrofit: Retrofit): TikTokApi =
        retrofit.create(TikTokApi::class.java)




    @Provides
    @Singleton
    fun getOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        //    headersInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        //    .addInterceptor(headersInterceptor)
        .build()

    @Provides
    @Singleton
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }


    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    @Provides
    @Singleton
    fun getSoundApi(): SoundApi = Retrofit.Builder()
        .baseUrl("https://api.deezer.com/chart/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
         .build().create(SoundApi::class.java)


    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)
    }
}
