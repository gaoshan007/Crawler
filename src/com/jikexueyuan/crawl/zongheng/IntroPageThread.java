package com.jikexueyuan.crawl.zongheng;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jikexueyuan.crawl.zongheng.db.ZonghengDB;
import com.jikexueyuan.crawl.zongheng.model.NovelChapterModel;
import com.jikexueyuan.crawl.zongheng.model.NovelInfoModel;

public class IntroPageThread extends Thread {
	private boolean flag = false;

	public IntroPageThread(String name) {
		super(name);

	}

	@Override
	public void run() {
		flag = true;

		try {
			ZonghengDB db = new ZonghengDB();
			while(flag) {
				String url = db.getRandIntroPageUrl(1);
				if (url != null) {
					IntroPage intro = new IntroPage(url);
					NovelInfoModel bean = intro.analyzer();
					if (bean != null) {
						ChapterPage chapterPage = new ChapterPage(
								bean.getChapterListUrl());
						List<NovelChapterModel> chapters = chapterPage.analyzer();
						bean.setChapterCount(chapters == null ? 0 : chapters.size());
						db.updateNovelInfo(bean);
						db.saveNovelChapter(chapters, bean.getId());
					}
					TimeUnit.MILLISECONDS.sleep(500);
				} else {
					TimeUnit.MILLISECONDS.sleep(1000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.run();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IntroPageThread thread = new IntroPageThread("novelinfo");
		thread.start();
	}
}
