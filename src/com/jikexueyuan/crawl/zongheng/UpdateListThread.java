package com.jikexueyuan.crawl.zongheng;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jikexueyuan.crawl.zongheng.db.ZonghengDB;

public class UpdateListThread extends Thread{
	
	private boolean flag = false;
	private String url = "";
	private int frequency;
	
	public UpdateListThread(String name, String url, int frequency) {
		super(name);
		this.url = url;
		this.frequency = frequency;
	}
	
	public void run() {
		flag = true;
		
		ZonghengDB db = new ZonghengDB();
		while(flag) {
			
			try {
				UpdateList updateList = new UpdateList(this.url);
				List<String> urls = updateList.getPageUrl(true);
				db.saveInfoUrls(urls);
				TimeUnit.SECONDS.sleep(this.frequency);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.run();
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UpdateListThread thread = new UpdateListThread("lll", "http://book.zongheng.com/store.html", 30);
		thread.start();
	}

}
