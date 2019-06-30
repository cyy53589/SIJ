/*
 * @(#) DoubleToken.java 2019/03/29
 */
package com.compilerExp.Token;

public class DoubleToken extends NumberToken {
    int valueInDouble;

    /**
     * 构造函数,但是本项目中不使用它
     * @param value 值-str格式
     * @param line 行数
     * @param row 列数
     */
    public DoubleToken(String value, int line, int row) {
        super(value, line, row);
        this.valueInDouble = Integer.valueOf(value);
    }

    /**
     * 返回浮点数格式的值形式
     * @return 返回浮点值
     */
    public int getValueInDouble() {
        return valueInDouble;
    }
}
