package com.bulletin.bulletin;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.Comment;
import com.bulletin.bulletin.service.ArticleService;
import com.bulletin.bulletin.service.CommentService;
import com.bulletin.bulletin.service.Page;
import com.bulletin.bulletin.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

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
				"comment1", "comment2", "comment3"
		};
		String[] childCommentList = {
				"childComment1", "childComment2", "childComment3"
		};

		userService.create(userId);

		for(String[] article: articleList) {
			articleService.create(userId, article[0], article[1]);
		}

		List<Article> articlePage = articleService.getPage(0, 2);

		assertEquals(2, articlePage.size());
		assertEquals("title3", articlePage.get(0).title);
		assertEquals("title2", articlePage.get(1).title);

		Article targetArticle = articlePage.get(0);

		for(String commentBody: commentList) {
			commentService.create(commentBody, userId, targetArticle.id, null);
		}

		LocalDateTime minDate = LocalDateTime.of(2000, 1, 1, 0, 0);
		UUID minId = UUID.fromString("00000000-0000-0000-0000-000000000000");

		Page<Comment> commentPage = commentService.getPage(targetArticle.id, null, minDate, minId, 2);

		assertEquals(false, commentPage.isLast);
		assertEquals(2, commentPage.content.size());
		assertEquals("comment1", commentPage.content.get(0).body);
		assertEquals("comment2", commentPage.content.get(1).body);

		Comment targetComment = commentPage.content.get(0);

		for(String body: childCommentList) {
			commentService.create(body, userId, targetArticle.id, targetComment.id);
		}

		Page<Comment> childCommentPage = commentService.getPage(targetArticle.id, targetComment.id, minDate, minId, 4);

		assertEquals(true, childCommentPage.isLast);
		assertEquals(3, childCommentPage.content.size());
		assertEquals("childComment1", childCommentPage.content.get(0).body);
		assertEquals("childComment2", childCommentPage.content.get(1).body);
		assertEquals("childComment3", childCommentPage.content.get(2).body);
	}

}
