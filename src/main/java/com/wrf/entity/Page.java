package com.wrf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: bookStore
 * @description: 用于分页显示
 * @author: Rifu Wu
 * @create: 2022-05-16 19:08
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    public static final Integer PAGE_SIZE = 4;
    //页码
    private Integer pageNo;
    //总页码
    private Integer pageTotal;
    //当前页显示数量
    private Integer pageSize = PAGE_SIZE;

    private Integer pageTotalCount;

    private List<T> items;
    //分页栏url
    private String url;

    public void setPageNo(Integer pageNo) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > pageTotal) {
            pageNo = pageTotal;
        }
        this.pageNo = pageNo;
    }
}