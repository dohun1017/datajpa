package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

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

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);           //컬렉션
    Member findMemberByUsername(String username);               //단건
    Optional<Member> findOptionalByUsername(String username);   //단건 Optional

    /**
     * Slice: limit + 1의 갯수만 조회하여 뒤의 페이지가 있는지 없는지만 확인
     * Page: 페이징 기능 전부 사용 가능
     *
     * 쿼리가 복잡해질때는 countQuery를 따로 작성 해주는 것이 좋음.
     *      따로 작성하지 않으면 countQuery도 join을 함.
     *
     * @Query(value = "select m from Member m left join m.team t",
     *      countQuery = "select count(m) from Member m")
     */
    Page<Member> findByAge(int age, Pageable pageable);
}
