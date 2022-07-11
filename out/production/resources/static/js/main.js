getnews();

function getnews(){
    $.ajax({
        url : "/getnews",
        success : function(data){
            let html = "";
            html += '<div class="news_head">실시간 뉴스</div>';
            for(let i=0; i<data.length; i++){
                html += '<div class="row news_contentbox">'+
                            '<div class="col-md-2">'+
                                '<a target="_blank" href="'+data[i].news_link+'"><img width="100%" src="'+data[i].news_img+'"></a>'+
                            '</div>'+
                            '<div class="col-md-10">'+
                                '<a target="_blank" href="'+data[i].news_link+'"><span class="news_title">'+data[i].news_title+'</span></a><br>'+
                                '<a target="_blank" href="'+data[i].news_link+'"><span class="news_content">'+data[i].news_content+'</span></a><br>'+
                                '<span class="news_info">'+data[i].news_info+'</span>'+
                            '</div>'+
                        '</div>';
            }
            $("#newsbox").html(html);
        }
    });
}