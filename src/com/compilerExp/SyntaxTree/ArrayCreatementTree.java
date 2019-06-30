/*
 * @(#) ArrayCreatement.java 2019/05/28
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * 创造数组树类
 */
public class ArrayCreatementTree implements Tree{
    /**
     * 构建一个创造数组的语法树
     * @param arrayName 数组名字
     * @param arraySize 数组大小
     */
    public ArrayCreatementTree(IdentifierTree arrayName, ExpressionTree arraySize){
        this.arrayName=arrayName;
        this.arraySize=arraySize;
    }

    /**
     *
     * @param env 运行环境
     * @throws SynTreeRuntimeException
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        arraySize.exec(env);
        env.createArray(arrayName,arraySize.getValue());
    }

    /**
     *
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from 图的父节点
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from){
        String myName = String.format("AyCt_%s",drawer.genVertice());
        String leftName = String.format("AyNm_%s_%s",arrayName.getVariableName(),drawer.genVertice());
        String rightName = String.format("AySz_%s",drawer.genVertice());

        drawer.addVertice(myName);
        drawer.addVertice(leftName);
        drawer.addVertice(rightName);

        drawer.addEdge(from,myName,"CreateArray");
        drawer.addEdge(myName,leftName,"ArrayName");
        drawer.addEdge(myName,rightName,"ArraySize");
        arraySize.putIntoGraphviz(drawer,rightName);
    }
    IdentifierTree arrayName;
    ExpressionTree arraySize;
}
