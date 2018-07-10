$(document).ready(function(){


    //选取的Timer
    var timerList;
    var timerIndex;
    var currentTimer;
});


////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////品种管理////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
/**
 * 添加品种
 */
function addTimer(type){
    var userinfo = JSON.parse(sessionStorage.getItem('userinfo'));
    var bodyParam={'prodnum':$('#prodnum').val(),'type':$('#type').val(),'starttime':$('#starttime').val(),
        'endtime':$('#endtime').val(),'creater':userinfo['username']};
    var httpR = new createHttpR(url+'addTimer','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        //var msg = obj['msg'];
        if(status=='0'){
            alert("数据已保存！");
            if(type==1){
                window.location.reload();
            }
            else if(type==2){
                queryTimer($('#productionSearch').val(),$('#typeSearch').val(),currentPage,pageSize);
                $('#addForm select').val("");
                $('#addForm input').val("");
            }

            //window.location.href="interface.html?index="+interfaceIndex;
        }
    });
}

/**
 * 修改品种
 * @param id
 */
function updateTimer(bodyParam){

    var httpR = new createHttpR(url+'updateTimer','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        //var msg = obj['msg'];
        if(status=='0'){
            alert("修改成功！");
            window.location.reload();
            //window.location.href="interface.html?index="+interfaceIndex;
        }
    });
}

/**
 * 删除品种
 * @param id
 */
function deleteTimer(id){
    var bodyParam={'ids':'('+id+')'};
    var httpR = new createHttpR(url+'deleteTimer','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        //var msg = obj['msg'];
        if(status=='0'){
            alert("删除成功！");
            window.location.reload();
        }
    });
}
/**
 * 查询品种

 */
function  queryTimer (prodnum,type,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;
    var bodyParam={'page':currentPage,'size':pageSize};
    if(prodnum!=null&&prodnum!=''){
        bodyParam['prodnum']=prodnum;
    }
    if(type!=null&&type!=''){
        bodyParam['type']=type;
    }


    var httpR = new createHttpR(url+'listTimer','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            timerList=msg['data'];
            var html='';

            for(var o in data){
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td class="deleteCheckBoxTr">\n' +
                    '<input index='+o+' class="delCheckbox" type="checkbox" >\n' +
                    '</td>' +
                    '<td>'+data[o].production+'</td>\n' +
                    '<td>'+data[o].type+'</td>\n' +
                    '<td>'+data[o].starttime+'-'+data[o].endtime+'</td>\n' +
                    '<td>'+data[o].creater+'</td>\n' +
                    '<td>'+data[o].adddate+'</td>\n' ;

                html+='<td><a class="deleteTimer" href="" index='+o+' data-toggle="modal" data-target="#delete-box-timer"><span class="label label-danger">删除</span></a></td>\n';
                html+='</tr>';
            }
            $('#timerTbody').html(html);
            var num=msg['num'];
            if(num>0) {
                var pageHtml = '';
                var totalPage = 0;
                if (num % pageSize == 0) {
                    totalPage = num / pageSize;
                }
                else {
                    totalPage = Math.ceil(num / pageSize);
                }

                if (currentPage == 1) {
                    pageHtml += '<li class="disabled"><a href="#">|&laquo;</a></li>';
                    pageHtml += '<li class="disabled"><a href="#">&laquo;</a></li>';
                }
                else {
                    pageHtml += '<li ><a href="#" class="pageBtn" index="1">|&laquo;</a></li>';
                    pageHtml += '<li ><a href="#" class="prevBtn" index="">&laquo;</a></li>';
                }
                if (totalPage <= showPage) {
                    for (var i = 1; i < Number(totalPage) + 1; i++) {
                        if (currentPage == i) {
                            pageHtml += '<li class="active"><a href="#" >' + i + '</a></li>';
                        }
                        else {
                            pageHtml += '<li><a href="#" class="pageBtn" index="' + i + '">' + i + '</a></li>';
                        }
                    }
                }
                else {
                    if (currentPage <= (showPage - 1) / 2) {
                        for (var i = 1; i <= showPage; i++) {
                            if (currentPage == i) {
                                pageHtml += '<li class="active"><a href="#" >' + i + '</a></li>';
                            }
                            else {
                                pageHtml += '<li><a href="#" class="pageBtn" index="' + i + '">' + i + '</a></li>';
                            }
                        }
                    }
                    else if (totalPage - currentPage < (showPage - 1) / 2) {
                        for (var i = Number(totalPage) - showPage; i <= totalPage; i++) {
                            if (currentPage == i) {
                                pageHtml += '<li class="active"><a href="#" >' + i + '</a></li>';
                            }
                            else {
                                pageHtml += '<li><a href="#" class="pageBtn" index="' + i + '">' + i + '</a></li>';
                            }
                        }
                    }
                    else {
                        for (var i = Number(currentPage) - (showPage - 1) / 2; i <= Number(currentPage) + (showPage - 1) / 2; i++) {
                            if (currentPage == i) {
                                pageHtml += '<li class="active"><a href="#" >' + i + '</a></li>';
                            }
                            else {
                                pageHtml += '<li><a href="#" class="pageBtn" index="' + i + '">' + i + '</a></li>';
                            }
                        }
                    }


                }

                if (currentPage == totalPage) {
                    pageHtml += '<li class="disabled"><a href="#">&raquo;</a></li>';
                    pageHtml += '<li class="disabled"><a href="#">&raquo;|</a></li>';
                }
                else {
                    pageHtml += '<li class="nextBtn" index=""><a href="#">&raquo;</a></li>';
                    pageHtml += '<li class="pageBtn" index="' + totalPage + '"><a href="#">&raquo;|</a></li>';
                }
                /* pageHtml+='<li><input type="text" id="jumpPageText" class="paging-inpbox form-control"></li>\n' +
                     '<li><button type="button" id="jumpBtn" class="paging-btnbox btn btn-primary">跳转</button></li>\n' +
                     '<li><span class="number-of-pages">共'+totalPage+'页</span></li>';*/

                $('.pagination').html(pageHtml);
            }


        }
    });
}

