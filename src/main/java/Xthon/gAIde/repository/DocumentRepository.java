package Xthon.gAIde.repository;

import Xthon.gAIde.domain.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    // 기본 CRUD 제공
}