package com.neo.licensio.network;

import lombok.Getter;
import lombok.Setter;

public class JwtToken {
    private static String jwtToken;

    public static String getJwtToken() {
        return jwtToken;
    }

    public static void setJwtToken(String jwtToken) {
        JwtToken.jwtToken = jwtToken;
    }
}
