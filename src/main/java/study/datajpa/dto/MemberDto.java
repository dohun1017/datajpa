package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

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
