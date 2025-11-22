package Xthon.gAIde.domain.entity;

import Xthon.gAIde.util.common.StringListConverter; // ⬅️ 새로 만든 컨버터 임포트
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "document")
public class Document {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> keywords;

    @Column(columnDefinition = "TEXT")
    private String topicDesc;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Document(Long memberId, String title, Category category, List<String> keywords, String topicDesc) {
        this.memberId = memberId;
        this.title = title;
        this.category = category;
        this.keywords = keywords;
        this.topicDesc = topicDesc;
    }

    public void updateTitle(String title) {this.title = title;}
    public void updateContent(String content) {
        this.content = content;
    }
    public void updateCategory(Category category) {
        this.category = category;
    }
    public void updateKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}