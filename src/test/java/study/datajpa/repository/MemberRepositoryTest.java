package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

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

}