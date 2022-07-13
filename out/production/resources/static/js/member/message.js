
getfrommsglist();
gettomsglist();

let msgnolist = [];

function getfrommsglist(){
    $.ajax({
        url : "/member/getfrommsglist",
        success : function(json){
            let html = "<tr class='text-center'><th width='20%;'>받은사람</th><th width='30%;'>내용</th><th width='30%;'>받은날짜/시간</th><th width='20%;'></th></tr>";
            for(let i=0; i<json.length; i++){
                if(json[i].isread){
                    html += '<tr style="color: #d3d3d3;"><td onclick="frommsgread('+json[i].msgno+')">'+json[i].to+'</td><td onclick="frommsgread('+json[i].msgno+')">'+json[i].msg+'</td><td onclick="frommsgread('+json[i].msgno+')">'+json[i].date+'</td><td></td></tr>';
                }else{
                    html += '<tr onclick="frommsgread('+json[i].msgno+')"><td onclick="frommsgread('+json[i].msgno+')">'+json[i].to+'</td><td onclick="frommsgread('+json[i].msgno+')">'+json[i].msg+'</td><td onclick="frommsgread('+json[i].msgno+')">'+json[i].date+'</td><td></td></tr>';
                }
            }
            $("#frommsgtable").html(html);
        }
    });
}

function gettomsglist(){
    $.ajax({
        url : "/member/gettomsglist",
        success : function(json){
            let html = "<tr class='text-center'><th width='20%;'>보낸사람</th><th width='30%;'>내용</th><th width='30%;'>받은날짜/시간</th><th width='20%;'>전체선택<br><input class='form-check-input' type='checkbox' name='msgcheck' onclick='selectAll(this)'></th></tr>";
            for(let i=0; i<json.length; i++){
                if(json[i].isread){
                    html += '<tr style="color: #d3d3d3;"><td onclick="msgread('+json[i].msgno+')">'+json[i].from+'</td><td onclick="msgread('+json[i].msgno+')">'+json[i].msg+'</td><td onclick="msgread('+json[i].msgno+')">'+json[i].date+'</td><td class="text-center"><input class="form-check-input" name="msgcheck" type="checkbox" id="msgnocheck'+json[i].msgno+'" value="'+json[i].msgno+'" onclick="getCheckboxValue('+json[i].msgno+')"></td></tr>';
                }else{
                    html += '<tr><td onclick="msgread('+json[i].msgno+')">'+json[i].from+'</td><td onclick="msgread('+json[i].msgno+')">'+json[i].msg+'</td><td onclick="msgread('+json[i].msgno+')">'+json[i].date+'</td><td class="text-center"><input class="form-check-input" type="checkbox" name="msgcheck" id="msgnocheck'+json[i].msgno+'" value="'+json[i].msgno+'" onclick="getCheckboxValue('+json[i].msgno+')"></td></tr>';
                }
            }
            $("#tomsgtable").html(html);
            checklist();
        }
    });
}

function msgread(msgno){
    alert("받은 상세 내용 모달창으로 띄우기"+msgno);
    $.ajax({
        url : "/member/isread",
        data : {"msgno":msgno},
        method : "PUT",
        success : function(re){
            getisread();
            getfrommsglist();
            gettomsglist();

        }
    });
}

function checklist(){
    for(let i=0; i<msgnolist.length; i++){
        $("#msgnocheck"+msgnolist[i]).prop('checked',true);
    }
}



function frommsgread(msgno){
    alert("보낸 상세내용 모달창으로 띄우기"+msgno);
    checklist();
}

function getCheckboxValue(msgno)  {
    if($("#msgnocheck"+msgno).is(":checked")){
        $("#msgnocheck"+msgno).prop('checked',true);
        msgnolist.push(msgno);
    }else{
        $("#msgnocheck"+msgno).prop('checked',false);
        for(let j = 0; j < msgnolist.length; j++) {
          if(msgnolist[j] == msgno)  {
            msgnolist.splice(j, 1);
            j--;
          }
        }
    }
    console.log(msgnolist);
}

function selectAll(selectAll)  {

	const checkboxes = document.getElementsByName('msgcheck');

	checkboxes.forEach((checkbox) => {
		checkbox.checked = selectAll.checked;
	})
	if(selectAll.checked == true){
        for(let i=0; i<checkboxes.length; i++) {
            if(checkboxes[i].value != "on"){
                msgnolist.push(checkboxes[i].value);
            }
        }
	}else{
		msgnolist = [];
	}
	console.log(msgnolist);
}

function msgdelete(){
    $.ajax({
       url : "/member/msgdelete",
       data : JSON.stringify(msgnolist),
       method : 'DELETE',
       contentType : 'application/json',
       success : function(re){
            if(re){
                alert("선택된 메세지가 삭제되었습니다.");
                getisread();
                getfrommsglist();
                gettomsglist();
                msgnolist = [];
            }else{
                alert("삭제할 메세지를 체크해주세요.");
            }
       }
    });
}