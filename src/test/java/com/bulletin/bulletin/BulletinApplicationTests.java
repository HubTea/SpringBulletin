package com.bulletin.bulletin;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.service.ArticleService;
import com.bulletin.bulletin.service.CommentService;
import com.bulletin.bulletin.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BulletinApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	ArticleService articleService;

	@Autowired
	CommentService commentService;

	@Test
	void test() throws Exception{
		String userId = "spring";
		String[][] articleList = {
				{"title1", "body1"},
				{"title2", "body2"},
				{"title3", "body3"}
		};
		String[] commentList = {
				"comment1", "comment2"
		};

		userService.create(userId);

		for(String[] article: articleList) {
			articleService.create(userId, article[0], article[1]);
		}

		List<Article> articlePage = articleService.getPage(0, 2);

		assertEquals(2, articlePage.size());
		assertEquals("title3", articlePage.get(0).title);
		assertEquals("title2", articlePage.get(1).title);
	}

}
