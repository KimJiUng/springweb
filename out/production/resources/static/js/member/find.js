
function findid(){
    $.ajax({
        url : "/member/findid",
        data : {"mname" : $("#idmname").val(), "memail" : $("#idmemail").val()},
        success : function(re){
            if(re == ""){
                alert("동일한 회원 정보가 없습니다.");
            }else{
                $("#findidbox").html("회원님의 아이디 : "+re);
                $("#findidbox").css("display","block");
            }
        }

    });
}

function findpw(){

    $.ajax({
        url : "/member/findpw",
        data : {"mid" : $("#pwmid").val(), "memail" : $("#pwmemail").val()},
        success : function(re){
            if(re){
                 alert("이메일로 임시 비밀번호를 발송했습니다.");
            }else{
                alert("동일한 회원 정보가 없습니다.");
            }
        }

    });

}