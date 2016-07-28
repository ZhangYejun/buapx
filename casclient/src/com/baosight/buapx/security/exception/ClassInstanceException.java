package com.baosight.buapx.security.exception;

/**
 * @author Mai BAOSCAPE,Inc. mailTo: mai_seven[AT]sina[DOT]com<br>
 *         2011-12-28
 */
public class ClassInstanceException extends Exception {
	private static final long serialVersionUID = 1L;

	public ClassInstanceException(String msg) {
		super(msg);
	}

	public ClassInstanceException(String msg, Throwable t) {
		super(msg, t);
	}

}
