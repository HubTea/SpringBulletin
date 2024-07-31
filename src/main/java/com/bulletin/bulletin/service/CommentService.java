package com.bulletin.bulletin.service;

import com.bulletin.bulletin.entity.Article;
import com.bulletin.bulletin.entity.Comment;
import com.bulletin.bulletin.entity.User;
import com.bulletin.bulletin.repository.CommentRepository;
import com.bulletin.bulletin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bulletin.bulletin.common.Constant.*;


@Service
@Transactional
public class CommentService {

    public static class Cursor {

        public UUID parentId;

        public LocalDateTime time;

        public UUID id;

        //parentId는 nullable
        public Cursor(UUID parentId, LocalDateTime time, UUID id) {
            this.parentId = parentId;
            this.time = time;
            this.id = id;
        }
    }

    public static class TreePageEntry {

        public Integer depth;

        public Comment comment;

        TreePageEntry(Comment comment, Integer depth) {
            this.comment = comment;
            this.depth = depth;
        }
    }

    public static class TreePage {

        public List<Cursor> cursorList;

        public List<TreePageEntry> content;

        TreePage(List<TreePageEntry> content, List<Cursor> cursorList) {
            this.content = content;
            this.cursorList = cursorList;
        }
    }

    class TreePageBuilder {

        private List<TreePageEntry> treePage;

        private List<Cursor> nextRequestCursorList;

        private final List<Cursor> cursorList;

        private final UUID articleId;
        private final Integer pageSize;

        TreePageBuilder(UUID articleId, List<Cursor> cursorList, Integer pageSize) {
            this.articleId = articleId;
            this.cursorList = cursorList;
            this.pageSize = pageSize;
        }

        //build 호출 이전에는 null 반환
        List<TreePageEntry> getTreePage() {
            return treePage;
        }

        //build 호출 이전에는 null 반환
        List<Cursor> getNextRequestCursorList() {
            return nextRequestCursorList;
        }

        void build() {
            treePage = new ArrayList<>();
            while(cursorList.size() > 1) {
                build(cursorList.size() - 1);
                if(isComplete()) {
                    return;
                }
                cursorList.remove(cursorList.size() - 1);
            }

            build(0);
            nextRequestCursorList = new ArrayList<>(cursorList);
        }

        private void build(Integer depth) {
            Cursor cursor = cursorList.get(depth);

            if(isComplete()) {
                nextRequestCursorList = new ArrayList<>(cursorList);
                return;
            }

            Page<Comment> page = CommentService.this.getPage(articleId, cursor.parentId, cursor.time, cursor.id, pageSize);
            for(Comment comment: page.content) {
                treePage.add(new TreePageEntry(comment, depth));
                cursor.time = comment.getCreatedAt();
                cursor.id = comment.getId();

                cursorList.add(new Cursor(comment.getId(), MIN_DATE, MIN_UUID));
                build(depth + 1);
                if(isComplete()) {
                    return;
                }
                cursorList.remove(cursorList.size() - 1);
            }
        }

        private Boolean isComplete() {
            return treePage.size() == pageSize;
        }
    }

    UserRepository userRepository;

    CommentRepository commentRepository;

    UserService userService;

    ArticleService articleService;

    CommentService(
            UserRepository userRepository,
            CommentRepository commentRepository,
            UserService userService,
            ArticleService articleService
    ) {
        this.articleService = articleService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    //parentId는 nullable
    public void create(String body, String writerId, UUID articleId, UUID parentId) throws Exception {
        User writer = userService.findExistUser(writerId);
        Article article = articleService.findExistArticle(articleId);
        Comment parent = null;

        if(parentId != null) {
            Optional<Comment> optionalParent = commentRepository.findById(parentId);

            if(optionalParent.isEmpty()) {
                throw new Exception();
            }
            parent = optionalParent.get();
        }

        Comment comment = new Comment(body, writer, article, parent);
        commentRepository.save(comment);
        article.setCommentVersion(article.getCommentVersion() + 1);
    }

    //parentId는 nullable
    public Page<Comment> getPage(UUID articleId, UUID parentId, LocalDateTime cursorCreatedAt, UUID cursorId, Integer pageSize) {
        List<Comment> page = null;

        if(parentId == null) {
            page = commentRepository.findByArticleId(articleId, cursorCreatedAt, cursorId, pageSize + 1);
        }
        else {
            page = commentRepository.findByArticleIdAndParentId(articleId, parentId, cursorCreatedAt, cursorId, pageSize + 1);
        }

        Boolean isLast = (page.size() != pageSize + 1);
        page = page.subList(0, Math.min(pageSize, page.size()));
        return new Page<>(page, isLast);
    }

    public TreePage getTreePage(UUID articleId, List<Cursor> cursorList, Integer pageSize) {
        assert !cursorList.isEmpty();

        TreePageBuilder builder = new TreePageBuilder(articleId, cursorList, pageSize);

        builder.build();
        return new TreePage(builder.getTreePage(), builder.getNextRequestCursorList());
    }

    //가장 오래된 댓글부터 조회
    public TreePage getTreePage(UUID articleId, Integer pageSize) {
        List<Cursor> cursorList = new ArrayList<>();
        cursorList.add(new Cursor(null, MIN_DATE, MIN_UUID));
        return getTreePage(articleId, cursorList, pageSize);
    }
}
