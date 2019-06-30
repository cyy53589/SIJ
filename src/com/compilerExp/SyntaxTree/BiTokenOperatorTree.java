/*
 * @(#) BiTokenOperator.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.Token.ArOpToken;
import com.compilerExp.Token.AssignOpToken;
import com.compilerExp.Token.LogOpToken;
import com.compilerExp.Token.Token;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * 二元运算符语法树
 * @version 1.0
 * @author ChenYuyang
 */
public class BiTokenOperatorTree extends ExpressionTree {
    /**
     * 二元运算符
     * @param operator 运算符号
     * @param leftTree 左运算元素
     * @param rightTree 右运算元素
     */
    public BiTokenOperatorTree(Token operator, ExpressionTree leftTree, ExpressionTree rightTree) {
        this.operator = operator;
        this.leftTree = leftTree;
        this.rightTree = rightTree;
    }

    /**
     * 得到运算结果值
     * @return 运算结果值
     */
    @Override
    public int getValue() {
        return valueResult;
    }

    /**
     * 从右到左计算
     * @param env 运行环境
     * @throws SynTreeRuntimeException 如果运算符是=号,并且左边是左值时候,抛出异常
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        if (!(operator instanceof LogOpToken || operator instanceof ArOpToken || operator instanceof AssignOpToken)) {
            throw new SynTreeRuntimeException(operator.getLineNumber(), operator.getLineNumber(), "需要正确二元的运算符");
        }
        // 判断一下是否赋值
        if (operator instanceof AssignOpToken) {
            if(!(leftTree instanceof IdentifierTree) || !leftTree.writeable()){
                throw new SynTreeRuntimeException(operator.getLineNumber(), operator.getLineNumber(), "赋值运算符左边应该为可赋值的值");
            }
            rightTree.exec(env);
            int rightValue = rightTree.getValue();
            env.assignOrCreate((IdentifierTree) leftTree, rightValue);
            valueResult = env.getVariableValue((IdentifierTree) leftTree);
            return;
        }
        String OperatorStr = operator.getValue();
        rightTree.exec(env);
        leftTree.exec(env);
        int rightValue = rightTree.getValue(), leftValue = leftTree.getValue();
        switch (OperatorStr) {
            case "+":
                valueResult = leftValue + rightValue;
                break;
            case "-":
                valueResult = leftValue - rightValue;
                break;
            case "*":
                valueResult = leftValue * rightValue;
                break;
            case "/":
                valueResult = leftValue / rightValue;
                break;
            case ">=":
                valueResult = bool2int(leftValue >= rightValue);
                break;
            case "<=":
                valueResult = bool2int(leftValue <= rightValue);
                break;
            case "==":
                valueResult = bool2int(leftValue == rightValue);
                break;
            case ">":
                valueResult = bool2int(leftValue > rightValue);
                break;
            case "<":
                valueResult = bool2int(leftValue < rightValue);
                break;
            case "!=":
                valueResult = bool2int(leftValue != rightValue);
                break;
            case "||":
                valueResult = bool2int(int2bool(leftValue) || int2bool(rightValue));
                break;
            case "&&":
                valueResult = bool2int(int2bool(leftValue) && int2bool(rightValue));
                break;
        }
    }

    /**
     * 结果不可写入
     * @return false
     */
    @Override
    public boolean writeable() {
        return false;
    }

    /**
     * 把树放入图中
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from) {
        String myName = String.format("BiOp_%s",drawer.genVertice());
        String OpName = String.format("OP_%s",drawer.genVertice());
        String leftTmpNode = String.format("LeftValue_%s",drawer.genVertice());
        String rightTmpNode = String.format("RightValue_%s",drawer.genVertice());

        drawer.addVertice(myName);
        drawer.addVertice(OpName);
        drawer.addVertice(leftTmpNode);
        drawer.addVertice(rightTmpNode);

        drawer.addEdge(from,myName,"");
        drawer.addEdge(myName,OpName,"Operator"+operator.getValue());
        drawer.addEdge(myName,leftTmpNode,"Left value");
        drawer.addEdge(myName,rightTmpNode,"right value");


        leftTree.putIntoGraphviz(drawer,leftTmpNode);
        rightTree.putIntoGraphviz(drawer,rightTmpNode);
    }

    int valueResult;
    Token operator;
    ExpressionTree leftTree, rightTree;
}
