package com.neo.licensio.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final String jwtToken;

    public JwtInterceptor(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Add the JWT token to the request headers
        Request modifiedRequest = originalRequest.newBuilder()
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwtToken)
                .build();

        return chain.proceed(modifiedRequest);
    }
}
