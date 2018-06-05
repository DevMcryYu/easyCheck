package com.devmcryyu.easycheck.Graph;

import java.util.Iterator;

/**
 * Created by 92075 on 2018/6/4.
 */

public interface VertexInterface<T> {
    /**
     * 取得顶点的标识--顶点标识用来区分各个顶点
     */

    public T getLabel();

    public void visit();

    public void unVisit();

    public boolean isVisited();

    /**
     * 用一条加权边连接该顶点与指定顶点
     *
     * @param endVertex  图中作为这条边终点的顶点
     * @param edgeWeight 实数值的边权值,如果有的话
     * @return 若插入成功, 返回true, 否则返回false
     */

    public boolean connect(VertexInterface<T> endVertex, double edgeWeight);

    /**
     * 用一条无权边连接该顶点与指定顶点
     *
     * @param endVertex 图中作为这条边终点的顶点
     * @return 若插入成功, 返回true
     */

    public boolean connect(VertexInterface endVertex);

    /**
     * 创建一个遍迭代器遍历从该顶点开始的所有边
     *
     * @return 从该顶点开始的边对象的迭代器
     */

    public Iterator<VertexInterface<T>> getNeighborIterator();

    /**
     * 创建一个迭代器,计算从该顶点到其邻接点的边的权重
     *
     * @return 该顶点的所有邻接点的权重迭代器
     */

    public Iterator<Double> getWeightIterator();

    public boolean hasNeighbor();                                                                   //查看顶点是否最少有一个邻接点

    /**
     * 取得该顶点一个未访问的邻接点,如果有的话
     *
     * @return 未访问的邻接点顶点, 若不存在这样的邻接点则返回 null
     */

    public VertexInterface<T> getUnvisitedNeighbor();

    /**
     * 记录到该顶点路径上的前一个顶点
     *
     * @param predecessor 该顶点的前一个顶点
     */

    public void setPredecessor(VertexInterface<T> predecessor);

    /**
     * 取得该顶点路径上的前一个顶点
     *
     * @return 前一个顶点, 若没有返回 null
     */

    public VertexInterface<T> getPredecessor();

    /**
     * 检查前一个顶点是否被记录
     *
     * @return 如果为该顶点记录了前一个顶点, 则返回true
     */

    public boolean hasPredecessor();

    public void setCost(double newCost);//设置到该顶点路径的费用

    public double getCost();//提取到该顶点路径的费用
}
