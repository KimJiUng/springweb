
board_get();
// 특정 게시물 호출

function board_get(){
    $.ajax({
        url : "/board/getboard",
        success : function(re){
            html = "";
            html += '<form id="updateform">'+
                         '제목 : <input type="text" name="btitle" value="'+re.btitle+'">'+
                         '내용 : <input type="text" name="bcontent" value="'+re.bcontent+'">'+
                         '<button type="button" onclick="board_update()">수정처리</button>'+
                     '</form>';
            $("#updatediv").html(html);
        }
    });
}

function board_update(){
    let form = $("#updateform")[0];
    let formdata  = new FormData(form);

    $.ajax({
        url : "/board/update",
        data : formdata,
        method : 'PUT',
        processData : false,
        contentType : false,
        success : function(re){
            if(re){
                alert("수정완료");
                location.href = "/board/list";
            }else{
                alert("수정실패");
            }
        }

    });

}