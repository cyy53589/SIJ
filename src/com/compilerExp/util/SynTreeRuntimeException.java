/*
 * @(#) SynTreeRuntimeException.java 2019/03/29
 */
package com.compilerExp.util;

/**
 * 树运行时异常
 */
public class SynTreeRuntimeException extends CompilerException{
    /**
     *
     * @param line token的函数
     * @param row  token在句子里index的位置
     * @param msg  错误信息
     */
    public SynTreeRuntimeException(int line,int row,String msg){
        super(line,row,msg);
    }
}
