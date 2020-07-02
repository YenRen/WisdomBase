package com.hnxx.wisdombase.framework.retrofit.entity;

import java.util.List;

/**
 * @author zhoujun
 * @date 2019/12/19
 * Describe:我们项目用的page数据返回model
 */
public class PageResult<T> extends Result<List<T>> implements IPageData<T> {
    private int total;
    private int pagenum = 1;
    public int limit = 10;//如果接口返回一页请求多少条就完美了，这里先写死

    @Override
    public boolean isLastPage() {
        return pagenum * limit >= total;
    }

    @Override
    public boolean isNoData() {
        return total <= 0;
    }

    @Override
    public int getPageNum() {
        return pagenum;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public int totalPages() {
        return (int) Math.ceil(total / limit);
    }

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public List<T> getList() {
        return getData();
    }

    public boolean isEmpty() {
        return getData() == null || getData().isEmpty();
    }

    public int getRequestPageNum(boolean refresh) {
        return refresh ? 1 : (pagenum + 1);
    }
}
