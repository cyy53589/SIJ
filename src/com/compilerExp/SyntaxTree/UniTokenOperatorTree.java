/*
 * @(#) UniTokenOperator.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.Token.Token;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * 一元运算符
 * @author ChenYuyang
 * @version 1.0
 */
public class UniTokenOperatorTree extends ExpressionTree {
    public UniTokenOperatorTree(Token operator, ExpressionTree uniExpressionTree) {
        this.uniExpressionTree = uniExpressionTree;
        this.operator = operator;
    }

    /**
     * 执行语句.注意:唯一的一元运算符是逻辑反
     * @param env 运行环境
     * @throws SynTreeRuntimeException
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        uniExpressionTree.exec(env);
        resultValue = bool2int(!int2bool(uniExpressionTree.getValue()));
    }

    /**
     * 得到结果值
     * @return 返回结果值
     */
    @Override
    public int getValue() {
        return resultValue;
    }

    /**
     * 结果返回是临时值,不可以被写入
     * @return false
     */
    @Override
    public boolean writeable() {
        return false;
    }

    /**
     * 把树放进图结构里面去
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from){
        String myName = String.format("UiOp_%s",drawer.genVertice());
        String OpName = String.format("OP_%s",drawer.genVertice());

        drawer.addVertice(myName);
        drawer.addVertice(OpName);

        drawer.addEdge(from,myName,"");
        drawer.addEdge(myName,OpName,"Operator"+operator.getValue());

        uniExpressionTree.putIntoGraphviz(drawer,myName);
    }

    int resultValue;
    ExpressionTree uniExpressionTree;
    Token operator;

}
