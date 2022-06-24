$(function(){
    $.ajax({
        url : "/room/myroomlist",
        success : function(data){

            let html = "";
            html += '<tr><th width="10%">대표이미지</th><th>방번호</th><th>방이름</th><th>최근수정날짜</th><th>비고</th></tr>'
            for(let i=0; i<data.length; i++){
                html += '<tr><td><img width="100%" src="/upload/'+data[i]["rimg"]+'"></td><td>'+data[i]["rno"]+'</td><td>'+data[i]["rtitle"]+'</td><td>'+data[i]["modifiedate"]+'</td><td><button onclick="room_update('+data[i]["rno"]+')">수정</button><button onclick="room_delete('+data[i]["rno"]+')">삭제</button> </td></tr>'
            }
            $("#myroom").html(html);
        }
    });

})

function room_delete(rno){
    $.ajax({
        url : "/room/room_delete",
        data : {"rno" : rno},
        type : "DELETE",
        success : function(re){
            if(re){
                alert("삭제완료");
                location.reload();
            }else{
                alert("삭제실패");
            }
        }
    })
}

function room_update(rno){
    $("#rno").val(rno);
    $("#updatemodalbtn").click();

}

function update(){

    let form = $("#saveform")[0];
    let formdata = new FormData(form);

    $.ajax({
        url : "/room/room_update",
        data : formdata,
        type : "put",
        contentType : false,    // 첨부파일 전송시 사용되는 속성
        processData : false,    // 첨부파일 전송시 사용되는 속성
        success : function(re){
            if(re){
                alert("수정완료");
                modalclosebtn.click();
                location.reload();
            }else{
                alert("수정실패");
            }
        }
    })

}

// 다음 주소 -> 좌표
  var mapContainer = document.getElementById('map'), // 지도를 표시할 div
         mapOption = {
             center: new daum.maps.LatLng(37.537187, 127.005476), // 지도의 중심좌표
             level: 5 // 지도의 확대 레벨
         };

     //지도를 미리 생성
     var map = new daum.maps.Map(mapContainer, mapOption);
     //주소-좌표 변환 객체를 생성
     var geocoder = new daum.maps.services.Geocoder();
     //마커를 미리 생성
     var marker = new daum.maps.Marker({
         position: new daum.maps.LatLng(37.537187, 127.005476),
         map: map
     });


     function sample5_execDaumPostcode() {
         new daum.Postcode({
             oncomplete: function(data) {
                 var addr = data.address; // 최종 주소 변수

                 // 주소 정보를 해당 필드에 넣는다.
                 document.getElementById("sample5_address").value = addr;
                 // 주소로 상세 정보를 검색
                 geocoder.addressSearch(data.address, function(results, status) {
                     // 정상적으로 검색이 완료됐으면
                     if (status === daum.maps.services.Status.OK) {

                         var result = results[0]; //첫번째 결과의 값을 활용

                         // 해당 주소에 대한 좌표를 받아서
                         var coords = new daum.maps.LatLng(result.y, result.x);
                            $("#rlat").val(result.x);
                            $("#rlon").val(result.y);

                         // 지도를 보여준다.
                         mapContainer.style.display = "block";
                         map.relayout();
                         // 지도 중심을 변경한다.
                         map.setCenter(coords);
                         // 마커를 결과값으로 받은 위치로 옮긴다.
                         marker.setPosition(coords)
                     }
                 });
             }
         }).open();
     }