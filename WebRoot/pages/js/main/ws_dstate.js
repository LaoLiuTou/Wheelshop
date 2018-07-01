$(document).ready(function(){


    //选取的Dstate
    var dstateList;
    var dstateIndex;
    var currentDstate;
});


////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////状态管理////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////


/**
 * 修改状态
 * @param id
 */
function updateDstate(bodyParam){

    var httpR = new createHttpR(url+'updateDstate','post','text',bodyParam,'callBack');
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
 * 删除状态
 * @param id
 */
function deleteDstate(id){
    var bodyParam={'id':id};
    var httpR = new createHttpR(url+'deleteDstate','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        //var msg = obj['msg'];
        if(status=='0'){
            alert("修改成功！");
            window.location.reload();
        }
    });
}
/**
 * 查询状态
 * @param dstatename
 * @param currentPage
 * @param pageSize
 */
function  queryDstates (bodyParam,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;



    var httpR = new createHttpR(url+'listDstate','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            dstateList=msg['data'];
            var html='';
            for(var o in data){
                html+='<tr index='+o+' class="gradeX">\n' ;



                if(data[o].production=='1'){html+='<td>旋压A线</td>\n' ;}
                else if(data[o].production=='2'){html+='<td>旋压B线</td>\n' ;}
                else if(data[o].production=='3'){html+='<td>滚型轮辋</td>\n' ;}
                else if(data[o].production=='4'){html+='<td>型钢轮辋</td>\n' ;}
                else if(data[o].production=='5'){html+='<td>旋压轮辐</td>\n' ;}
                else if(data[o].production=='6'){html+='<td>滚型轮辐</td>\n' ;}
                else{html+='<td></td>\n' ;}


                if(data[o].state=='01'){html+='<td>设备停台</td>\n' ;}
                else if(data[o].state=='02'){html+='<td>工装停台</td>\n' ;}
                else if(data[o].state=='03'){html+='<td>生产停台</td>\n' ;}
                else{html+='<td></td>\n' ;}


                html+='<td>'+data[o].duration+'</td>\n' +
                    '<td>'+data[o].deviceno+'</td>\n' +
                    '<td>'+data[o].adddate+'</td>\n' +
                    '<td>'+data[o].comment+'</td>\n' ;

                html+='<td><a class="updateDstate" href="" index='+o+' data-toggle="modal" data-target="#update-box"><span class="label label-info label-mini">编辑备注</span></a>   ' ;
                html+='</tr>';
            }
            $('#dstateTbody').html(html);
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
