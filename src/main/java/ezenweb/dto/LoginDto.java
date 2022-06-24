package ezenweb.dto;

import ezenweb.domain.member.MemberEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class LoginDto implements UserDetails { // 로그인 세션에 넣을 dto 생성

    private int mno;    // 회원번호
    private String mid; // 회원 아이디
    private String mname;   // 회원 이름
    private String mpassword;   // 회원 비밀번호
    private final Set<GrantedAuthority> authorities;    // 부여된 인증들



    public LoginDto(MemberEntity memberEntity, Collection<? extends GrantedAuthority> authorityList) {
        this.mno = memberEntity.getMno();
        this.mid = memberEntity.getMid();
        this.mname = memberEntity.getMname();
        this.mpassword = memberEntity.getMpassword();
        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorityList));
    }


    // 패스워드 반환
    @Override
    public String getPassword() {
        return this.mpassword;
    }

    // 아이디 반환
    @Override
    public String getUsername() {
        return this.mid;
    }

    // 계정 인증 만료 여부 확인 [true : 만료되지 않음]
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠겨 있는지 확인 [true : 잠겨있지 않음]
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 패스워드 만료 여부 확인 [true : 만료되지 않음]
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 사용 가능 여부 확인 [true : 사용가능]
    @Override
    public boolean isEnabled() {
        return true;
    }
}
