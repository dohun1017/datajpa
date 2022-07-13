package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 스프링 데이터 JPA가 제공하는 쿼리 메소드 기능
     *
     * 조회: find...By, read...By, query...By, get...By
     * COUNT: count...By (반환타입 long)
     * EXISTS: exists...By (반환타입 boolean)
     * 삭제: delete...By, remove...By (반환타입 long)
     * DISTINCT: findDistinct, findMemberDistinctBy
     * LIMIT: findFirstN, findFirst, findTop, findTopN - N개의 갯수만큼 limit
     *
     * 필드명이 변경되면 메소드 이름도 변경되어야 함.
     * 그렇지 않으면 애플리케이션 실행 시 에러 발생.
     */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * NamedQuery 처럼 작동함
     * 파싱 실패시 컴파일에러
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
