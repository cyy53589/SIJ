package com.compilerExp;

import com.compilerExp.Module.CodeError;
import com.compilerExp.Module.CodeLine;
import com.compilerExp.TreeRuntimeEnv.TreeRunEnv;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CLI  {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TreeRunEnv env = new TreeRunEnv();
        while(sc.hasNextLine()){
            CodeLine c = new CodeLine(sc.nextLine());
            if(c.compileAndRun(env)){
                System.out.println(env.getLastOutput());
            }
            else{
                System.out.println(c.getLastError());
            }

        }
    }


    /*
    public static void main_2(String[] args) throws Exception {
        /*
        ArrayList<Token> tokens = new ArrayList<>();
        int line = 1;
        Scanner sc = new Scanner(new FileInputStream("in.txt"));
        while (sc.hasNextLine()) {
            tokens.addAll(lexer.analyse(sc.nextLine(), line++));
        }
        System.out.printf("Token size=%d\n", tokens.size());
        for (Token i : tokens) {
            System.out.printf("[%s\t]\tValue=\"%s\"\n", i.getClass().getSimpleName(), i.getValue());
        }
        System.out.println("===");
        Tree root = RecursiveDescent.run(tokens);
        DebugTreeRunEnv env=new DebugTreeRunEnv();
        root.exec(env);
        System.out.println("Result:"+env.getFinalResult());
    }
        */
    /*
    String getDoeCode(Tree root){
        GraphDrawer dg = new GraphDrawer();
        dg.addVertice("root");
        root.putIntoGraphviz(dg, "root");
        return dg.getDotCode();
    }
    */
}
