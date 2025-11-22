package Xthon.gAIde.controller;

import Xthon.gAIde.domain.dto.response.DocumentFeedbackResponse;
import Xthon.gAIde.domain.dto.response.DocumentResponse;
import Xthon.gAIde.domain.dto.response.DocumentSaveResponse;
import Xthon.gAIde.domain.dto.request.DocumentCreateRequest;
import Xthon.gAIde.domain.dto.request.DocumentUpdateRequest;
import Xthon.gAIde.service.DocumentService;
import Xthon.gAIde.util.common.CommonResponseDto;
import Xthon.gAIde.util.common.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import Xthon.gAIde.domain.dto.response.DocumentListResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // [GET] 마이 페이지
    // URL: /api/documents
    @GetMapping
    public CommonResponseDto<List<DocumentListResponse>> getMyDocuments(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return CommonResponseDto.ok(documentService.getMyDocuments(user.getUserId()));
    }

    /**
     * [POST] 글 생성
     * URL: /api/documents
     * 설명: 초기 세팅(제목, 키워드 등)을 저장하고 doc_id를 반환
     */
    @PostMapping
    public CommonResponseDto<Long> createDocument(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody DocumentCreateRequest requestDto
    ) {
        return CommonResponseDto.ok(documentService.createDocument(user.getUserId(), requestDto));
    }
    /**
     * [GET] 글 조회 (작성 화면 진입)
     * URL: /api/documents/{docId}
     * 설명: 저장된 글 내용과 설정을 불러옴
     */
    @GetMapping("/{docId}")
    public CommonResponseDto<DocumentResponse> getDocument(@PathVariable Long docId) {
        DocumentResponse response = documentService.getDocument(docId);
        return CommonResponseDto.ok(response);
    }

    /**
     * [PATCH] 글 저장
     * URL: /api/documents/{docId}
     * 설명: 에디터 내용을 저장하고 간단한 평가 메시지를 반환
     */
    @PatchMapping("/{docId}")
    public CommonResponseDto<DocumentSaveResponse> updateDocument(
            @PathVariable Long docId,
            @RequestBody DocumentUpdateRequest requestDto
    ) {
        // 서비스 로직 수행
        DocumentSaveResponse response = documentService.updateDocument(docId, requestDto);

        // 응답: { "data": { "eval": "...", "msg": "..." } }
        return CommonResponseDto.ok(response);
    }

    @PostMapping("{docId}/feedback")
    public CommonResponseDto<DocumentFeedbackResponse> outlineDocument(){

        DocumentFeedbackResponse response = null;
        return CommonResponseDto.ok(response);
    }
}