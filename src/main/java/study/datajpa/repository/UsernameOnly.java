package study.datajpa.repository;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    /**
     * @Value - OpenProjection: 엔티티 전체 조회
     * CloseProjection: 해당 value만 조회
     */
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
