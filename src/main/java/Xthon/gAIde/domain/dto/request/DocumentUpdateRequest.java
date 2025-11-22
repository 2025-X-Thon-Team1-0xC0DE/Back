package Xthon.gAIde.domain.dto.request;

import Xthon.gAIde.domain.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DocumentUpdateRequest(
        @JsonProperty("doc_id") Long docId, // JSON엔 있지만 URL로 받으므로 무시 가능하지만 에러 방지용
        Category category,
        String title,
        List<String> keywords,
        String description,
        @JsonProperty("user_text") // 핵심: JSON의 user_text를 받음
        String userText
) {}