/*
 * @(#) SyntaxTree.java 2019/03/29
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * 语法树的抽象interface
 *
 * @author ChenYuyang
 * @version 1.0
 */
public interface Tree {
    /**
     * 执行语法树的代码
     * @param env 运行环境
     * @throws SynTreeRuntimeException 树运行时候产生的动态错误
     */
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException;

    /**
     * 把自己的图结构放进GraphDrawer中去
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     */
    public void putIntoGraphviz(GraphDrawer drawer,String from);
}
