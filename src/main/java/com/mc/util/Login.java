package com.mc.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component("login")
public class Login {
	private static Logger log = LoggerFactory.getLogger(Login.class);
	
//	public static void main(String[] args) {
//		int a = 10;
//		int b = 0;
//		try{
//			int c = a/b;
//		}catch(Exception e){
//			log.error("error:",e);
//		}
//	}
	
//	@Scheduled(fixedDelay=5000)
//	public void test(){
//		log.info("时间："+new Date().getTime());
//	}
	
	@Scheduled(cron="0 20 8 ? * *")
	public void check() throws Exception {
		Random r = new Random();
		Thread.sleep((r.nextInt(600)+1)*1000);
		String url = "https://passport.eteams.cn";
		String url_login = "https://passport.eteams.cn/login";

		Map<String, String> cookies = new HashMap<String, String>();
		Map<String, String> params = new HashMap<String, String>();
		Connection conn = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
		Response index = conn.execute();
		cookies = index.cookies();
		Document doc = index.parse();
		
		String lt = doc.select("input[name='lt']").attr("value");
		String execution = doc.select("input[name='execution']").attr("value");
		String j_pcClient = doc.select("input[name='j_pcClient']").attr("value");
		String _eventId = doc.select("input[name='_eventId']").attr("value");
		String isApplyed = doc.select("input[name='isApplyed']").attr("value");
		String username = "18565155401";
		String password = "zhangdd123";
		
		params.put("lt", lt);
		params.put("execution", execution);
		params.put("j_pcClient", j_pcClient);
		params.put("_eventId", _eventId);
		params.put("isApplyed", isApplyed);
		params.put("username", username);
		params.put("password", password);
		Response response = Jsoup.connect(url_login).ignoreContentType(true).method(Method.POST).cookies(cookies).data(params).execute();
		cookies = response.cookies();
		
		conn = Jsoup.connect("https://www.eteams.cn/timecard/check.json");
		conn.header("Host", "www.eteams.cn");
		conn.header("Origin", "https://www.eteams.cn");
		conn.header("Pragma", "no-cache");
		conn.header("Referer", "https://www.eteams.cn/");
		conn.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
		conn.header("X-Requested-With", "XMLHttpRequest");
		
		Map<String, String> data = new HashMap<String, String>();
		String op = "CHECKIN";
		data.put("type", op);
		Response execute = conn.ignoreContentType(true).cookies(cookies).method(Method.POST).data(data).execute();
		log.info(execute.body());
	}
}
