package com.bulletin.bulletin;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.ArticleBody;
import com.bulletin.bulletin.entity.Comment;
import com.bulletin.bulletin.service.ArticleService;
import com.bulletin.bulletin.service.CommentService;
import com.bulletin.bulletin.service.Page;
import com.bulletin.bulletin.service.UserService;

import static com.bulletin.bulletin.common.Constant.*;
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

		List<ArticleService.PageEntry> articlePage = articleService.getPage(0, 2, userId);

		assertEquals(2, articlePage.size());
		assertEquals("title3", articlePage.get(0).article.getTitle());
		assertEquals("title2", articlePage.get(1).article.getTitle());

		Article targetArticle = articlePage.get(0).article;

		for(String commentBody: commentList) {
			commentService.create(commentBody, userId, targetArticle.getId(), null);
		}

		LocalDateTime minDate = MIN_DATE;
		UUID minId = MIN_UUID;

		Page<Comment> commentPage = commentService.getPage(targetArticle.getId(), null, minDate, minId, 2);

		assertEquals(false, commentPage.isLast);
		assertEquals(2, commentPage.content.size());
		assertEquals("comment1", commentPage.content.get(0).getBody());
		assertEquals("comment2", commentPage.content.get(1).getBody());

		Comment targetComment = commentPage.content.get(0);

		for(String body: childCommentList) {
			commentService.create(body, userId, targetArticle.getId(), targetComment.getId());
		}

		Page<Comment> childCommentPage = commentService.getPage(targetArticle.getId(), targetComment.getId(), minDate, minId, 4);

		assertEquals(true, childCommentPage.isLast);
		assertEquals(3, childCommentPage.content.size());
		assertEquals("childComment1", childCommentPage.content.get(0).getBody());
		assertEquals("childComment2", childCommentPage.content.get(1).getBody());
		assertEquals("childComment3", childCommentPage.content.get(2).getBody());

		CommentService.TreePage treeCommentPage = commentService.getTreePage(targetArticle.getId(), 3);

		assertEquals(3, treeCommentPage.cursorList.size());
		assertEquals(3, treeCommentPage.content.size());
		assertEquals("comment1", treeCommentPage.content.get(0).comment.getBody());
		assertEquals(0, treeCommentPage.content.get(0).depth);
		assertEquals("childComment1", treeCommentPage.content.get(1).comment.getBody());
		assertEquals(1, treeCommentPage.content.get(1).depth);
		assertEquals("childComment2", treeCommentPage.content.get(2).comment.getBody());
		assertEquals(1, treeCommentPage.content.get(2).depth);

		treeCommentPage = commentService.getTreePage(targetArticle.getId(), treeCommentPage.cursorList, 10);

		assertEquals(1, treeCommentPage.cursorList.size());
		assertEquals(3, treeCommentPage.content.size());
		assertEquals("childComment3", treeCommentPage.content.get(0).comment.getBody());
		assertEquals(1, treeCommentPage.content.get(0).depth);
		assertEquals("comment2", treeCommentPage.content.get(1).comment.getBody());
		assertEquals(0, treeCommentPage.content.get(1).depth);
		assertEquals("comment3", treeCommentPage.content.get(2).comment.getBody());
		assertEquals(0, treeCommentPage.content.get(2).depth);

		List<ArticleService.PageEntry> articlePageEntryList = articleService.getPage(0, 3, userId);

		assertEquals(3, articlePageEntryList.size());
		assertEquals(false, articlePageEntryList.get(0).isVisited);
		assertEquals(6, articlePageEntryList.get(0).article.getCommentVersion());
		assertEquals(false, articlePageEntryList.get(1).isVisited);
		assertEquals(false, articlePageEntryList.get(2).isVisited);

		ArticleBody articleBody = articleService.getBody(targetArticle.getId(), userId);

		assertEquals("body3", articleBody.getBody());

		articlePageEntryList = articleService.getPage(0, 3, userId);

		assertEquals(3, articlePageEntryList.size());
		assertEquals(true, articlePageEntryList.get(0).isVisited);
		assertEquals(true, articlePageEntryList.get(0).isUpdated);
		assertEquals(false, articlePageEntryList.get(1).isVisited);
		assertEquals(false, articlePageEntryList.get(2).isVisited);

		commentService.create("newComment", userId, targetArticle.getId(), null);
		articlePageEntryList = articleService.getPage(0, 3, userId);

		assertEquals(false, articlePageEntryList.get(0).isUpdated);
		assertEquals(7, articlePageEntryList.get(0).article.getCommentVersion());

		articleService.getBody(targetArticle.getId(), userId);
		articlePageEntryList = articleService.getPage(0, 3, userId);

		assertEquals(true, articlePageEntryList.get(0).isVisited);
		assertEquals(true, articlePageEntryList.get(0).isUpdated);
	}

}
