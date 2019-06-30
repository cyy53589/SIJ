/*
 * @(#) CodeError.java 2019/05/28
 */
package com.compilerExp.Module;

import java.util.ArrayList;

/**
 * 代码编译运行错误的记录
 * @version 1.0
 * @author ChenYuyang
 */
public class CodeError {
    /**
     * 构建一个空白的错误记录
     */
    public CodeError(){
    }

    /**
     * 构建一个错误记录
     * @param fileName 错误出现的文件名
     * @param msg 错误信息
     */
    public CodeError(String fileName,String msg){
        this.fileName=fileName;
        this.msg=msg;
    }

    /**
     * @return 得到错误记录的文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置错误记录文件名
     * @param fileName 错误记录文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    String fileName;

    /**
     * @return 得到错误记录的信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置错误记录信息
     * @param msg 错误记录信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    String msg;
}
