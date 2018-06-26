$(document).ready(function(){


    //选取的Production
    var productionList;
    var productionIndex;
    var currentProduction;
});


////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////品种管理////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
/**
 * 查询生产
 */
function lastProduction(type,production){
    var bodyParam={'page':1,'size':1,'flag':production};
    var httpR = new createHttpR(url+'listProduction','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            if(data.length>0){
                for(var o in data) {
                    for (var item in data[o]) {
                        if(type==1){
                            $('#'+item+'[prod="'+production+'"]').text(data[o][item]);
                            if(data[o]['actualcomp']!=''&&data[o]['power']!=''){
                                $('#rate[prod="'+production+'"]').text(((data[o]['actualcomp']/data[o]['power'])*100).toFixed(0));
                            }
                        }
                        else{
                            $('#'+item+'[prod="'+production+'"]').val(data[o][item]);
                        }

                    }
                    if(type==1){
                        //变化点
                        $('.box-body i').addClass('hidden');
                        var changedJO= jQuery.parseJSON(data[o]['changed']);
                        var redNum=0;
                        for(var k in changedJO ){
                            if(changedJO[k]=='red'){
                                redNum++;
                            }
                            $('#'+k+'[prod="'+production+'"]').removeClass('hidden').addClass('visible').addClass(changedJO[k]);
                        }
                        if(redNum!=0){
                            $('#changed[prod="'+production+'"]').text(redNum);
                        }
                        else{
                            $('#changedI[prod="'+production+'"]').addClass('hidden');

                        }

                        var prodstopJO= jQuery.parseJSON(data[o]['prodstop']);

                        var prodstop=0,equipstop=0,toolstop=0;
                        for(var key in prodstopJO ){
                             if(prodstopJO[key]=='01'){//设备异常
                                $('#'+key+'_01[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                                equipstop++;
                            }
                            else if(prodstopJO[key]=='02'){//工装异常
                                 $('#'+key+'_02[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                                 toolstop++;
                            }
                            else if(prodstopJO[key]=='03'){//生产异常
                                 $('#'+key+'_03[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                                 prodstop++;
                            }

                        }
                        $('#prodstop[prod="'+production+'"]').text(prodstop);
                        $('#equipstop[prod="'+production+'"]').text(equipstop);
                        $('#toolstop[prod="'+production+'"]').text(toolstop);
                        if(equipstop>0){
                            $('#shebei[prod="'+production+'"]').addClass('bg-green-red');
                        }
                        if(prodstop>0){
                            $('#shengchan[prod="'+production+'"]').addClass('bg-green-red');
                        }
                        if(toolstop>0){
                            $('#gongzhuang[prod="'+production+'"]').addClass('bg-green-red');
                        }
                    }
                    else{
                        var changedJO= jQuery.parseJSON(data[o]['changed']);
                        var redNum=0,greenNum=0;
                        for(var k in changedJO ){
                            $('input[name="'+k+'"][value="'+changedJO[k]+'"]').prop('checked',true);
                            if(changedJO[k]=='red'){
                                redNum++;
                            }
                            if(changedJO[k]=='green'){
                                greenNum++;
                            }
                        }
                        $('#redNum').text(redNum);
                        $('#greenNum').text(greenNum);
                        $('#changedSpan').text($('#changedShow').text());
                    }
                }
            }
            else{

            }


        }
    });
}
/**
 * 添加生产
 */
function addProduction(bodyParam){

    var httpR = new createHttpR(url+'addProduction','post','text',bodyParam,'callBack');
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
 * 修改生产
 * @param id
 */
function updateProduction(bodyParam){

    var httpR = new createHttpR(url+'updateProduction','post','text',bodyParam,'callBack');
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
 * 删除生产
 * @param id
 */
function deleteProduction(id){
    var bodyParam={'ids':'('+id+')'};
    var httpR = new createHttpR(url+'deleteProduction','post','text',bodyParam,'callBack');
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
 * 查询生产

 */
function  queryProduction (variety,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;
    if(variety==null||variety==''){
        var bodyParam={'page':currentPage,'size':pageSize};
    }
    else{
        var bodyParam={'page':currentPage,'size':pageSize,'variety':'%'+variety+'%'};
    }

    var httpR = new createHttpR(url+'listProduction','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            productionList=msg['data'];
            var html='';

            for(var o in data){
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td class="deleteCheckBoxTr">\n' +
                    '<input index='+o+' class="delCheckbox" type="checkbox" >\n' +
                    '</td>' +
                    '<td>'+data[o].variety+'</td>\n' +
                    '<td>'+data[o].yield+'</td>\n' +
                    '<td>'+data[o].rhythm+'</td>\n' +
                    '<td>'+data[o].production+'</td>\n' +
                    '<td>'+data[o].capacity+'</td>\n' +
                    '<td>'+data[o].changtime+'</td>\n' +
                    '<td>'+data[o].creater+'</td>\n' +
                    '<td>'+data[o].adddate+'</td>\n' ;

                html+='<td><a class="deleteProduction" href="" index='+o+' data-toggle="modal" data-target="#delete-box-pzqd"><span class="label label-danger">删除</span></a></td>\n';
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
