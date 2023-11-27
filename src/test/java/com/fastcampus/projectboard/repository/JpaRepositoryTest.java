package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest // Transaction 이 걸려있어서 기본값으로 rollback 이 동작함
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {

        // Given

        // When
        List<Article> articles = articleRepository.findAll();
        // Then
        assertThat(articles).isNotNull().hasSize(0);
    }

    @DisplayName("insert test")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {

        // Given
        long previousCount = articleRepository.count();

        // When
        articleRepository.save(Article.of("new article title", "new article content", "new hashtag"));
        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update test")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {

        // Given
        articleRepository.save(Article.of("new article title", "new article content", "new hashtag"));

        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // When
        Article savedArticle = articleRepository.save(article);

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }
}