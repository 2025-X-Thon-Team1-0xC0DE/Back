package Xthon.gAIde.repository;

import Xthon.gAIde.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    // 기본 CRUD 제공

    // 내 글 목록 조회
    List<Document> findAllByMemberIdOrderByIdDesc(Long memberId);


}