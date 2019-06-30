/*
 * @(#) WhileStatement.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * While循环语法树
 * @author ChenYuyang
 * @version 1.1
 */
public class WhileStatementTree implements Tree {
    /**
     * 构建一颗while语法树 while(condition){trueStatement();}
     * @param condition 判断语句
     * @param trueStatement 如果为真执行的语句
     */
    public WhileStatementTree(ExpressionTree condition, StatementsTree trueStatement){
        this.condition=condition;
        this.trueStatement=trueStatement;
    }

    /**
     * while(condition){trueStatement();}
     * @param env 运行环境
     * @throws SynTreeRuntimeException
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        int conditionValue;
        while(true) {
            condition.exec(env);
            conditionValue=condition.getValue();
            if(conditionValue==0)
                break;
            trueStatement.exec(env);
            if(env.hasReturnTree())break;
        }
    }

    /**
     * 把树放进图结构里面去
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from){
        String myName = String.format("while_%s",drawer.genVertice());
        String con=drawer.genVertice(),t=drawer.genVertice();

        drawer.addVertice(myName);
        drawer.addVertice(con);
        drawer.addVertice(t);

        drawer.addEdge(from,myName,"");
        drawer.addEdge(myName,con,"condition");
        drawer.addEdge(myName,t,"WhileTrue");

        condition.putIntoGraphviz(drawer,con);
        trueStatement.putIntoGraphviz(drawer,t);
    }
    ExpressionTree condition;
    StatementsTree trueStatement;
}
