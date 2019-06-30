/*
 * @(#) NumberToken.java 2019/03/29
 */
package com.compilerExp.Token;

/**
 * 数字符号
 *
 * @author ChenYuyang
 * @version 1.0
 */
public abstract class NumberToken extends Token {
    /**
     * 初始化
     *
     * @param value      token的值
     * @param beginIndex token出现的位置
     */
    public NumberToken(String value, int lineNumber, int beginIndex) {
        super(value, lineNumber, beginIndex);
    }
}
