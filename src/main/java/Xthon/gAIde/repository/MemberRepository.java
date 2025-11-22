package Xthon.gAIde.repository;

import Xthon.gAIde.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByLoginId(String loginId);
    Optional<MemberEntity> findByLoginId(String loginId);
}