/*
 * @(#) ExressionTree.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.Token.Token;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * 表达式语法树
 *
 * @author ChenYuyang
 * @version 1.0
 */
public abstract class ExpressionTree implements Tree {
    public static class NullExpressionTree extends ExpressionTree {
        public NullExpressionTree(Token result) {
            this.result = result;
        }

        @Override
        public boolean writeable() {
            return false;
        }

        @Override
        public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
            throw new SynTreeRuntimeException(result.getLineNumber(), result.getRowNumber(), "此处运算表达式没有确切的值");
        }

        @Override
        public int getValue() {
            return 0;
        }

        Token result;

        @Override
        public void putIntoGraphviz(GraphDrawer drawer, String from) {
            ;
        }
    }

    /**
     * 本树的返回值是否可以被写,是否是左值
     *
     * @return 本树的返回值是否可以被写, 是否是左值
     */
    public abstract boolean writeable();

    /**
     * 执行树的结构
     * @param env 运行环境
     * @throws SynTreeRuntimeException
     */
    @Override
    abstract public void exec(TreeRunEnv env) throws SynTreeRuntimeException;

    /**
     * @return 得到本树运行结果(注意 : IndentifierTree不应该通过本树获得值)
     */
    abstract public int getValue();

    protected int bool2int(boolean value) {
        return value ? 1 : 0;
    }

    protected boolean int2bool(int value) {
        return value != 0;
    }

    /**
     * 把树加进图里面去
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    abstract public void putIntoGraphviz(GraphDrawer drawer, String from);
}
