/*
 * @(#) EndToken.java 2019/03/29
 */
package com.compilerExp.Token;

/**
 * 结束符号
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class EndToken extends Token {
    /**
     * 初始化
     */
    public EndToken() {
        super("EOF", -1, -1);
    }
}
