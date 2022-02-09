var selectBoxRequest=function(){
    var data={
        year: $('#year option:selected').val(),
        semester: $('#semester option:selected').val()
    };

    $.ajax({
        type: 'POST',
        url: encodeURI('/student'),
        dataType: 'text',
        contentType: 'application/json',
        data: JSON.stringify(data),
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        }
    }).done(function(){
        alert('년도-학기 에 대한 요청을 보냈습니다.');
    }).fail(function (error){
        alert(JSON.stringify(error));
        alert('년도-학기 에 대한 요청이 실패했습니다.');
    });
}

