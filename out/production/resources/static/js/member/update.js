function update(){
    $.ajax({
        url : "/member/update",
        data : {"mname" : $("#mname").val()},
        method : "put",
        success : function(re){
            alert(re);
        }
    });
}

function mdelete(){

    $.ajax({
        url : "/member/mdelete",
        data : {"mpassword":$("#mpassword").val()},
        type : "DELETE",
        success : function(re){
            if(re){
                alert("탈퇴성공");
                location.href = "/member/logout";
            }else{
                alert("탈퇴실패");
            }
        }
    })
}