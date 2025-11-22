package Xthon.gAIde.domain.dto.request.AI;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EvaluationRequest(
        String category,
        List<String> keywords,
        String description,
        @JsonProperty("user_text")
        String userText
) {
}
