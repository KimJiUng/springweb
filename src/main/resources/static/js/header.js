
getweather();

function getweather(){
    $.ajax({
        url : "/getweather",
        success : function(data){
            $("#weatherbox").html(data.지역명 +'  '+ data.상태 +'  '+ data.온도 +'  '+ data.습도 +'  '+ data.풍속 +'  '+ data.미세먼지);

        }
    });

}