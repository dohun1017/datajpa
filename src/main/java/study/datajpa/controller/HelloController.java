package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MemberRepository memberRepository;

    @GetMapping("/hello")
    @Transactional
    public String hello() {
        memberRepository.save(new Member("testMember"));
        return "hello";
    }
}
