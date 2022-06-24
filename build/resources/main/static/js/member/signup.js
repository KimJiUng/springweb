function signup(){
    let form = $("#saveform")[0];
    let formdata = new FormData(form);

    $.ajax({
        url : "/member/signup",
        type : "POST",
        data : formdata,
        contentType : false,
        processData : false,
        success : function(re){
            if(re==true){
                alert("회원가입 완료");
                location.href = "/member/login";
            }else{
                alert("회원가입 실패 [서비스 오류:관리자에게 문의]");
            }

        }
    })
}