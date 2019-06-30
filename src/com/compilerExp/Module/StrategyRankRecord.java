/*
 * @(#) StrategyRankRecord.java 2019/05/28
 */
package com.compilerExp.Module;

/**
 * 策略排名记录
 * @version 1.0
 * @author ChenYuyang
 */
public class StrategyRankRecord {
    /**
     * 构建一个策略排名记录
     */
    public StrategyRankRecord(){

    }

    /**
     * 构建一个策略排名记录
     * @param rank 排名
     * @param Sname 名字
     * @param score 分数
     */
    public StrategyRankRecord(int rank,String Sname,int score){
        this.score=score;
        this.rank=rank;
        this.Sname=Sname;
    }

    /**
     *
     * @return 得到排名
     */
    public int getRank() {
        return rank;
    }

    /**
     *
     * @param rank 设置排名
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     *
     * @return 得到策略名字
     */
    public String getSname() {
        return Sname;
    }

    /**
     *
     * @param sname 设置策略名字
     */
    public void setSname(String sname) {
        Sname = sname;
    }

    /**
     *
     * @return 得到策略名字
     */
    public int getScore() {
        return score;
    }

    /**
     *
     * @param score 设置策略名字
     */
    public void setScore(int score) {
        this.score = score;
    }


    int score;
    int rank;
    String Sname;
}
