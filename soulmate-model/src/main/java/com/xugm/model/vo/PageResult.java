package com.xugm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult implements Serializable {

    private Long counts = 0l;//总记录数
    private Integer pagesize=10;//页大小
    private Long pages = 0l;//总页数
    private Integer page=1;//当前页码
    private List<?> items = Collections.emptyList(); //列表

    public PageResult(Integer page,Integer pagesize,
                      Long counts,List list) {
        this.page = page;
        this.pagesize = pagesize;
        this.items = list;
        this.counts = counts;
        this.pages =  counts % pagesize == 0 ? counts / pagesize : counts / pagesize + 1;
    }

}