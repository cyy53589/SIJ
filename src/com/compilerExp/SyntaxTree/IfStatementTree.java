/*
 * @(#) IfStatement.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * If语句语法树
 * @version 1.0
 * @author ChenYuyang
 */
public class IfStatementTree implements Tree {
    /**
     * 构建if语法树
     * @param condition 判断语句
     * @param trueStatement 如果condition为真,就执行此
     * @param falseStatement 如果condition为假,就执行此
     */
    public IfStatementTree(ExpressionTree condition, StatementsTree trueStatement, StatementsTree falseStatement){
        this.condition=condition;
        this.trueStatement=trueStatement;
        this.falseStatement=falseStatement;
    }

    /**
     * 执行if-statement
     * @param env 运行环境
     * @throws SynTreeRuntimeException
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        condition.exec(env);
        int conditionValue = condition.getValue();
        if(conditionValue==1){
            trueStatement.exec(env);
        }
        else{
            if(falseStatement!=null)
                falseStatement.exec(env);
        }
    }

    /**
     * 把树放入图结构中去
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from){
        String myName = String.format("If_%s",drawer.genVertice());
        String con=drawer.genVertice(),t=drawer.genVertice(),f=drawer.genVertice();

        drawer.addVertice(myName);
        drawer.addVertice(con);
        drawer.addVertice(t);
        if(falseStatement!=null)
            drawer.addVertice(f);

        drawer.addEdge(from,myName,"");
        drawer.addEdge(myName,con,"condition");
        drawer.addEdge(myName,t,"IfTrue");
        if(falseStatement!=null)
            drawer.addEdge(myName,f,"IfFalse");

        condition.putIntoGraphviz(drawer,con);
        trueStatement.putIntoGraphviz(drawer,t);
        if(falseStatement!=null)
            falseStatement.putIntoGraphviz(drawer,f);
    }
    ExpressionTree condition;
    StatementsTree trueStatement,falseStatement;
}
