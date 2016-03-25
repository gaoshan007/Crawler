package com.jikexueyuan.crawl.zongheng;

import java.util.concurrent.TimeUnit;

import com.jikexueyuan.crawl.zongheng.db.ZonghengDB;
import com.jikexueyuan.crawl.zongheng.model.NovelChapterModel;
import com.jikexueyuan.crawl.zongheng.model.NovelReadModel;

public class ReadPageThread extends Thread{
	
	private boolean flag = false;

	public ReadPageThread(String name) {
		super(name);
	}
	
	@Override
	public void run() {
		flag = true;
		ZonghengDB db = new ZonghengDB();
		while(flag) {
			try {
				NovelChapterModel chapter = db.getRandChapter(1);
				if(chapter != null) {
					ReadPage readPage = new ReadPage(chapter.getUrl());
					NovelReadModel novel = readPage.analyzer();
					if(novel == null) {
						continue;
					}
					novel.setChapterId(chapter.getChapterId());
					novel.setChapterTime(chapter.getChapterTime());
					db.saveNovelRead(novel);
					db.updateChapterState(chapter.getId(), 0);
					TimeUnit.MILLISECONDS.sleep(500);
				} else {
					TimeUnit.MILLISECONDS.sleep(1000);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		super.run();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadPageThread thread = new ReadPageThread("readPage");
		thread.start();
	}

}
