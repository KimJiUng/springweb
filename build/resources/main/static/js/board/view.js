
board_get();
// 특정 게시물 호출

function board_get(){
    $.ajax({
        url : "/board/getboard",
        success : function(re){
            html = "";
            html += '<div>게시물 번호'+re.bno+'</div>'+
                    '<div>게시물 작성자'+re.mid+'</div>'+
                    '<div>게시물 제목'+re.btitle+'</div>'+
                    '<div>게시물 내용'+re.bcontent+'</div>'+
                    '<div>게시물 작성일'+re.createdate+'</div>'+
                    '<div>게시물 수정일'+re.modifiedate+'</div>'+
                    '<div>게시물 조회수'+re.bview+'</div>'+
                    '<div>게시물 좋아요수'+re.blike+'</div>'+
                    '<button onclick="board_delete('+re.bno+')">삭제처리</button>';
            $("#boarddiv").html(html);
        }
    });
}

// 4. D 삭제 처리 메소드
function board_delete(bno){
    $.ajax({
        url : "/board/delete",
        data : {"bno":bno},
        method : 'DELETE',
        success : function(re){
            alert("삭제합니다.");
            location.href="/board/list";
        }

    });

}

