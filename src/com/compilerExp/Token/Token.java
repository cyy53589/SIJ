/*
 * @(#) ParanToken.java 2019/03/29
 */
package com.compilerExp.Token;


/**
 * 所有Token的父类
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class Token implements Comparable<Token> {
    String value;
    int beginIndex, lineNumber;

    /**
     * 初始化
     *
     * @param value      token的值
     * @param beginIndex token出现的位置
     */
    public Token(String value, int lineNumber, int beginIndex) {
        this.value = value;
        this.lineNumber = lineNumber;
        this.beginIndex = beginIndex;
    }

    /**
     * @return 得到token的值
     */
    public String getValue() {
        return value;
    }

    /**
     * @return 得到token出现在句子中的位置
     */
    public int getRowNumber() {
        return beginIndex;
    }

    /**
     * @return 得到token在文件中出现的行数
     */
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public int compareTo(Token to) {
        return this.beginIndex - to.beginIndex;
    }
}
