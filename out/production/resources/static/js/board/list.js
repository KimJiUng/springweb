let cno = 1;
let key = ""
let keyword = ""
board_list();
// 2. R 보기 처리 메소드
function board_list(){
    $.ajax({
        url : "getcategory",
        success : function(category){
            html = "";
            html += '<select name="category" id="category" onchange="categoryselect()">'
            for(let i=0; i<category.length; i++){
                html += '<option value="'+category[i].cno+'">'+category[i].cname+'</option>'
            }
            html += '<option value="0">전체보기</option>';
            html += '</select>';
            $("#categorybox").html(html);
        }
    });
    getboardlist(key,keyword,0);
}

function categoryselect(){
    var langSelect = document.getElementById("category");

    // select element에서 선택된 option의 value가 저장된다.
    var selectValue = langSelect.options[langSelect.selectedIndex].value;
    cno = selectValue;
    key = "";
    keyword = "";
    getboardlist(key,keyword,0);

/*    // select element에서 선택된 option의 text가 저장된다.
    var selectText = langSelect.options[langSelect.selectedIndex].text;
    console.log(selectText);*/
}

function getboardlist(key,keyword,page){
    $.ajax({
        url : "/board/getboardlist",
        data : {"key":key,"keyword":keyword,"cno":cno, "page":page},
        method : 'GET',
        success : function(re){
            console.log(re);

            let html = "";
            html += '<tr><th>번호</th><th>제목</th><th>작성자</th><th>작성일</th><th>조회수</th><th>좋아요수</th></tr>';
            if(re.jsonarray.length==0){
                html += '<tr><th class="text-center" colspan="6">검색결과가 존재하지 않습니다.</th></tr>';
            }else{
                for(let i=0; i<re.jsonarray.length; i++){
                    if(cno==0){
                        html += '<tr><td>'+re.jsonarray[i].bno+'</td><td><a href="/board/view/'+re.jsonarray[i].bno+'">'+re.jsonarray[i].btitle+'</a></td><td>'+re.jsonarray[i].mid+'</td><td>'+re.jsonarray[i].createdate+'</td><td>'+re.jsonarray[i].bview+'</td><td>'+re.jsonarray[i].blike+'</td></tr>';
                    }else{
                        if(re.jsonarray[i].cno == cno){
                            html += '<tr><td>'+re.jsonarray[i].bno+'</td><td><a href="/board/view/'+re.jsonarray[i].bno+'">'+re.jsonarray[i].btitle+'</a></td><td>'+re.jsonarray[i].mid+'</td><td>'+re.jsonarray[i].createdate+'</td><td>'+re.jsonarray[i].bview+'</td><td>'+re.jsonarray[i].blike+'</td></tr>';
                        }
                        //html += '<tr><td>'+re[i].bno+'</td><td onclick="view('+re[i].bno+')">'+re[i].btitle+'</td><td>'+re[i].createdate+'</td><td>'+re[i].bview+'</td><td>'+re[i].blike+'</td></tr>';
                    }

                }
            }
            //////////////////////////// 페이징 버튼 생성 코드 ////////////////////////////////////
            let pagehtml = "";

            ////////////////////////// 이전 버튼 /////////////////////////////////////////////
            if(page==0){    // 현재 페이지가 첫페이지이면
                pagehtml += '<li class="page-item disabled"><button class="page-link" type="button" onclick="getboardlist(key,keyword,'+(page)+')">이전</button> </li>';
            }else{  // 현재페이지가 첫페이지가 아니면
                pagehtml += '<li class="page-item"><button class="page-link" type="button" onclick="getboardlist(key,keyword,'+(page-1)+')">이전</button> </li>';
            }
            ////////////////////////////////////////////////////////////////////////////////
            for(let i = re.startbtn; i <=re.endbtn; i++) {
                pagehtml += '<li class="page-item"><button class="page-link" type="button" onclick="getboardlist(key,keyword,'+(i-1)+')">'+i+'</button> </li>';
            }
            ////////////////////////// 다음 버튼 ///////////////////////////////////////////////////
            if(page==re.totalpage-1 || re.totalpage == 0){
                pagehtml += '<li class="page-item disabled"><button class="page-link" type="button" onclick="getboardlist(key,keyword,'+(page)+')">다음</button> </li>';
            }else{
                pagehtml += '<li class="page-item"><button class="page-link" type="button" onclick="getboardlist(key,keyword,'+(page+1)+')">다음</button> </li>';
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////
            $("#boardlist").html(html); // 테이블에 html 넣기
            $("#pagebtnbox").html(pagehtml);
        }
    });
}

// 검색
function search(){
    key = $("#key").val();
    keyword = $("#keyword").val();
    // 키 와 키워드 입력받음

    getboardlist(key,keyword,0);
}



