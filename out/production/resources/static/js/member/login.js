function login(){
    $.ajax({
        url : "/member/authmailcheck",
        data : { "mid" : $("#mid").val() },
        success: function(re){
            if( re == 1){
                $("#loginform").submit();   // .submit() : form 전송
            }else if(re == 2 || re == 3){
                alert("간편 로그인 이용해주세요.");
            }else{
                alert("이메일 인증 후 로그인이 가능합니다.");
            }
        }

    });
}