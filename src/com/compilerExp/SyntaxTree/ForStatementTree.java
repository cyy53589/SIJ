/*
 * @(#) ForStatement.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * For循环语句
 * @author ChenYuyang
 * @version 1.0
 */
public class ForStatementTree implements Tree {
    /**
     * 构建一个for循环语句
     * @param conditions 一个长度为3的条件表达式组
     * @param trueStatement 如果第二个条件是true,就执行这些表达式
     */
    public ForStatementTree(ExpressionTree[] conditions, StatementsTree trueStatement) {
        assert conditions.length == 3;
        this.conditions = conditions;
        this.trueStatement = trueStatement;
    }

    /**
     * 1. 执行condition[0] 2.若condition[1]为真,执行trueStatement,再执行condition[2],重复直到condition[1]==false
     * @param env 运行环境
     * @throws SynTreeRuntimeException
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        conditions[0].exec(env);
        while (true) {
            conditions[1].exec(env);
            int conditionValue = conditions[1].getValue();
            if (conditionValue == 0)
                break;
            trueStatement.exec(env);
            conditions[2].exec(env);
            if(env.hasReturnTree())break;
        }
    }

    /**
     * 把for循环加进图中
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from) {
        String myName = String.format("For_%s",drawer.genVertice());
        String[] expName = new String[3];
        for(int i=0;i<3;++i)expName[i]=String.format("Exp_%d_%s",i+1,drawer.genVertice());

        drawer.addVertice(myName);
        for(int i=0;i<3;++i)drawer.addVertice(expName[i]);

        drawer.addEdge(from,myName,"");
        for(int i=0;i<3;++i)drawer.addEdge(myName,expName[i],"");

        trueStatement.putIntoGraphviz(drawer,myName);
        for(int i=0;i<3;++i)conditions[i].putIntoGraphviz(drawer,expName[i]);
    }

    ExpressionTree[] conditions;
    StatementsTree trueStatement;
}
