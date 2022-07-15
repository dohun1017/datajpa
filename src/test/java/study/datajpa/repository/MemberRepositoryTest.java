package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EntityManager em;

    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");

        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertEquals(member, findMember);
    }

    @Test
    public void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when - then

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertEquals(member1, findMember1);
        assertEquals(member2, findMember2);

        //리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertEquals(2, members.size());

        //카운트 검증
        long count = memberRepository.count();
        assertEquals(2, count);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertEquals(0, deletedCount);

    }

    @Test
    public void findByUsernameAndGraterHen() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        //then
        assertEquals("AAA", result.get(0).getUsername());
        assertEquals(20, result.get(0).getAge());
    }

    @Test
    public void testNamedQuery() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findUser(member2.getUsername(), member2.getAge());

        //then
        assertEquals(member2.getUsername(), members.get(0).getUsername());
        assertEquals(member2, members.get(0));
    }

    @Test
    public void findUsernameList() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

//        List<String> usernameList = Stream.<String>builder()
//                .add(member1.getUsername())
//                .add(member2.getUsername())
//                .build()
//                .collect(Collectors.toList());
        List<String> usernameList = Arrays.asList(member1.getUsername(), member2.getUsername());

        //when
        List<String> findUsernameList = memberRepository.findUsernameList();


        //then
        assertEquals(usernameList, findUsernameList);
    }

    @Test
    public void findMemberDto() throws Exception {
        //given
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member memberA = new Member("AAA", 10);
        memberA.setTeam(teamA);
        memberRepository.save(memberA);

        MemberDto memberDto = new MemberDto(memberA.getId(), memberA.getUsername(), teamA.getName());

        //when
        List<MemberDto> memberDtoList = memberRepository.findMemberDto();

        //then
        assertEquals(memberDto, memberDtoList.get(0));
    }

    @Test
    public void findByNames() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = Arrays.asList(member1, member2);

        //when
        List<Member> findMembers = memberRepository.findByNames(Arrays.asList(member1.getUsername(), member2.getUsername()));

        //then
        assertEquals(members, findMembers);

    }

    @Test
    public void returnTypeTest() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = Arrays.asList(member1);

        //when
        List<Member> memberList = memberRepository.findListByUsername(member1.getUsername());
        Member findMember = memberRepository.findMemberByUsername(member1.getUsername());
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername(member1.getUsername());

        //then
    }

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int memberCount = 5;
        int limit = 3;
        int pageNum = 0;
        int totalPageCount = 2;

        PageRequest pageRequest = PageRequest.of(pageNum, limit, Sort.Direction.DESC, "username");

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        List<Member> content = page.getContent();

        /**
         * API에서 entity를 직접 내보내는 것은 상당히 위험함하다.
         *      page.map의 함수를 사용하여 Dto 객체로 변환 후 내보내야 한다.
         */
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        //Page와 Slice 모두 가지고 있는 기능
        assertEquals(limit, content.size());
        assertEquals(pageNum, page.getNumber());
        assertTrue(page.isFirst());
        assertTrue(page.hasNext());

        /**
         * Slice 에는 없는 기능
         * limit + 1 의 쿼리를 보냄으로써 뒤의 데이터가 있는지 정도만 확인함.
         */
        assertEquals(memberCount, page.getTotalElements());
        assertEquals(totalPageCount, page.getTotalPages());
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 50));
        teamRepository.save(new Team("teamA"));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //then
        assertEquals(4, resultCount);

        /**
         * memberRepository.bulkAgePlus(20); 호출했을 때 자동으로 flush 하지만 이 때 벌크쿼리와 관련된 엔티티만 flush하게 된다.
         * 따라서 벌크 연산과 관계 없는 Entity들의 값을 저장하기 위해 flush를 호출해줘야 한다.
         *
         * @Modifying(clearAutomatically = true) 해당 내용으로 em.flush, em.clear 해결 가능.
         */
//        assertEquals(50, memberRepository.findMemberByUsername("member5").getAge());
//        em.flush();
//        em.clear();
        assertEquals(51, memberRepository.findMemberByUsername("member5").getAge());
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when

        /**
         * N + 1 문제 발생
         * 1 : select Member
         * N : select Team
         *
         * @EntityGraph(attributePaths = {"team"}) 을 이용해 해결
         *      findAll = findMemberFetchJoin = findMemberEntityGraph = findByUsername
         *
         * @NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
         *      Member Entity 에 추가
         *      @EntityGraph("Member.all")
         */
        memberRepository.findAll().forEach(m -> {
            System.out.println("member = " + m.getUsername());
            System.out.println("member.team = " + m.getTeam().getName());
        });
        em.clear();

        memberRepository.findMemberEntityGraph().forEach(m -> {
            System.out.println("member = " + m.getUsername());
            System.out.println("member.team = " + m.getTeam().getName());
        });
    }

    @Test
    public void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when

        /**
         * @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
         * 단순 조회용 -> 영속성 컨텍스트 X
         */
        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when

        /**
         * @Lock(LockModeType.PESSIMISTIC_WRITE)
         * select for update
         *
         * SELECT ~ FOR UPDATE를 실행하면 특정 세션이 데이터에 대해 수정을 할 때까지 LOCK이 걸려 다른 세션이 데이터에 접근할 수 없다.
         */
        List<Member> lockByUsername = memberRepository.findLockByUsername(member1.getUsername());

        em.flush();
    }

    @Test
    public void callCustom() throws Exception {
        List<Member> result = memberRepository.findMemberCustom();
    }
}