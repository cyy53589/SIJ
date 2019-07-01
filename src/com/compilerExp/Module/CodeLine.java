/*
 * @(#) Strategy.java 2019/05/28
 */
package com.compilerExp.Module;

import com.compilerExp.TreeRuntimeEnv.DebugTreeRunEnv;
import com.compilerExp.SyntaxTree.Tree;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;
import com.compilerExp.Token.IdentifierToken;
import com.compilerExp.Token.Token;
import com.compilerExp.component.Lexer;
import com.compilerExp.component.RecursiveDescent;
import com.compilerExp.util.CompilerException;
import com.compilerExp.util.GraphDrawer;
import com.compilerExp.util.SynTreeRuntimeException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 策略类
 * @version 1.0
 * @author ChenYuyang
 */
public class CodeLine {
    /**
     *
     * @param content 内容
     */
    public CodeLine(String content){
        this.contentInStr = content;
        this.lexer = Lexer.getRegLexer();
    }
    /**
     * @param content 内容
     * @param Lexer lexer
     */
    public CodeLine(String content,Lexer lexer ){
        this.contentInStr = content;
        this.lexer = lexer;
    }

    /**
     * 编译策略
     * @return 是否正常
     */
    public boolean compileAndRun(TreeRunEnv env){
        try {
            ArrayList<Token> tokenArrayList = lexer.analyse(contentInStr,0);
            root = RecursiveDescent.run(tokenArrayList);
            run(env);
        }catch (CompilerException e){
            //errorLine=content.get(e.getErrorLine());
            errorMsg=getErrorWavyLine(e.getErrorRow()+2)+": "+e.getMessage();
        }catch (Exception e){
            errorMsg= e.getMessage();
        }
        return root!=null;
    }

    /**
     * 运行策略
     * @param env 运行环境
     * @return 返回运行是否成功
     */
    private boolean run(TreeRunEnv env) throws Exception{
            root.exec(env);
            if(env instanceof DebugTreeRunEnv) {
                writeOutPesudoCode(((DebugTreeRunEnv)env).getPseudocode());
            }
            return true;
    }

    /**
     * 得到策略的dot代码
     * @return dot代码
     */
    public String getDotCode(){
        GraphDrawer drawer = new GraphDrawer();
        String rootVe = drawer.genVertice();
        drawer.addVertice(rootVe);
        root.putIntoGraphviz(drawer,rootVe);
        return drawer.getDotCode();
    }

    /**
     * 把伪代码写入文件中去
     * @param pesudoCode 伪代码
     */
    void writeOutPesudoCode(String pesudoCode){
        String pesName = "pesName";
        try {
            PrintStream os=new PrintStream(new FileOutputStream(pesName,true));
            os.println(pesudoCode);
            os.close();
        }catch (Exception e){
        }
    }

    String getErrorWavyLine(int row){
        StringBuilder sb = new StringBuilder();
        for(int i=1;i<row;++i)sb.append('~');
        sb.append('^');
        return sb.toString();
    }

    /**
     *
     * @return 得到上一次的错误
     */
    public String getLastError(){
        return errorMsg;
    }

    /**
     *
     * @return 本策略是否编译成功过了
     */
    public boolean isCompiled(){return root!=null;}

    Tree root=null;
    String contentInStr;
    String errorMsg,errorLine;
    ArrayList<String> content = new ArrayList<>();
    Lexer lexer;
}
