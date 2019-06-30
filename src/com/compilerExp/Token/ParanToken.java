/*
 * @(#) ParanToken.java 2019/03/29
 */
package com.compilerExp.Token;

/**
 * 括号token
 */
public class ParanToken extends ArOpToken {
    String value;

    /**
     * 初始化
     *
     * @param value      token的值
     * @param beginIndex token出现的位置
     */
    public ParanToken(String value, int line, int beginIndex) {
        super(value, line, beginIndex);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
