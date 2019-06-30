/*
 * @(#) RecursiveDescentException.java 2019/03/29
 */
package com.compilerExp.util;

/**
 * 这个类是递归下降分析方法异常类
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class RecursiveDescentException extends CompilerException {
    /**
     * 初始化
     *
     * @param line token的函数
     * @param row  token在句子里index的位置
     * @param msg  错误信息
     */
    public RecursiveDescentException(int line, int row, String msg) {
        super(line,row,msg);
    }
}
