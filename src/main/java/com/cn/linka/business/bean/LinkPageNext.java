package com.cn.linka.business.bean;

import lombok.Data;

import java.util.List;

/**
 * 下拉分页
 */
@Data
public class LinkPageNext {
    /**
     * 本页的最大Id，透传给服务端
     */
    private Long nextId;
    /**
     * 是否是最后一页
     */
    private Boolean isLast;
    /**
     * 每页的条数
     */
    private int pageSize;
    /**
     * 数据
     */
    private List list;

    public static LinkPageNext createBasePage(Long nextId,Boolean isLast,int pageSize,List list){
        LinkPageNext listLinkPageNext = new LinkPageNext();
        listLinkPageNext.setIsLast(isLast);
        listLinkPageNext.setList(list);
        listLinkPageNext.setNextId(nextId);
        listLinkPageNext.setPageSize(pageSize);
        return listLinkPageNext;

    }

}
