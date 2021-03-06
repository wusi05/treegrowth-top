package top.treegrowth.single.service;

import top.treegrowth.model.req.PagePure;
import top.treegrowth.model.req.PagesReq;
import top.treegrowth.model.res.ItemRes;
import top.treegrowth.model.res.PageDetail;
import top.treegrowth.model.res.PageRes;

/**
 * @author wusi
 * @version 2017/4/1 7:19.
 */
public interface IPageService {

    PageDetail createPage(PagePure pagePure);

    PageRes<PageDetail> getPagesBetween(PagesReq pagesReq);

    ItemRes<Boolean> deleteBy(String userId, String pageId);

    ItemRes<PageDetail> getPageDetailBy(String userId, String pageId);
}
