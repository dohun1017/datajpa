package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");

        //when
        memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(member.getId());

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
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //when - then

        //단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertEquals(member1, findMember1);
        assertEquals(member2, findMember2);

        //리스트 조회 검증
        List<Member> members = memberJpaRepository.findAll();
        assertEquals(2, members.size());

        //카운트 검증
        long count = memberJpaRepository.count();
        assertEquals(2, count);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        assertEquals(0, deletedCount);

    }

    @Test
    public void findByUsernameAndGraterHen() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        //then
        assertEquals("AAA", result.get(0).getUsername());
        assertEquals(20, result.get(0).getAge());
    }

    @Test
    public void testNamedQuery() throws Exception {
        //given
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //when
        List<Member> members = memberJpaRepository.findByUsername(member2.getUsername());

        //then
        assertEquals(member2.getUsername(), members.get(0).getUsername());
        assertEquals(member2, members.get(0));
    }
}