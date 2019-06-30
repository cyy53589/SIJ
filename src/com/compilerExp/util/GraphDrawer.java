/*
 * @(#) SplitOpToken.java 2019/03/29
 */
package com.compilerExp.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 绘画器
 */
public class GraphDrawer {
    /**
     * 为点产生器产生一条点
     * @return 返回产生的点的ID
     */
    public String genVertice(){
        return String.valueOf(uuid++);
    }

    /**
     *  把一个点增加进图中
     * @param verticeName 点的名字
     */
    public void addVertice(String verticeName){
        graphs.put(verticeName,new ArrayList<>());
    }

    /**
     * 增加一条边
     * @param from 边的起始点
     * @param to 边的终结点
     * @param weight 边的权重
     */
    public void addEdge(String from,String to,String weight){
        graphs.get(from).add(new Edge(to,weight));
    }

    /**
     * 得到从某点出发的所有边
     * @param verticeFrom 出发点
     * @return 返回边集
     */
    public ArrayList<Edge> getAllEdgeFrom(String verticeFrom){
        return graphs.get(verticeFrom);
    }

    /**
     * 得到Dot的代码
     * @return 见介绍
     */
    public String getDotCode(){
        StringBuilder sb = new StringBuilder();
        sb.append("digraph st{\n");
        for(String from : graphs.keySet()){
            sb.append(from+";\n");
        }
        for(HashMap.Entry<String,ArrayList<Edge>> curr : graphs.entrySet()){
            for(Edge edge : curr.getValue()){
                sb.append(String.format("%s -> %s [label=\"%s\"]\n",curr.getKey(),edge.to,edge.weight));
            }
        }
        sb.append("}");
        return sb.toString();
    }
    HashMap<String, ArrayList<Edge>> graphs= new HashMap<>();
    int uuid=0;
}

class Edge{
    public Edge(String to,String weight){
        this.to=to;
        this.weight=weight;
    }
    public String to,weight;
}
