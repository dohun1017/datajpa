package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpa.entity.Member;

import java.util.Objects;

@Data
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return Objects.equals(getId(), memberDto.getId()) && Objects.equals(getUsername(), memberDto.getUsername()) && Objects.equals(getTeamName(), memberDto.getTeamName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getTeamName());
    }
}
