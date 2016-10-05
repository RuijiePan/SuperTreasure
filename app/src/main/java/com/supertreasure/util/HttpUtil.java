package com.supertreasure.util;

import android.os.Handler;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Http访问工具类
 * Created by Neo on 2015/10/28 0028.
 */
public class HttpUtil {
    public static int timeOut = 10;                //超时时间，单位秒
    public static int connectTimeOut = 10;        //连接超时时间，单位秒
    public static int progressInterval = 1;         //下载时上报进度周期，单位秒
    public static boolean showLog = false;       //是否打印日志

    private static Handler handler = new Handler();     //用于维护自身的线程同步
    private static OkHttpClient client = initClient();           //维护唯一的客户端，用于节约内存

    public static Long currSize = 0L;
    public static Long fileSize = 0L;
    /**
     * 初始化OkHttpClient
     *
     * @return 初始化过的OkHttpClient
     */
    private static OkHttpClient initClient() {
        OkHttpClient initClient = new OkHttpClient();
        /*initClient.setWriteTimeout(timeOut, TimeUnit.SECONDS);
        initClient.setReadTimeout(timeOut, TimeUnit.SECONDS);*/
        initClient.setConnectTimeout(connectTimeOut, TimeUnit.SECONDS);
        initClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        return initClient;
    }

    /**
     * Http获取数据通过Post方式
     *
     * @param mRequest Http请求
     * @param callBack 访问网络回调函数
     * @return 用于控制可以中途中断的Call对象
     */
    public static Call httpPostGetData(HttpRequest mRequest, final CallBack callBack) {
        Request.Builder builder = new Request.Builder().url(mRequest.url);
        if (mRequest.params != null) {
            builder = builder.post(mRequest.params.build());
        }
        //拼接请求字符串
        printRequestUrl(mRequest);
        callBack.onStart();
        Call call = client.newCall(builder.build());

        call.enqueue(new Callback() {
            //okhttp异步回调函数是在子线程中的，需要和主线程进行同步
            @Override
            public void onFailure(Request request, IOException e) {
                //超时，连接失败
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onConnctError();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                Log.w("haha1",currSize+"!!"+fileSize);
                //获取到相应
                if (response.isSuccessful()) {
                    final String strBody = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(strBody);
                        }
                    });
                } else {
                    //服务器返回错误
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            callBack.onFailure(response);
                        }
                    });
                }
            }
        });
        return call;
    }

    public static Call uploadFiles(final HttpUploadRequest request, final CallBack callBack) {
        Request.Builder builder = new Request.Builder().url(request.url);
        RequestBody body = request.requestBody.build();
        printRequestUrl(request);

        callBack.onStart();
        final Call call = client.newCall(builder.post(body).build());
        call.enqueue(new Callback() {

            //okhttp异步回调函数是在子线程中的，需要和主线程进行同步
            @Override
            public void onFailure(Request request, IOException e) {
                //超时，连接失败
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onConnctError();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {

                //获取到相应
                if (response.isSuccessful()) {
                    final String strBody = response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(strBody);
                        }
                    });
                } else {
                    //服务器返回错误
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            callBack.onFailure(response);
                        }
                    });
                }
            }


        });
        return call;
    }

    /**
     * 异步下载文件
     *
     * @param url         要下载文件的URL
     * @param destFileDir 本地文件存储的文件夹
     * @param callBack    返回回调事件
     */
    public static void downloadAsyn(final String url, final String destFileDir, final CallBack callBack) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        callBack.onStart();
        call.enqueue(new Callback() {

            @Override
            public void onFailure(final Request request, final IOException e) {
                //超时，连接失败
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onConnctError();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;

                FileOutputStream fos = null;
                Headers headers = response.headers();
                String strSize = headers.get("Content-Length");
                long fileSize = -1, currSize = 0;
                try {
                    //尝试转换文件大小
                    fileSize = Long.parseLong(strSize);
                } catch (NumberFormatException ex) {
                    //转换失败
                }
                try {
                    is = response.body().byteStream();
                    FileUtil.makeRootDirectory(destFileDir);
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        //更新下载进度
                        currSize += len;
                        callBack.onProgress(currSize, fileSize);
                    }
                    //下载完毕
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(destFileDir + "/" + getFileName(url));
                        }
                    });
                } catch (IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure(response);
                        }
                    });
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }

    /**
     * 获取文件的名字
     *
     * @param path 文件路径
     * @return 文件名字带后缀
     */
    private static String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 输出请求Url
     */
    private static void printRequestUrl(HttpRequest request) {
        if (showLog) {
            Log.e("HttpUtil", request.toString());
        }
    }

    /**
     * 输出请求Url
     */
    private static void printRequestUrl(HttpUploadRequest request) {
        if (showLog) {
            Log.e("HttpUtil Upload Files", request.toString());
        }
    }


    /**
     * Http类回调函数
     */
    public abstract static class CallBack {
        /**
         * 连接服务器失败回调函数
         */
        public void onConnctError() {
            //可以设置统一的连接失败回调
        }


        /**
         * 网络连接开始回调函数
         */
        public void onStart() {

        }

        /**
         * 下载进度回调函数，注意该回调在独立线程中，需做线程处理
         */
        public void onProgress(long curSize, long fileSize) {

        }

        /**
         * 获取响应成功回调
         *
         * @param body 返回响应体
         */
        public abstract void onSuccess(String body);

        /**
         * 获取响应失败
         *
         * @param response 响应体
         */
        public abstract void onFailure(Response response);
    }

    /**
     * Http请求类
     */
    public static class HttpRequest {
        private String url;
        private StringBuilder paramsString;
        private FormEncodingBuilder params;

        public HttpRequest(String strUrl) {
            url = strUrl;
        }

        /**
         * 增加请求的参数
         *
         * @param key   参数名
         * @param value 参数值
         */
        public void addParam(String key, String value) {
            if (params == null) {
                params = new FormEncodingBuilder();
                paramsString = new StringBuilder("?");
                paramsString.append(key + "=" + value);
            } else {
                paramsString.append("&" + key + "=" + value);
            }
            params.add(key, value);
        }

        /**
         * 获得拼接的Url
         *
         * @return 拼接的Url，带有请求参数
         */
        @Override
        public String toString() {
            return paramsString == null ? url : url + paramsString.toString();
        }
    }

    /**
     * Http请求类
     */
    public static class HttpUploadRequest {
        private static final MediaType MEDIA_TYPE = MediaType.parse("image/*");
        private String url;
        private StringBuilder paramsString;
        private MultipartBuilder requestBody = null;

        public HttpUploadRequest(String strUrl) {
            url = strUrl;
        }

        /**
         * 增加请求的参数
         *
         * @param key   参数名
         * @param value 参数值
         */
        public void addHeaderParam(String key, String value) {
            if (requestBody == null) {
                requestBody = new MultipartBuilder().type(MultipartBuilder.FORM);
                paramsString = new StringBuilder("?");
                paramsString.append(key + "=" + value);
            } else {
                paramsString.append("&" + key + "=" + value);
            }
            requestBody.addFormDataPart(key, value);
        }

        public void addUploadFile(String key, final ArrayList<File> files, final UIchangeListener uIchangeListener) {

            long allsize = 0;
            final long[] cursize = {0};
            for (int i=0;i<files.size();i++)
            {
                File file = files.get(i);
                allsize += file.length();
            }
            for (int i=0;i<files.size();i++)
 /*           for (File file : files)*/
            {
                File file = files.get(i);
                final int position = i+1;
                final int all = files.size();
                RequestBody body = RequestBody.create(MEDIA_TYPE, file);
                final long finalAllsize = allsize;
                ProgressRequestBody progressRequestBody=ProgressHelper.addProgressRequestListener(body, new UIProgressRequestListener() {
                    @Override
                    public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {
                        if(done){
                            cursize[0] +=contentLength;
                        }
                        uIchangeListener.progressUpdate(cursize[0]+bytesWrite, finalAllsize,done,position,all);
                    }
                });
                requestBody.addFormDataPart(key, file.getAbsolutePath(),
                        progressRequestBody);
            }
        }

        public void uploadFile(String key, File file,final UIchangeListener uIchangeListener) {
            RequestBody body = RequestBody.create(MEDIA_TYPE, file);
            ProgressRequestBody progressRequestBody=ProgressHelper.addProgressRequestListener(body, new UIProgressRequestListener() {
                @Override
                public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {
                    uIchangeListener.progressUpdate(bytesWrite, contentLength,done,1,1);
                }
            });
            requestBody.addFormDataPart(key, file.getAbsolutePath(),
                    progressRequestBody);
        }

        /**
         * 获得拼接的Url
         *
         * @return 拼接的Url，带有请求参数
         */
        @Override
        public String toString() {
            return paramsString == null ? url : url + paramsString.toString();
        }
    }

    public interface UIchangeListener{
        void progressUpdate(long bytesWrite, long contentLength, boolean done,int position,int all);
    }

}
