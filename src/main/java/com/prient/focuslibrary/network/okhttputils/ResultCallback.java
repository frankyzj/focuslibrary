package com.prient.focuslibrary.network.okhttputils;

import okhttp3.Response;

/**
 * 作者：prient
 * 时间：2018/9/20 17:05
 * 功能介绍：
 * 修改时间：
 * 修改说明：
 */
public interface ResultCallback {
    void onError(Exception e);
    void onResponse(Response response);
}
