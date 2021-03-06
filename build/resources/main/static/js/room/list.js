
 // 마커 클러스터 지도 사용

// 0.현재 내 위치 위도,경도 구하기
// GeoLocation을 이용해서 접속 위치를 얻어옵니다
/*
navigator.geolocation.getCurrentPosition(function(position) {
    var rlat = position.coords.latitude, // 위도
        rlon = position.coords.longitude; // 경도

*/

    // 1. Map 변수
 var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
        center : new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표 // 현재 접속된 디바이스 좌표
        level : 5 // 지도의 확대 레벨
    });

    // 마커 클러스터러를 생성합니다
    // 마커 클러스터러를 생성할 때 disableClickZoom 값을 true로 지정하지 않은 경우
    // 클러스터 마커를 클릭했을 때 클러스터 객체가 포함하는 마커들이 모두 잘 보이도록 지도의 레벨과 영역을 변경합니다
    // 이 예제에서는 disableClickZoom 값을 true로 설정하여 기본 클릭 동작을 막고
    // 클러스터 마커를 클릭했을 때 클릭된 클러스터 마커의 위치를 기준으로 지도를 1레벨씩 확대합니다
        // 2. 클러스터[마커 집합] 변수
    var clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 6, // 클러스터 할 최소 지도 레벨
        disableClickZoom: true // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
    });

    // 데이터를 가져오기 위해 jQuery를 사용합니다
    // 데이터를 가져와 마커를 생성하고 클러스터러 객체에 넘겨줍니다
    // $.get("통신할 URL", function(반환인수) {
    // 3. 여러개 MAP -> 클러스터 저장 [ 모든 지도 좌표 가져오기 ]
    /*$.get("/room/roomlist", function(data) {
        alert("확인"+data);
        console.log(data);
        // 데이터에서 좌표 값을 가지고 마커를 표시합니다
        // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
        var markers = $(data.positions).map(function(i, position) {
            return new kakao.maps.Marker({
                position : new kakao.maps.LatLng(position.lat, position.lng)
            });
        });

        // 클러스터러에 마커들을 추가합니다
        clusterer.addMarkers(markers);
    });*/

    // 마커 이미지 변경
        // 마커 이미지의 주소
        var markerImageUrl = 'http://192.168.17.85:8081/img/icon_home.png',
            markerImageSize = new kakao.maps.Size(40, 42), // 마커 이미지의 크기
            markerImageOptions = {
                offset : new kakao.maps.Point(20, 42)// 마커 좌표에 일치시킬 이미지 안의 좌표
            };

        // 마커 이미지를 생성한다
        var markerImage = new kakao.maps.MarkerImage(markerImageUrl, markerImageSize, markerImageOptions);


    // 4.
    // 지도 시점 변화 완료 이벤트를 등록한다 [ idle(드래그 완료시 이벤트발생) vs bounds_changed(드래그 중에 이벤트 발생) ]
    kakao.maps.event.addListener(map, 'idle', function () {
        // 클러스터 초기화
        clusterer.clear();
        // 사이드바에 넣을 html 변수 선언
        let html = "";
        $.ajax({
            url : '/room/roomlist',
            data : JSON.stringify(map.getBounds()), // 현재 보고 있는 지도 범위 [동서남북 좌표]
            method : 'POST',
            contentType : 'application/json',
            success : function(data){
                console.log(data)
                if(data.positions.length == 0){
                    html += '<div>조건에 맞는 결과가 없습니다.</div>'
                }else{
                    // 데이터에서 좌표 값을 가지고 마커를 표시합니다
                    // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
                    // 마커 목록 생성
                    var markers = $(data.positions).map(function(i, position) {
                        // 마커 하나 생성
                        var marker = new kakao.maps.Marker({
                                position : new kakao.maps.LatLng(position.rlon, position.rlat),
                                image : markerImage // 마커의 이미지
                            });
                            console.log(position);
                            // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
                            kakao.maps.event.addListener(marker, 'click', function() {
                               $("#roommodalheader").html(position.rtitle);
                               roommodal(position.rno);

                            });
                            html += '<div onclick="test('+position.rno+')" class="row"><div class="col-md-6"><img width="100%" src="/upload/'+position.rimg+'"></div><div class="col-md-6"> <div>집 번호 : <span>'+position.rno+'</span></div><div>집 이름 : <span>'+position.rtitle+'</span></div></div></div>';

                            return marker;
                        // 마커 하나 생성 end
                    });
                    // 클러스터러에 마커들을 추가합니다
                    clusterer.addMarkers(markers);
                }

                // 해당 html을 해당 id값에 추가
                $("#sidebar").html(html);
            }
        });
    });


    // 5.
    // 마커 클러스터러에 클릭이벤트를 등록합니다
    // 마커 클러스터러를 생성할 때 disableClickZoom을 true로 설정하지 않은 경우
    // 이벤트 헨들러로 cluster 객체가 넘어오지 않을 수도 있습니다
    kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {

        // 현재 지도 레벨에서 1레벨 확대한 레벨
        var level = map.getLevel()-1;

        // 지도를 클릭된 클러스터의 마커의 위치를 기준으로 확대합니다
        map.setLevel(level, {anchor: cluster.getCenter()});
    });


//}); // 현재 내 위치 위도,경도 구하기 end

function test(rno){
    $.ajax({
        url : "/room/getroom",
        data : {"rno" : rno},
        success : function(data){
            console.log(data);
            let html = "";
            html+= ' <div class="row">'+
                        '<span>'+data.rtitle+'</span><br>'+
                        '<div><img onclick="roommodal('+data.rno+')" width="100%" src="/upload/'+data.rimg+'"></div>'+
                        '<div><span>'+data.rtype+'</span></div>'+
                        '<div><span>'+data.rprice+'</span></div>'+
                        '<div class="col-md-4"><span>'+data.rarea+'</span></div>'+
                        '<div class="col-md-4"><span>'+data.radministrativeexpenses+'</span></div>'+
                        '<div class="col-md-4"><span>'+data.rrescue+'</span></div>'+
                    '</div>';
            $("#roommodalheader").html(data.rtitle);
            $("#sidebar").html(html);
        }
    })
}

function roommodal(rno){
    // 해당 모달에 데이터 넣기
    $.ajax({
        url : "/room/getroomimg",
        method : "GET",
        data : {"rno":rno},
        contentType : 'application/json',
        success : function(room){

        console.log(room);
            // 응답받은 데이터를 모달에 넣기
            let html = "";
            html += '<div id="carouselExampleControls" class="carousel slide" data-bs-ride="carousel">'+
                         '<div class="carousel-inner">';
            for(let i=0; i<room.length; i++){
                if(i==0){
                    html += '<div class="carousel-item active">';
                }else{
                    html += '<div class="carousel-item">';
                }
                html += '<img src="/upload/'+room[i]["rimg"]+'" class="d-block w-100"></div>';
            }
            html += '</div><button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="prev">'+
                          '<span class="carousel-control-prev-icon" aria-hidden="true"></span>'+
                          '<span class="visually-hidden">Previous</span>'+
                      '</button>'+
                      '<button class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls" data-bs-slide="next">'+
                          '<span class="carousel-control-next-icon" aria-hidden="true"></span>'+
                          '<span class="visually-hidden">Next</span>'+
                      '</button>'+
                  '</div>';
            $("#roommodalimg").html(html);
            // 모달 띄우기
            $("#roommodalbtn").click();
        }
    })
}







