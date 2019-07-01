/*
 * @(#) DebugTreeRunEnv.java 2019/03/29
 */
package com.compilerExp.TreeRuntimeEnv;

import com.compilerExp.SyntaxTree.IdentifierTree;
import com.compilerExp.Token.IdentifierToken;
import com.compilerExp.Token.IntegerToken;
import com.compilerExp.Token.Token;
import com.compilerExp.util.SynTreeRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * Tree运行环境
 */
public class TreeRunEnv {
    static Random random = new Random();

    /**
     * 初始化运行环境
     */
    public TreeRunEnv(){
        this.arrayMap=new HashMap<>();
        this.variableMap=new HashMap<>();
        variableMap.put("round",0);
    }

    /**
     * 初始化运行环境
     * @param variableMap 变量映射
     * @param arrayMap 数组映射
     */
    public TreeRunEnv(HashMap<String,Integer> variableMap,HashMap<String,ArrayList<Integer>> arrayMap){
        this.arrayMap=new HashMap<>(arrayMap);
        this.variableMap=new HashMap<>(variableMap);
        variableMap.put("round",0);
    }
    /**
    * 初始化运行环境
    * @param arrayMap 数组映射
    */
    public TreeRunEnv(HashMap<String,ArrayList<Integer>> arrayMap){
        this.arrayMap=new HashMap<>(arrayMap);
        variableMap=new HashMap<>();
        variableMap.put("round",0);
    }

    /**
     * 设置一次运行的最终结果
     * @param value 返回值
     */
    public void setFinalResult(int value){
        hasEnd=true;
        finalResult=value;
    }

    /**
     * 一次运行的过程中是否有返回值
     * @return 一次运行的过程中是否有返回值
     */
    public boolean hasReturnTree(){
        return hasEnd;
    }

    /**
     * 重置是否结束
     */
    public void resetHasReturnTree(){
        hasEnd=false;
    }

    /**
     * 返回最终结果值
     * @return 返回最终结果值
     */
    public int getFinalResult() {
        return finalResult;
    }

    /**
     * 创造一个数组
     * @param varName 数组名字
     * @param arraySize 数组大小
     * @throws SynTreeRuntimeException
     */
    public void createArray(IdentifierTree varName, int arraySize) throws SynTreeRuntimeException{
        if(arrayMap.containsKey(varName.getVariableName())){
            arrayMap.remove(varName.getVariableName());
        }
        ArrayList<Integer> arrayList = new ArrayList<>(arraySize);
        for(int i=0;i<arraySize;++i){
            arrayList.add(0);
        }
        arrayMap.put(varName.getVariableName(),arrayList);
    }

    /**
     * 赋值或者创造一个变量
     * @param varName 变量名字(或者数组名字+下标)
     * @param value 值
     * @throws SynTreeRuntimeException
     */
    public void assignOrCreate(IdentifierTree varName,int value)throws SynTreeRuntimeException{
        lastOutput=String.valueOf(value);
        // 如果varName是数组下标索引
        if(varName.getIsArray()){
            if(arrayMap.containsKey(varName.getItdentifierToken().getValue())){
                ArrayList<Integer> arrayValue = arrayMap.get(varName.getItdentifierToken().getValue());
                checkIndex(arrayValue,varName.getIndex(this),varName.getItdentifierToken());
                int indexOfArray=varName.getIndex(this);
                arrayValue.set(indexOfArray,value);
            }
            else{
                throw  new SynTreeRuntimeException(
                        varName.getItdentifierToken().getLineNumber(),
                        varName.getItdentifierToken().getRowNumber(),
                        varName.getItdentifierToken().getValue()+"不是一个数组");
            }
        }
        // 如果varName是一个变量
        else{
            if(variableMap.containsKey(varName.getItdentifierToken().getValue())){
                variableMap.remove(varName.getItdentifierToken().getValue());
            }
            variableMap.put(varName.getItdentifierToken().getValue(),value);
        }
    }

    /**
     * 得到变量的值,当作右值用
     * @param varName 变量名字
     * @return 返回的值
     * @throws SynTreeRuntimeException
     */
    public int getVariableValue(IdentifierTree varName) throws SynTreeRuntimeException {
        if(varName.getIsArray()){
            if(!arrayMap.containsKey(varName.getVariableName()))
                throw new SynTreeRuntimeException(
                        varName.getItdentifierToken().getLineNumber(),
                        varName.getItdentifierToken().getRowNumber(),
                        String.format("变量%s没有先定义",varName.getVariableName()));
            ArrayList<Integer> array = arrayMap.get(varName.getVariableName());
            checkIndex(array,varName.getIndex(this),varName.getItdentifierToken());
            int forRet = array.get(varName.getIndex(this));
            lastOutput = String.valueOf(forRet);
            return forRet;
        }
        else{
            if(varName.getItdentifierToken().getValue().contentEquals("rand")){
                return random.nextInt(100);
            }
            if(!variableMap.containsKey(varName.getVariableName()))
                throw new SynTreeRuntimeException(
                        varName.getItdentifierToken().getLineNumber(),
                        varName.getItdentifierToken().getRowNumber(),
                        String.format("变量%s没有先定义",varName.getVariableName()));
            int forRet =  variableMap.get(varName.getItdentifierToken().getValue());
            lastOutput = String.valueOf(forRet);
            return forRet;
        }
    }

    /**
     * 增加运行次数统计
     */
    public void addRound(){
        int round=0;
        if(variableMap.containsKey("round")){
            round = variableMap.get("round");
            variableMap.remove("round");
        }
        variableMap.put("round",round+1);
    }
    void checkIndex(ArrayList<Integer> arrayList, int index, Token tokenResulting) throws SynTreeRuntimeException{
        if(index<0 || index>=arrayList.size())
            throw new SynTreeRuntimeException(tokenResulting.getLineNumber(),tokenResulting.getRowNumber(),"数组越界");
    }
    public String getLastOutput(){
        String forRet = lastOutput;
        lastOutput="";
        return forRet;
    }
    String lastOutput="";
    HashMap<String,Integer> variableMap;
    HashMap<String, ArrayList<Integer>> arrayMap;
    boolean hasEnd=false;
    int finalResult;
}
