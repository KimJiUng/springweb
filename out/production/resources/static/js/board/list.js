board_list();

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
                html += '<tr><td>'+re[i].bno+'</td><td><a href="/board/view/'+re[i].bno+'">'+re[i].btitle+'</a></td><td>'+re[i].createdate+'</td><td>'+re[i].bview+'</td><td>'+re[i].blike+'</td></tr>';
                //html += '<tr><td>'+re[i].bno+'</td><td onclick="view('+re[i].bno+')">'+re[i].btitle+'</td><td>'+re[i].createdate+'</td><td>'+re[i].bview+'</td><td>'+re[i].blike+'</td></tr>';
            }
            $("#boardlist").html(html);
        }

    });

}






