package com.bj58.wenda;

import com.bj58.wenda.util.MailSender;
import com.bj58.wenda.webmagic.ZhihuPageProcessor;
import com.bj58.wenda.webmagic.ZhihuPipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Spider;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WendaApplicationTests {

	@Autowired
	MailSender mailSender;

	@Test
	public void contextLoads() {
		Map<String, Object> model = new HashMap<>();
		model.put("username", "star");
		//System.out.println(mailSender.sendMailTest("296536788@qq.com", "test", "/mails/login_exception"));
		System.out.println(mailSender.sendWithHTMLTemplate("296536788@qq.com", "hello", "/mails/login_exception", model));
	}

	@Test
	public void startSpider() {
		Spider.create(new ZhihuPageProcessor()).addUrl("https://www.zhihu.com/search?type=content&q=%E8%AE%A1%E7%AE%97%E6%9C%BA").addPipeline(new ZhihuPipeline()).run();
	}

}
