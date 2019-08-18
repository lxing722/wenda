package com.bj58.wenda.webmagic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author star
 * @date 2018/12/28 17:01
 */
@SpringBootApplication
@ServletComponentScan
public class ZhihuPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        List<String> urls = page.getHtml().xpath("div[@itemprop='zhihu:question']/meta[@itemprop='url']/@content").all();
        page.addTargetRequests(urls);
        page.putField("title", page.getHtml().xpath("h1[@class='QuestionHeader-title']/text()").toString());
        page.putField("content", page.getHtml().xpath("span[@class='RichText ztext']/text()").toString());
        page.putField("url", page.getUrl().toString());
        page.putField("items", page.getHtml().xpath("div[@class='ContentItem AnswerItem']").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String args[]) {
        Spider.create(new ZhihuPageProcessor()).addUrl("https://www.zhihu.com/search?type=content&q=%E8%AE%A1%E7%AE%97%E6%9C%BA").addPipeline(new ZhihuPipeline()).run();
    }
}
