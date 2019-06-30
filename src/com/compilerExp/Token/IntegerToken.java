/*
 * @(#) IntegerToken.java 2019/03/29
 */
package com.compilerExp.Token;

/**
 * 整数token
 */
public class IntegerToken extends NumberToken {
    int valueInInt;

    /**
     * 构造一个整数的Token
     * @param value 值
     * @param line 行数
     * @param row 列数
     */
    public IntegerToken(String value, int line, int row) {
        super(value, line, row);
        this.valueInInt = Integer.valueOf(value);
    }

    /**
     * 得到Int形式的值
     * @return 值
     */
    public int getValueInInt() {
        return valueInInt;
    }
}
