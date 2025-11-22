package Xthon.gAIde.domain.dto.request;

import Xthon.gAIde.domain.entity.Category;
import java.util.List;

// 글 생성 요청 데이터
public record DocumentCreateRequest(
        Category category,
        String title,
        List<String> keywords,
        String description
) {}