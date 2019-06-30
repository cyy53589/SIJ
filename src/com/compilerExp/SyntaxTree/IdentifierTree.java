/*
 * @(#) IdentifierTree.java 2019/04/15
 */
package com.compilerExp.SyntaxTree;

import com.compilerExp.Token.IdentifierToken;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

/**
 * 标识符树
 *
 * @author ChenYuyang
 * @version 1.0
 */
public class IdentifierTree extends ExpressionTree {
    boolean isArray;
    IdentifierToken identifierToken = null;
    ExpressionTree subIndex = null;

    /**
     * 构建一个标识符树
     * @param identifier 标识符
     */
    public IdentifierTree(IdentifierToken identifier) {
        this.identifierToken = identifier;
        this.isArray = false;
    }

    /**
     * 构建一个数组树
     * @param identifierToken 数组名字
     * @param subIndex 下标
     */

    public IdentifierTree(IdentifierToken identifierToken, ExpressionTree subIndex) {
        this.identifierToken = identifierToken;
        this.subIndex = subIndex;
        this.isArray = true;
    }

    /**
     * 得到变量的名字
     * @return 变量名字
     */
    public String getVariableName() {
        return identifierToken.getValue();
    }

    /**
     * 返回是否是一个数组
     * @return 是否是一个数组
     */
    public boolean getIsArray() {
        return isArray;
    }

    /**
     * 如果是一个数组就返回其下标
     * @param env 运行环境
     * @return 下标值
     * @throws SynTreeRuntimeException
     */
    public int getIndex(TreeRunEnv env) throws SynTreeRuntimeException {
        subIndex.exec(env);
        return subIndex.getValue();
    }

    /**
     * 得到Token
     * @return token
     */
    public IdentifierToken getItdentifierToken() {
        return identifierToken;
    }

    /**
     * 得到标识符的值
     * @return 标识符的值
     */
    @Override
    public int getValue() {
        return resultReturn;
    }

    /**
     * 标识符可写入
     * @return true
     */
    @Override
    public boolean writeable() {
        return true;
    }

    /**
     * 把本树放入图结构
     * @param drawer 图结构的目的地，可以产生graphViz的图示
     * @param from
     */
    @Override
    public void putIntoGraphviz(GraphDrawer drawer, String from){
        if(this.getIsArray()){
            String myName=String.format("Array_%s_%s",identifierToken.getValue(),drawer.genVertice());
            String indexName = String.format("Index_%s",drawer.genVertice());

            drawer.addVertice(myName);
            drawer.addVertice(indexName);

            drawer.addEdge(from,myName,"");
            drawer.addEdge(myName,indexName,"SubIndex");
            subIndex.putIntoGraphviz(drawer,indexName);
        }
        else{
            String myName = String.format("ID_%s_%s",identifierToken.getValue(),drawer.genVertice());

            drawer.addVertice(myName);

            drawer.addEdge(from,myName,"");
        }
    }

    /**
     * 标识符语法树不做任何事情
     *
     * @param env
     */
    @Override
    public void exec(TreeRunEnv env) throws SynTreeRuntimeException {
        resultReturn = env.getVariableValue(this);
    }

    int resultReturn;
}
