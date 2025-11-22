package Xthon.gAIde.domain.dto.response;

import Xthon.gAIde.domain.entity.Document;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// 글 조회 시 반환할 데이터
public record DocumentResponse(
        @JsonProperty("doc_id") Long docId, // JSON 키값을 doc_id로 맞춤
        String category,
        String title,
        List<String> keywords,
        String description,
        String content
) {
    public static DocumentResponse from(Document doc) {
        return new DocumentResponse(
                doc.getId(),
                doc.getCategory().toString(),
                doc.getTitle(),
                doc.getKeywords(),
                doc.getTopicDesc(),
                doc.getContent()
        );
    }
}