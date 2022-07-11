
getweather();

// 날씨 크롤링 메소드
function getweather(){
    $.ajax({
        url : "/getweather",
        success : function(data){
            $("#weatherbox").html(data.지역명 +'  '+ data.상태 +'  '+ data.온도 +'  '+ data.습도 +'  '+ data.풍속 +'  '+ data.미세먼지);

        }
    });

}


// 채팅 메소드 - js 열리면 실행되는 메소드
$(document).ready(function(){
    // 1. 익명 닉네임 난수 만들기
        // 1~1000 사이 난수 생성
    let random = Math.floor(Math.random()*1001);
    let username ="익명"+random;

    // 2. 전송 버튼 눌렀을때
        // 1. 클릭이벤트 람다식
//    $("#sendbtn").on("click", e=> {
//        send();
//    });
        // 2. 클릭이벤트 메소드 정의
//    $("#sendbtn").on("click", function() {
//        send();
//    });
        // 3. 클릭이벤트 메소드 정의
    $("#sendbtn").click(function() {
        send();
    });
    // 2. JS에서 제공하는 websocket 클래스로 websocket 객체 선언
        // 1. [ /ws/chat ] 해당 url로 소켓 연결
    let websocket = new WebSocket("ws://localhost:8081/ws/chat");
    websocket.onmessage = onMessage;    // 아래에서 구현한 메소드를 웹소켓 객체에 대입
    websocket.onopen = onOpen;  // 아래에서 구현한 메소드를 웹소켓 객체에 대입
    websocket.onclose = onClose;    // 아래에서 구현한 메소드를 웹소켓 객체에 대입


    // 3. 소켓 연결이 종료 되었을때
    function onClose() {
        websocket.send(username+":님이 퇴장했습니다.");
    }

    // 4. 소켓 연결이 되었을때
    function onOpen() {
        websocket.send(username+":님이 입장했습니다.");
    }

    // 5. 메세지 전송
    function send() {
        let msg = $("#msg").val();  // 채팅 입력창에 입력한 데이터 호출
        websocket.send(username+":" + msg);
       // alert("메세지 전송 : "+username+" : " + msg);
        $("#msg").val("");
    }

    // 6. 메세지를 받았을때
    function onMessage(msg) {
        let data = msg.data;    // 받은 메세지의 내용
        let sessionid = data.split(":")[0]; // 보낸사람
        let message = data.split(":")[1];   // 메세지 내용
        let html = "";
        // 본인이 보낸 메세지 이면
        if(sessionid == username){
            html += '<div class="alert alert-primary"><span>'+data+'</span></div>';
        }else{  // 본인이 보낸 메세지가 아니면
            html += '<div class="alert alert-warning"><span>'+data+'</span></div>';
        }
        $("#contentbox").append(html);
        // 스크롤 최하단으로 이동
        $("#contentbox").scrollTop( $("#contentbox")[0].scrollHeight );
                                // $("#contentbox")[0].scrollHeight : 스크롤의 전체 길이
                                // $("#contentbox").scrollTop() : 스크롤 막대의 상단 위치
    }


});