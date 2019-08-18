package com.bj58.wenda.util;

import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean {

    public static void main(String[] args) throws Exception{

//构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
//模板所在目录，相对于当前classloader的classpath。模板的路径
        resolver.setPrefix("templates/");
//模板文件后缀
        resolver.setSuffix(".html");
//文件模式
        resolver.setTemplateMode("HTML5");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);


//渲染模板 输出静态资源的位置
        FileWriter write = new FileWriter("result.html");
        Context context = new Context();
        context.setVariable("username", "star");
        //System.out.println(templateEngine.process("mails/login_exception", context));
        MailSender mailSender = new MailSender();
        Map<String, Object> model = new HashMap<>();
        model.put("username", "star");
        mailSender.sendWithHTMLTemplate("29536788@qq.com", "test", "/mails/login_exception", model);
    }

    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    private JavaMailSenderImpl mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public boolean sendMailTest(String to, String subject, String content) {
        try {
            String nick = MimeUtility.encodeText("牛客中级课");
            InternetAddress from = new InternetAddress(nick + "<lxing722@sina.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            String result = "Hello!";
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, false);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("SuperStar");
            InternetAddress from = new InternetAddress(nick + "<lxing722@sina.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            Context context = new Context();
            context.setVariables(model);
            String result = templateEngine.process(template, context);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("lxing722@sina.com");
        mailSender.setPassword("959515356191");
        mailSender.setHost("smtp.sina.com");
        //mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        //javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
