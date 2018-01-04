package com.cdhy.exception;

public class BizException extends RuntimeException {

    private static final long serialVersionUID = -8283607968431589418L;

    public BizException() {
	super();
    }

    public BizException(String msg) {
	super(msg);
    }

    public BizException(Throwable e) {
	super(e);
    }

    public BizException(String msg, Throwable e) {
	super(msg, e);
    }

}
