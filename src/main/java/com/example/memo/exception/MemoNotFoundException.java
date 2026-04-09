package com.example.memo.exception;

/**
 * 備忘錄找不到時拋出的自訂例外
 */
public class MemoNotFoundException extends RuntimeException {

    public MemoNotFoundException(String message) {
        super(message);
    }

    public MemoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
