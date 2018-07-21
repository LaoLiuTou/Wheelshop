$(document).ready(function(){


    //选取的Varieties
    var varietiesList;
    var varietiesIndex;
    var currentVarieties;
});


////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////品种管理////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
/**
 * 添加品种
 */
function addVarieties(){
    var userinfo = JSON.parse(sessionStorage.getItem('userinfo'));
    var bodyParam={'variety':$('#variety').val(),'yield':$('#yield').val(),'rhythm':$('#rhythm').val(),'itemtime':$('#itemtime').val(),
        'prodnum':$('#production').val(),'production':$('#production').find('option:selected').text(),'capacity':$('#capacity').val(),'changtime':$('#changtime').val(),
        'creater':userinfo['username']};

    var httpR = new createHttpR(url+'addVarieties','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        //var msg = obj['msg'];
        if(status=='0'){
            alert("新建成功！");
            window.location.reload();
            //window.location.href="interface.html?index="+interfaceIndex;
        }
    });
}
function addVarieties2(){
    var userinfo = JSON.parse(sessionStorage.getItem('userinfo'));
    var requiredvar=[];

    for(var key in $('#required').val()){
        var temp={};
        temp['index']=$('#required').val()[key];
        temp['name']=$("#required option[value=" + $('#required').val()[key] + "]").text();
        temp['replace']='';
        requiredvar.push(temp)
    }
    //alert(JSON.stringify(requiredvar));

    var bodyParam={'variety':$('#variety').val(),'yield':$('#yield').val(),'rhythm':$('#rhythm').val(),'itemtime':$('#itemtime').val(),
        'prodnum':$('#production').val(),'production':$('#production').find('option:selected').text(),'capacity':$('#capacity').val(),'changtime':$('#changtime').val(),
        'required':JSON.stringify(requiredvar),'creater':userinfo['username']};
    var httpR = new createHttpR(url+'addVarieties','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        //var msg = obj['msg'];
        if(status=='0'){
            alert("新建成功！");
            window.location.reload();
            //window.location.href="interface.html?index="+interfaceIndex;
        }
    });
}

/**
 * 修改品种
 * @param id
 */
function updateVarieties(bodyParam){

    var httpR = new createHttpR(url+'updateVarieties','post','text',bodyParam,'callBack');
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
function deleteVarieties(id){
    var bodyParam={'ids':'('+id+')'};
    var httpR = new createHttpR(url+'deleteVarieties','post','text',bodyParam,'callBack');
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
function  queryVarieties (variety,prodnum,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;
    var bodyParam={'page':currentPage,'size':pageSize};
    if(variety!=''){
        bodyParam['variety']='%'+variety+'%';
    }
    if(prodnum!=''){
        bodyParam['prodnum']=prodnum;
    }

    var httpR = new createHttpR(url+'listVarieties','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            varietiesList=msg['data'];
            var html='';

            for(var o in data){
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td class="deleteCheckBoxTr">\n' +
                    '<input index='+o+' class="delCheckbox" type="checkbox" >\n' +
                    '</td>' +
                    '<td>'+data[o].variety+'</td>\n' +
                    '<td>'+data[o].yield+'</td>\n' +
                    '<td>'+data[o].rhythm+'</td>\n' +
                    '<td>'+data[o].itemtime+'</td>\n' +
                    '<td>'+data[o].production+'</td>\n' +
                    '<td>'+data[o].capacity+'</td>\n' +
                    '<td>'+data[o].changtime+'分</td>\n' +
                    '<td>'+data[o].creater+'</td>\n' +
                    '<td>'+data[o].adddate+'</td>\n' ;

                html+='<td><a class="deleteVarieties" href="" index='+o+' data-toggle="modal" data-target="#delete-box-pzqd"><span class="label label-danger">删除</span></a></td>\n';
                html+='</tr>';
            }
            $('#pzqdTbody').html(html);
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
/**
 * 查询品种

 */
function  queryVarieties2 (variety,prodnum,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;
    var bodyParam={'page':currentPage,'size':pageSize};
    if(variety!=''){
        bodyParam['variety']='%'+variety+'%';
    }
    if(prodnum!=''){
        bodyParam['prodnum']=prodnum;
    }

    var httpR = new createHttpR(url+'listVarieties','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            varietiesList=msg['data'];
            var html='';

            for(var o in data){
                var temp;
                var required=jQuery.parseJSON(data[o].required);
                var tempArray=[];
                for(var key in required){
                    tempArray.push(required[key]['name']);
                }
                if(tempArray.length>3){
                    temp=tempArray[0]+"/"+tempArray[1]+"/"+tempArray[2];
                }
                else{
                    temp=tempArray.join('/');
                }


                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td class="deleteCheckBoxTr">\n' +
                    '<input index='+o+' class="delCheckbox" type="checkbox" >\n' +
                    '</td>' +
                    '<td>'+data[o].variety+'</td>\n' +
                    '<td>'+data[o].yield+'</td>\n' +
                    '<td>'+data[o].rhythm+'</td>\n' +
                    '<td>'+data[o].itemtime+'</td>\n' +
                    '<td>'+data[o].production+'</td>\n' +
                    '<td>'+data[o].capacity+'</td>\n' +
                    //'<td>'+temp+'</td>\n' +
                    '<td class="required"><div class="tips-box-1">'+
                    '<div class="tips-content">'+temp+'</div>'+
                    '<div id="details" class="tips-box-2 hide">'+
                    '<span class="triangle"></span>'+tempArray.join('/')+
                    '</div>'+
                    '</div></td>\n' +
                    '<td>'+data[o].changtime+'分</td>\n' +
                    '<td>'+data[o].creater+'</td>\n' +
                    '<td>'+data[o].adddate+'</td>\n' ;



                html+='<td><a class="deleteVarieties" href="" index='+o+' data-toggle="modal" data-target="#delete-box-pzqd"><span class="label label-danger">删除</span></a></td>\n';
                html+='</tr>';
            }
            $('#pzqdTbody').html(html);
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

/**
 * 查询select

 * @param currentPage
 * @param pageSize
 */
function  selectVarieties (production) {

    var bodyParam={'prodnum':production};

    var httpR = new createHttpR(url+'allVarieties','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            var html='';
            html+='<option value="" rhythm="" yield=""></option>\n';
            for(var o in data){
                html+='<option value="'+data[o].variety+'" rhythm="'+data[o].rhythm+'" changtime="'+data[o].changtime+'" yield="'+data[o].yield+'" itemtime="'+data[o].itemtime+'" requiredeq=\''+data[o].required+'\'>'+data[o].variety+'</option>\n';
            }
            $('#variety').html(html);


            lastProduction(2,production);
        }
    });
}