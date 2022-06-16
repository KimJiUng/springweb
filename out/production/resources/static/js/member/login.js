function login(){

    let mid = $("#mid").val();
    let mpassword = $("#mpassword").val();

    $.ajax({
        url : "/member/login",
        data : {"mid":mid, "mpassword":mpassword},
        type : "POST",
        success : function(re){
            if(re==true){
                alert("로그인성공");
                location.href = "/";    // 메인 페이지로 매핑
            }else{
                alert("로그인실패");
            }
        }
    })


}