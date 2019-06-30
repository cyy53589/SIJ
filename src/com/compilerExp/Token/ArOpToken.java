/*
 * @(#) ArOpToken.java 2019/03/29
 */
package com.compilerExp.Token;

/**
 * 算数操作符
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class ArOpToken extends Token {
    /**
     * 初始化
     *
     * @param value      token的值
     * @param beginIndex token出现的位置
     */
    public ArOpToken(String value, int line, int beginIndex) {
        super(value, line, beginIndex);
    }
}
