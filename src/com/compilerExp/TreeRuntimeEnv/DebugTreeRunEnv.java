/*
 * @(#) DebugTreeRunEnv.java 2019/03/29
 */
package com.compilerExp.TreeRuntimeEnv;

import com.compilerExp.SyntaxTree.IdentifierTree;
import com.compilerExp.util.SynTreeRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Debug用的运行环境. 因为写出大量的IO所以小心使用,不然会严重降低速度
 * @version 1.0
 * @author ChenYuyang
 */
public class DebugTreeRunEnv extends TreeRunEnv{
    /**
     * 构建运行环境
     * @param arrayMap 初始化数组映射
     */
    public DebugTreeRunEnv(HashMap<String, ArrayList<Integer>> arrayMap){
        super(arrayMap);
    }

    /**
     * 构建运行环境
     */
    public DebugTreeRunEnv(){
        super();
    }

    /**
     * 设置最终返回值
     * @param value 返回值
     */
    @Override
    public void setFinalResult(int value){
        super.setFinalResult(value);
        pseudocode.append(String.format("[return]:%d\n",value));
    }

    /**
     * 创造一个数组
     * @param varName 数组名字
     * @param arraySize 数组大小
     * @throws SynTreeRuntimeException
     */
    @Override
    public void createArray(IdentifierTree varName, int arraySize)throws SynTreeRuntimeException{
        super.createArray(varName,arraySize);
        pseudocode.append(String.format("[CreateArray]:ID:%s;size:%d\n",varName.getVariableName(),arraySize));
    }

    /**
     * 赋值或者创造一个变量
     * @param varName 变量名字(或者数组名字+下标)
     * @param value 值
     * @throws SynTreeRuntimeException
     */
    @Override
    public void assignOrCreate(IdentifierTree varName,int value) throws SynTreeRuntimeException{
        super.assignOrCreate(varName,value);
        if(varName.getIsArray()){
            pseudocode.append(String.format("[Assign]%s[%d]=%d\n",varName.getVariableName(),varName.getIndex(this),value));
        }
        else{
            pseudocode.append(String.format("[Assign]%s=%d\n",varName.getVariableName(),value));
        }
    }

    /**
     *
     * @param varName 变量名字
     * @return 得到变量的值
     * @throws SynTreeRuntimeException
     */
    @Override
    public int getVariableValue(IdentifierTree varName)throws SynTreeRuntimeException{
        int forRet = super.getVariableValue(varName);
        pseudocode.append(String.format("[GetVariable] %s:%d\n",varName.getVariableName(),forRet));
        return forRet;
    }

    /**
     * 得到变量的值,但是不写log
     * @param varName
     * @return
     * @throws SynTreeRuntimeException
     */
    public int getVariableValueWithoutLog(IdentifierTree varName) throws SynTreeRuntimeException{
        int forRet = super.getVariableValue(varName);
        return forRet;
    }

    /**
     * 得到一次运行结果的伪代码
     * @return 伪代码
     */
    public String getPseudocode(){
        return String.format(
                        "=> round = %d\n" +
                        "=> my:\t\t%s\n" +
                        "=> enemy:\t%s\n" +
                                "====\n%s",
                this.variableMap.get("round"),
                arrayList2String(this.arrayMap.get("my")),
                arrayList2String(this.arrayMap.get("enemy")),
                pseudocode.toString()
        );
    }
    StringBuilder pseudocode = new StringBuilder();
    static String arrayList2String(ArrayList<Integer> list){
        StringBuilder sb = new StringBuilder();
        for(Integer i : list){
            sb.append(i);
            sb.append(',');
        }
        return sb.toString();
    }
}
