/*
 * @(#) ReturnTree.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.Token.IdentifierToken;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * return-语法树
 * @version 1.0
 * @author ChenYuyang
 */
public class ReturnTree implements Tree{
    /**
     * 构建一颗返回语法树,期中IdentifierToken一定是=return
     * @param returnToken return-token
     * @param returnValue 返回值
     */
    public ReturnTree(IdentifierToken returnToken, ExpressionTree returnValue){
        this.returnToken=returnToken;
        this.returnValue=returnValue;
    }

    /**
     * 执行返回语句,结束程序
     * @param env 运行环境
     * @throws SynTreeRuntimeException
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException{
        returnValue.exec(env);
        env.setFinalResult(returnValue.getValue());
    }
    IdentifierToken returnToken;
    /**
     * 把树放入图结构中去
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from) {
        String myName = String.format("ret_%s",drawer.genVertice());
        drawer.addVertice(myName);
        drawer.addEdge(from,myName,"return");
        returnValue.putIntoGraphviz(drawer,myName);
    }
    ExpressionTree returnValue;
}
