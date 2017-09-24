package com.github.ckroeger.whatismyip.util;

public class ResolveException extends RuntimeException {

    public ResolveException(String msg, Throwable cause){
        super(msg, cause);
    }
}
