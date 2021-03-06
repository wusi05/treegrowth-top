package top.treegrowth.single.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.treegrowth.es.service.IElasticService;
import top.treegrowth.model.elastic.IndexInfo;
import top.treegrowth.model.entity.Diary;
import top.treegrowth.model.entity.Page;
import top.treegrowth.model.req.PagePure;
import top.treegrowth.model.req.PagesReq;
import top.treegrowth.model.res.ItemRes;
import top.treegrowth.model.res.PageDetail;
import top.treegrowth.model.res.PageRes;
import top.treegrowth.single.dao.mapper.DiaryMapper;
import top.treegrowth.single.dao.mapper.PageMapper;
import top.treegrowth.single.service.IPageService;
import top.treegrowth.single.serviceimpl.exception.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static top.treegrowth.common.utils.Conditions.checkState;
import static top.treegrowth.common.utils.Generator.uuid;
import static top.treegrowth.es.ElasticServiceImpl.INDEX;
import static top.treegrowth.es.ElasticServiceImpl.TYPE;

/**
 * @author wusi
 * @version 2017/4/1 7:25.
 */
@Service
public class PageServiceImpl implements IPageService {

    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private IElasticService<Page> elasticService;

    public PageDetail getPageDetail(Page page, String userId) {
        PageDetail pageDetail = new PageDetail();
        pageDetail.setId(page.getId());
        pageDetail.setName(page.getName());
        pageDetail.setContent(page.getContent());
        pageDetail.setDiaryId(page.getDiaryId());
        pageDetail.setWeather(page.getWeather());
        pageDetail.setMind(page.getMind());
        pageDetail.setCreateTime(page.getCreateTime());
        pageDetail.setDiaryName(diaryMapper.getDiaryBy(page.getDiaryId(), userId).getName());
        if (!StringUtils.isEmpty(userId)) {
            fillInWithUserState(pageDetail, userId);
        }
        return pageDetail;
    }


    @Transactional
    @Override
    public PageDetail createPage(PagePure pagePure) {

        Diary diary = diaryMapper.getDiaryBy(pagePure.getDiaryId(), pagePure.getAuthorId());
        checkState(diary != null, () -> new NotFoundException("没有找到对应日记"));
        Page page = pagePure.toPage();
        page.setId(uuid());
        page.setCreateTime(new Date());
        page.setAuthorId(pagePure.getAuthorId());
        // 防止富文本编辑器输入过多的空格
        page.setContent(page.getContent().trim());
        page.setText(page.getText().trim());
        pageMapper.createPage(page);

//        sender.send(MessageBuilder.withPayload(page)
//                .setHeader(KafkaHeaders.TOPIC, TOPIC)
//                .build());
        IndexInfo indexInfo = new IndexInfo(INDEX, TYPE, page.getId());
        // 查询的时候用纯文本，所以将content字段(带有html标签)置空，用纯文本字段text
        page.setContent("");
        elasticService.index(page, indexInfo);

        return getPageDetail(page, pagePure.getAuthorId());
    }

    @Override
    public PageRes<PageDetail> getPagesBetween(PagesReq pagesReq) {
        PageHelper.startPage(pagesReq.getPageNum(), pagesReq.getPageSize());
        List<Page> pages = pageMapper.getPagesBetween(pagesReq.getStartTime(), pagesReq.getEndTime(), pagesReq.getDiaryId(), pagesReq.getUserId());
        checkState(pages != null, () -> new NotFoundException("没有查询到该日记下的列表"));
        PageInfo<Page> pageInfo = new PageInfo<>(pages);
        List<Page> pageList = pageInfo.getList();

        List<PageDetail> pageDetails = pageList.stream()
                .map(page -> getPageDetail(page, pagesReq.getUserId()))
                .collect(Collectors.toList());
        return new PageRes<>(pageDetails, pageInfo.getTotal(), pageInfo.isIsLastPage());
    }

    @Override
    public ItemRes<Boolean> deleteBy(String userId, String pageId) {
        Page page = pageMapper.getPageBy(userId, pageId);
        checkState(page != null, () -> new NotFoundException("没有查到相关每天记录"));
        pageMapper.deleteBy(userId, pageId);
        return new ItemRes<>(true);
    }

    @Override
    public ItemRes<PageDetail> getPageDetailBy(String userId, String pageId) {
        Page page = pageMapper.getPageBy(userId, pageId);
        checkState(page != null, () -> new NotFoundException("没有查到相关每天记录"));
        return new ItemRes<>(getPageDetail(page, userId));
    }

    private void fillInWithUserState(PageDetail pageDetail, String userId) {

    }
}
