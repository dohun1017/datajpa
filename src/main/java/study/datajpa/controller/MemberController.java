package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * 도메일 클래스 컨버터가 중간에 동작하여 회원 엔티티 객체를 반환한다.
     *
     * 트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영되지 않는다.
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /**
     * localhost:8080/members?page=0&size=3&sort=id,desc&sort=username@param pageable
     *
     * default 지정 가능 : public Page<Member> list(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
     */
    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {

        return memberRepository.findAll(pageable);
    }

    /**
     * Page 1부터 시작하기
     *  - 스프링 데이터는 Page를 0부터 시작한다.
     *
     *  1. Pageable, Page를 파라미터와 응답 값으로 사용하지 않고, 직접 클래스를 만들어 처리한다.
     *     직접 PageRequest(Pageable 구현체)를 생성하여 리포지토리에 넘긴다. (응답값도 Page 대신 직접 클래스를 만들어 제공해야 한다.)
     *  2. 'spring.data.web.pageable.one-indexed-parameters' true로 설정한다.
     *     이 방법은 web에서 page파라미터를 -1 처리 할 뿐이며, 응답값인 Page에 모두 0페이지 인덱스를 사용하는데 한계가 있다.
     */
    @GetMapping("/members2")
    public Page<MemberDto> dtoList(@PageableDefault(size = 10, sort = "id") Pageable pageable) {

        return memberRepository.findAll(pageable).map(MemberDto::new);
    }

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
