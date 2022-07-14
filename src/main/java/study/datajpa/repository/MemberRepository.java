package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 스프링 데이터 JPA가 제공하는 쿼리 메소드 기능
     * <p>
     * 조회: find...By, read...By, query...By, get...By
     * COUNT: count...By (반환타입 long)
     * EXISTS: exists...By (반환타입 boolean)
     * 삭제: delete...By, remove...By (반환타입 long)
     * DISTINCT: findDistinct, findMemberDistinctBy
     * LIMIT: findFirstN, findFirst, findTop, findTopN - N개의 갯수만큼 limit
     * <p>
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
     * <p>
     * 쿼리가 복잡해질때는 countQuery를 따로 작성 해주는 것이 좋음.
     * 따로 작성하지 않으면 countQuery도 join을 함.
     *
     * @Query(value = "select m from Member m left join m.team t",
     * countQuery = "select count(m) from Member m")
     */
    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * @Modifying 어노테이션이 있어야 데이터 변경이 일어남.
     * 해당 어노테이션이 없을 때 데이터 변경 쿼리를 날리면 ExceptionError 발생
     * <p>
     * 벌크 연산 이후 영속성 컨텍스트 초기화 하기
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query(value = "select m From Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    @Query(value = "select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("team"))
    List<Member> findByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
