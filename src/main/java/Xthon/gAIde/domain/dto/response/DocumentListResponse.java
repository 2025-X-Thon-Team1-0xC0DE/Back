package Xthon.gAIde.domain.dto.response;

import Xthon.gAIde.domain.entity.Document;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DocumentListResponse(
        @JsonProperty("doc_id") Long docId,
        String category,
        String title
) {
    public static DocumentListResponse from(Document doc) {
        return new DocumentListResponse(
                doc.getId(),
                doc.getCategory().toString(),
                doc.getTitle()
        );
    }
}