/*
 * @(#) IdentifierToken.java 2019/03/29
 */
package com.compilerExp.Token;

import java.util.HashSet;

/**
 * 标记符
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class IdentifierToken extends Token {
    static HashSet<String> keywords = new HashSet<>();

    static {
        keywords.add("if");
        keywords.add("while");
        keywords.add("for");
        keywords.add("return");
    }

    /**
     *
     * @param value Token名字
     * @param line 所在行数
     * @param row 所在列数
     */
    public IdentifierToken(String value, int line, int row) {
        super(value, line, row);
    }

    /**
     * 是否是关键字
     * @return 返回是否为关键字
     */
    boolean isKeyword() {
        return keywords.contains(this.value);
    }
}
