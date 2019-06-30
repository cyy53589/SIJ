/*
 * @(#) Constant.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.Token.IntegerToken;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * 常量树.其实意义上并没有什么用,起到一个标志作用
 * @version 1.0
 * @author ChenYuyang
 */
public class ConstantTree extends ExpressionTree {
    /**
     * 构建一个正常量树
     * @param token 常量Token
     */
    public ConstantTree(IntegerToken token){
        integerToken=token;
        this.neg=false;
    }

    /**
     * 构建一个常量树,正负由neg决定
     * @param token 常量token
     * @param neg 正=true,负=false
     */
    public ConstantTree(IntegerToken token,boolean neg){
        this.integerToken=token;
        this.neg=neg;
    }

    /**
     * 不执行什么代码
     * @param env
     * @throws SynTreeRuntimeException
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        return;
    }

    /**
     * 树是否可以被写入
     * @return 是否是左值
     */
    @Override
    public boolean writeable(){
        return false;
    }

    /**
     * 得到树的值
     * @return
     */
    @Override
    public int getValue(){
        if(neg) return integerToken.getValueInInt()*-1;
        return integerToken.getValueInInt();
    }

    /**
     * 把树放进图中
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from) {
        String myName=String.format("Cnst_%d_%s",integerToken.getValueInInt(),drawer.genVertice());

        drawer.addVertice(myName);

        drawer.addEdge(from,myName,"");
    }
    IntegerToken integerToken;
    boolean neg=true;
}
