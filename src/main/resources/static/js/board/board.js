
board_list();


// 1. C 쓰기 처리 메소드
function board_save(){

    let form = $("#saveform")[0];
    let formdata  = new FormData(form);

    $.ajax({
        url : "/board/save",
        data : formdata,
        method : 'POST',
        processData : false,
        contentType : false,
        success : function(re){
            if(re){
                alert("작성성공");
            }else{
                alert("작성실패");
            }
        }

    });

}

// 2. R 보기 처리 메소드
function board_list(){
    $.ajax({
        url : "/board/getboardlist",
        data : {},
        method : 'GET',
        success : function(re){
            let html = "";
            html += '<tr><th>번호</th><th>제목</th><th>작성일</th><th>조회수</th><th>좋아요수</th></tr>';
            for(let i=0; i<re.length; i++){
                html += '<tr><td>'+re[i].bno+'</td><td>'+re[i].btitle+'</td><td>'+re[i].createdate+'</td><td>'+re[i].bview+'</td><td>'+re[i].blike+'</td></tr>';
            }
            $("#boardlist").html(html);
        }

    });

}

// 3. U 수정 처리 메소드
function board_update(){
    $.ajax({
        url : "/board/update",
        data : {},
        method : 'PUT',
        success : function(re){
            alert("수정합니다."+re);
        }

    });

}

// 4. D 삭제 처리 메소드
function board_delete(){
    $.ajax({
        url : "/board/delete",
        data : {},
        method : 'DELETE',
        success : function(re){
            alert("삭제합니다."+re);
        }

    });

}