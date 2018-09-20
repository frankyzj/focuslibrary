package com.prient.focuslibrary.network.okhttputils;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者：prient
 * 时间：2018/9/20 16:28
 * 功能介绍：
 * 修改时间：
 * 修改说明：
 */
public class OkHttpUtil {
    private static OkHttpUtil okHttpUtil;
    private OkHttpClient okHttpClient;
    private Handler handler;

    public static OkHttpUtil getInstance(Context context){
        if (okHttpUtil == null){
            synchronized (OkHttpUtil.class){
                if (okHttpUtil == null){
                    okHttpUtil = new OkHttpUtil(context);
                }
            }
        }
        return okHttpUtil;
    }

    public OkHttpUtil(Context context){
        handler = new Handler();
        //缓存路径
        File file = context.getExternalCacheDir();
        //缓存大小
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = null;
        if(file != null){
            cache = new Cache(file, cacheSize);
        }
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.MILLISECONDS)
                .readTimeout(50, TimeUnit.MILLISECONDS)
                .writeTimeout(50, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .cache(cache)
                .build();
    }

    /**
     * 异步 get 请求
     * @param url url
     * @param callback 结果回调
     */
    public void getAsynHttp(String url, ResultCallback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        asyncRequest(request, callback);
    }

    /**
     * 异步 post 请求
     * @param url url
     * @param requestMap 请求参数
     * @param callback 回调接口
     */
    public void postAsynHttp(String url, Map<String, String> requestMap, ResultCallback callback){
        FormBody.Builder builder = new FormBody.Builder();
        if(requestMap != null && requestMap.size() > 0){
            for(Map.Entry<String, String> entry : requestMap.entrySet()){
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        asyncRequest(request, callback);
    }

    private void asyncRequest(Request request, final ResultCallback callback) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                if(callback != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response){
                if (callback != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(response);
                        }
                    });
                }
            }
        });
    }
}
