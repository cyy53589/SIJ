/*
 * @(#) StatementsTree.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

import java.util.ArrayList;

/**
 * 语句集-语法树
 * @author ChenYuyang
 * @version 1.0
 */
public class StatementsTree implements Tree {
    /**
     * 使用多个语法树集合在一起构建成一个语句集
     * @param trees 子语句
     */
    public StatementsTree(ArrayList<Tree> trees){
        this.childTrees=trees;
    }

    /**
     * 按照顺序执行语句
     * @param env 运行环境
     * @throws SynTreeRuntimeException
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        for(Tree i : childTrees){
            i.exec(env);
            if(env.hasReturnTree()){
                break;
            }
        }
    }

    /**
     * 把树放进图结构里面去
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from) {
        String[] childName = new String[childTrees.size()];

        for(int i=0;i<childName.length;++i)
            childName[i]=String.format("St_%s",drawer.genVertice());

        for(String i: childName)
            drawer.addVertice(i);

        drawer.addEdge(from,childName[0],"");
        for(int i=1;i<childName.length;++i)
            drawer.addEdge(childName[i-1],childName[i],"Next");

        for(int i=0;i<childName.length;++i)
            childTrees.get(i).putIntoGraphviz(drawer,childName[i]);
    }
    ArrayList<Tree> childTrees;
}
