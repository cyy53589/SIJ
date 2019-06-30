/*
 * @(#) CompilerException.java 2019/03/29
 */

package com.compilerExp.util;

/**
 * 编译异常
 */
public abstract class CompilerException extends Exception{
    public CompilerException(int line, int row, String msg) {
        super(msg);
        this.errorLine = line;
        this.errorRow = row;
    }
    /**
     * @return 返回错误所在列
     */
    public int getErrorRow(){
        return errorRow;
    }

    /**
     * @return 返回错误所在行
     */
    public int getErrorLine(){
        return errorLine;
    }
    int errorRow, errorLine;
}
