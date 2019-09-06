package com.tatvasoft.engineeringaiapp.util

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.tatvasoft.engineeringaiapp.BuildConfig

@GlideModule
class AppGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled() = false

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(
                context,
                builder.setLogLevel(if (BuildConfig.DEBUG) Log.DEBUG else Log.ERROR).setDefaultRequestOptions(
                        RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                )
        )
    }

}