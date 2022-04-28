package com.zhonghezhihui.iorg.util;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    public HttpDeleteWithBody() {
    }

    public HttpDeleteWithBody(URI uri) {
        this.setURI(uri);
    }

    public HttpDeleteWithBody(String uri) {
        this.setURI(URI.create(uri));
    }

    /**
    * 重载获取方法
    **/
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
