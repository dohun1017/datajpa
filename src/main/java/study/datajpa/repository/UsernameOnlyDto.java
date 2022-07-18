package study.datajpa.repository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class UsernameOnlyDto {

    private final String username;
}
