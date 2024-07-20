package refooding.api.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    // 생성 시간
    // Entity가 생성되어 저장될 때 시간을 자동 저장
    @CreatedDate
    @Column(updatable = false) // 생성 시간이므로 수정 불가능
    private LocalDateTime createdDate;

    // 수정 시간
    // 조회한 Entity 값을 변경할 때 시간을 자동 저장
    @LastModifiedDate
    private LocalDateTime modifiedDate;

}