package Xthon.gAIde.controller;

import Xthon.gAIde.domain.dto.response.DocumentResponse;
import Xthon.gAIde.domain.dto.response.DocumentSaveResponse;
import Xthon.gAIde.domain.dto.request.DocumentCreateRequest;
import Xthon.gAIde.domain.dto.request.DocumentUpdateRequest;
import Xthon.gAIde.service.DocumentService;
import Xthon.gAIde.util.common.CommonResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    /**
     * [POST] 글 생성
     * URL: /api/documents
     * 설명: 초기 세팅(제목, 키워드 등)을 저장하고 doc_id를 반환
     */
    @PostMapping
    public CommonResponseDto<Map<String, Long>> createDocument(
            @RequestBody DocumentCreateRequest requestDto,
            HttpServletRequest request
    ) {
        // 해커톤 편의상: 토큰 필터에서 memberId를 못 찾으면 1번 유저로 간주
        Long memberId = (Long) request.getAttribute("memberId");
        if (memberId == null) memberId = 1L;

        Long docId = documentService.createDocument(memberId, requestDto);

        // 응답: { "data": { "doc_id": 1 } }
        return CommonResponseDto.ok(Map.of("doc_id", docId));
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
}