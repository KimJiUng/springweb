package ezenweb.dto;

import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.Role;
import lombok.*;

import java.util.Map;

@Getter@Setter@ToString
@NoArgsConstructor@AllArgsConstructor
@Builder
public class OauthDto {


    private String mid;     // 아이디  [이메일에서 아이디만 추출 vs 문자열 난수]
    private String mname;   // 이름
    private String memail;  // 이메일
    private String registrationId;  // 클라이언트 id
    private Map<String,Object> attributes;  // 인증결과
    private String nameAttributeKey;    // 회원정보 요청 키

    // 클라이언트 구분 메소드 [ 네이버 or 카카오 ]
    public static OauthDto of(String registrationId, String nameAttributeKey, Map<String,Object> attributes){
        if(registrationId.equals("naver")){ // 네이버이면
            return ofnaver(registrationId,nameAttributeKey,attributes);
        }else if(registrationId.equals("kakao")){   // 카카오이면
            return ofkakao(registrationId,nameAttributeKey,attributes);
        }else{  // 없으면
            return null;
        }

    }

    // 네이버 로그인시 [만약에 registrationid = naver 이면]
    private static OauthDto ofnaver(String registrationId,String nameAttributeKey, Map<String,Object> attributes){
        Map<String,Object> response = (Map<String, Object>) attributes.get(nameAttributeKey);
        String mid = ( (String)response.get("email")).split("@")[0];

        return OauthDto.builder()
                .mid(mid)
                .mname((String) response.get("name"))
                .memail((String) response.get("email"))
                .nameAttributeKey(nameAttributeKey)
                .registrationId(registrationId)
                .attributes(attributes)
                .build();
    }

    // 카카오 로그인시 [만약에 registrationid = kakao 이면]
    private static OauthDto ofkakao(String registrationId,String nameAttributeKey, Map<String,Object> attributes){
        Map<String,Object> kakao_account = (Map<String, Object>) attributes.get(nameAttributeKey);
        Map<String,Object> profile = (Map<String, Object>) kakao_account.get("profile");
        String mid = ( (String)kakao_account.get("email")).split("@")[0];

        return OauthDto.builder()
                .mid(mid)
                .mname((String) profile.get("nickname"))
                .memail((String) kakao_account.get("email"))
                .nameAttributeKey(nameAttributeKey)
                .registrationId(registrationId)
                .attributes(attributes)
                .build();
    }




    @Builder
    public OauthDto(Map<String,Object> attributes, String nameAttributeKey, String mname, String memail) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.mname = mname;
        this.memail = memail;
    }

    // oauth 정보 entity 변경
    public MemberEntity toentity(){
        return MemberEntity.builder()
                .mid(this.mid)
                .mname(this.mname)
                .memail(this.memail)
                .oauth(this.registrationId)
                .role(Role.MEMBER)
                .build();
    }


}
