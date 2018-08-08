$(document).ready(function(){


    //选取的Production
    var productionList;
    var productionIndex;
    var currentProduction;


});
var statusParam={};

////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////品种管理////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
/**
 * 查询生产
 */
function lastProduction(type,production){
    var bodyParam={'page':1,'size':1,'prodnum':production};
    if(type=='1'){
        bodyParam.startstatus='1';
    }
    var httpR = new createHttpR(url+'lastProduction','post','text',bodyParam,'callBack');
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
                            if(data[o]['actualcomp']!=''&&data[o]['power']!=''&&data[o].power!=null){
                                $('#rate[prod="'+production+'"]').text(((data[o]['actualcomp']/data[o]['power'])*100).toFixed(1)+'%');
                            }

                        }
                        else{
                            $('#'+item+'[prod="'+production+'"]').val(data[o][item]);
                        }

                    }
                    if(type==1){

                        //第六条生产线
                        if(data[o]['required']!=''&&production=='6'){
                            var requiredJO= jQuery.parseJSON(data[o]['required']);
                            for(var key in requiredJO ){
                                $('#device'+requiredJO[key]['index']+'_01[prod="'+production+'"]').removeClass('bg-gray').addClass('bg-green');
                                $('#device'+requiredJO[key]['index']+'_02[prod="'+production+'"]').removeClass('bg-gray').addClass('bg-green');
                                $('#device'+requiredJO[key]['index']+'_03[prod="'+production+'"]').removeClass('bg-gray').addClass('bg-green');
                            }
                        }


                        //变化点
                        //变化点
                        $('.box-body i').addClass('hidden');
                        var changedJO= jQuery.parseJSON(data[o]['changed']);
                        var redNum=0;
                        for(var k in changedJO ){
                            if(!$('#device'+k.replace('radio','').substr(0,1)+'_01[prod="'+production+'"]').hasClass('bg-gray')){
                                if(changedJO[k]=='red'){
                                    redNum++;
                                }
                                $('#'+k+'[prod="'+production+'"]').removeClass('hidden').addClass('visible').addClass(changedJO[k]);
                            }

                        }
                        $('#changed[prod="'+production+'"]').text(redNum);
                        if(redNum==0){
                            $('#changedI').addClass('hidden');
                        }
                        /*$('.box-body i').addClass('hidden');
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

                        }*/

                        var stopsJO= jQuery.parseJSON(data[o]['stops']);

                        var prodstop=0,equipstop=0,toolstop=0;
                        for(var key in stopsJO ){
                             if(stopsJO[key]=='01'){//设备异常
                                $('#'+key+'_01[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                                equipstop++;
                            }
                            else if(stopsJO[key]=='02'){//工装异常
                                 $('#'+key+'_02[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                                 toolstop++;
                            }
                            else if(stopsJO[key]=='03'){//生产异常
                                 $('#'+key+'_03[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                                 prodstop++;
                            }

                        }
                        /*$('#prodstop[prod="'+production+'"]').text(prodstop);
                        $('#equipstop[prod="'+production+'"]').text(equipstop);
                        $('#toolstop[prod="'+production+'"]').text(toolstop);*/

                        clearInterval(prodstopInterval);
                        clearInterval(equipstopInterval);
                        clearInterval(toolstopInterval);
                        $('#prodstop[prod="'+production+'"]').text(sec_to_time(sessionStorage.getItem('prodstopTime')==null?0:sessionStorage.getItem('prodstopTime')));
                        $('#equipstop[prod="'+production+'"]').text(sec_to_time(sessionStorage.getItem('equipstopTime')==null?0:sessionStorage.getItem('equipstopTime')));
                        $('#toolstop[prod="'+production+'"]').text(sec_to_time(sessionStorage.getItem('toolstopTime')==null?0:sessionStorage.getItem('toolstopTime')));


                        if(equipstop>0){
                            $('#shebei[prod="'+production+'"]').addClass('bg-green-red');
                            equipstopInterval=setInterval(function (){
                                var equipstopTime=sessionStorage.getItem('equipstopTime');
                                if(equipstopTime==null){
                                    equipstopTime=0;
                                }
                                else{
                                    equipstopTime=Number(equipstopTime)+1;
                                }
                                sessionStorage.setItem('equipstopTime',equipstopTime);
                                $('#equipstop'+'[prod="'+production+'"]').text(sec_to_time(equipstopTime));
                                //$('#prodstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['prodstop']));
                                //$('#equipstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['equipstop']));
                                //$('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['toolstop']));
                            },1000);
                        }
                        if(prodstop>0){
                            $('#shengchan[prod="'+production+'"]').addClass('bg-green-red');
                            prodstopInterval=setInterval(function (){
                                var prodstopTime=sessionStorage.getItem('prodstopTime');
                                if(prodstopTime==null){
                                    prodstopTime=0;
                                }
                                else{
                                    prodstopTime=Number(prodstopTime)+1;
                                }
                                sessionStorage.setItem('prodstopTime',prodstopTime);
                                $('#prodstop'+'[prod="'+production+'"]').text(sec_to_time(prodstopTime));
                                //$('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['toolstop']));
                            },1000);
                        }
                        if(toolstop>0){
                            $('#gongzhuang[prod="'+production+'"]').addClass('bg-green-red');
                            toolstopInterval=setInterval(function (){
                                var toolstopTime=sessionStorage.getItem('toolstopTime');
                                if(toolstopTime==null){
                                    toolstopTime=0;
                                }
                                else{
                                    toolstopTime=Number(toolstopTime)+1;
                                }
                                sessionStorage.setItem('toolstopTime',toolstopTime);
                                $('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(toolstopTime));
                                //$('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['toolstop']));
                            },1000);
                        }



                        //开始时间
                        if(data[o]['starttime']!=''){
                            var starttime = new Date(data[o]['starttime']).format("hh:mm:ss");
                            $('#starttime').text(starttime);

                        }

                        //停台时间转时分秒
                        //$('#prodstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['prodstop']));
                        //$('#equipstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['equipstop']));
                        //$('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['toolstop']));

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
                        changed=jQuery.parseJSON(data[o]['changed']);
                        $('#redNum').text(redNum);
                        $('#greenNum').text(greenNum);
                        $('#changedSpan').text($('#changedShow').text());

                        //第六条生产线
                        if(data[o]['required']!=''&&production=='6'){
                            var requiredJO= jQuery.parseJSON(data[o]['required']);

                            var requiredValue=new Array();
                            for(var key in requiredJO ){
                                requiredValue.push(requiredJO[key]['index']);
                            }
                            $('#required').val(requiredValue);
                            $('#required').multiselect("refresh");
                        }

                        //开始结束按钮
                        if(data[o]['variety']!=''&&data[o]['variety']!=null&&data[o]['prodstate']=='生产时间'){

                            if(data[o]['flag']=='1'){
                                $('#startBtn').addClass('hide');

                                //开始状态 品种、状态不能修改
                                $('#variety').prop("disabled",true);
                                $('#prodstate').prop("disabled",true);
                            }
                            else{
                                $('#endBtn').addClass('hide');

                            }

                        }
                        else{
                            $('#endBtn').addClass('hide');
                            $('#startBtn').prop("disabled",true);
                            $('#startBtn').removeClass('btn-info').addClass('btnbg');

                        }
                        statusParam.id=data[o]['id'];
                        statusParam.prodnum=data[o]['prodnum'];

                    }
                }
            }
            else{

            }


        }
    });
}
function lastProduction2(production){
    var bodyParam={'page':1,'size':1,'prodnum':production,'startstatus':'1'};
    var httpR = new createHttpR(url+'lastProduction','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var id,yield=0,itemtime=0;
            var prodstop=0,equipstop=0,toolstop=0,startflag='0';
            var data=msg['data'];
            if(data.length>0){
                for(var o in data) {

                    //开始状态
                    startflag=data[o]['flag'];

                    //保存id
                    id=data[o]['id'];
                    yield=Number(data[o]['yield']);
                    itemtime=Number(data[o]['itemtime']);
                    for (var item in data[o]) {

                        $('#'+item+'[prod="'+production+'"]').text(data[o][item]);


                    }
                    if(data[o]['actualcomp']!=''&&data[o]['power']!=''&&data[o].power!=null){
                        $('#rate[prod="'+production+'"]').text(((data[o]['actualcomp']/data[o]['power'])*100).toFixed(1)+'%');
                    }
                    //停台时间转时分秒
                    //$('#prodstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['prodstop']));
                    //$('#equipstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['equipstop']));
                    //$('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['toolstop']));



                    //开始时间
                    if(data[o]['starttime']!='') {
                        var starttime = new Date(data[o]['starttime']).format("hh:mm:ss");
                        $('#starttime').text(starttime);
                    }



                    var stopsJO= jQuery.parseJSON(data[o]['stops']);


                    for(var key in stopsJO ){
                         if(stopsJO[key]=='01'){//设备异常
                            $('#'+key+'_01[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                            equipstop++;
                        }
                        else if(stopsJO[key]=='02'){//工装异常
                             $('#'+key+'_02[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                             toolstop++;
                        }
                        else if(stopsJO[key]=='03'){//生产异常
                             $('#'+key+'_03[prod="'+production+'"]').removeClass('bg-green').addClass('bg-green-red');
                             prodstop++;
                        }

                    }
                    clearInterval(prodstopInterval);
                    clearInterval(equipstopInterval);
                    clearInterval(toolstopInterval);
                    $('#prodstop[prod="'+production+'"]').text(sec_to_time(sessionStorage.getItem('prodstopTime')==null?0:sessionStorage.getItem('prodstopTime')));
                    $('#equipstop[prod="'+production+'"]').text(sec_to_time(sessionStorage.getItem('equipstopTime')==null?0:sessionStorage.getItem('equipstopTime')));
                    $('#toolstop[prod="'+production+'"]').text(sec_to_time(sessionStorage.getItem('toolstopTime')==null?0:sessionStorage.getItem('toolstopTime')));

                    if(equipstop>0){
                        $('#shebei[prod="'+production+'"]').addClass('bg-green-red');

                        equipstopInterval=setInterval(function (){
                            var equipstopTime=sessionStorage.getItem('equipstopTime');
                            if(equipstopTime==null){
                                equipstopTime=0;
                            }
                            else{
                                equipstopTime=Number(equipstopTime)+1;
                            }
                            sessionStorage.setItem('equipstopTime',equipstopTime);
                            $('#equipstop'+'[prod="'+production+'"]').text(sec_to_time(equipstopTime));
                            //$('#prodstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['prodstop']));
                            //$('#equipstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['equipstop']));
                            //$('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['toolstop']));
                        },1000);
                    }
                    if(prodstop>0){
                        $('#shengchan[prod="'+production+'"]').addClass('bg-green-red');
                        prodstopInterval=setInterval(function (){
                            var prodstopTime=sessionStorage.getItem('prodstopTime');
                            if(prodstopTime==null){
                                prodstopTime=0;
                            }
                            else{
                                prodstopTime=Number(prodstopTime)+1;
                            }
                            sessionStorage.setItem('prodstopTime',prodstopTime);
                            $('#prodstop'+'[prod="'+production+'"]').text(sec_to_time(prodstopTime));
                            //$('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['toolstop']));
                        },1000);
                    }
                    if(toolstop>0){
                        $('#gongzhuang[prod="'+production+'"]').addClass('bg-green-red');
                        toolstopInterval=setInterval(function (){
                            var toolstopTime=sessionStorage.getItem('toolstopTime');
                            if(toolstopTime==null){
                                toolstopTime=0;
                            }
                            else{
                                toolstopTime=Number(toolstopTime)+1;
                            }
                            sessionStorage.setItem('toolstopTime',toolstopTime);
                            $('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(toolstopTime));
                            //$('#toolstop'+'[prod="'+production+'"]').text(sec_to_time(data[o]['toolstop']));
                        },1000);
                    }

                    //第六条生产线
                    if(data[o]['required']!=''&&production=='6'){
                        var requiredJO= jQuery.parseJSON(data[o]['required']);
                        for(var key in requiredJO ){
                            $('#device'+requiredJO[key]['index']+'_01[prod="'+production+'"]').removeClass('bg-gray').addClass('bg-green');
                            $('#device'+requiredJO[key]['index']+'_02[prod="'+production+'"]').removeClass('bg-gray').addClass('bg-green');
                            $('#device'+requiredJO[key]['index']+'_03[prod="'+production+'"]').removeClass('bg-gray').addClass('bg-green');
                        }
                    }

                    //变化点
                    $('.box-body i').addClass('hidden');
                    var changedJO= jQuery.parseJSON(data[o]['changed']);
                    var redNum=0;
                    for(var k in changedJO ){
                        if(!$('#device'+k.replace('radio','').substr(0,1)+'_01[prod="'+production+'"]').hasClass('bg-gray')){
                            if(changedJO[k]=='red'){
                                redNum++;
                            }
                            $('#'+k+'[prod="'+production+'"]').removeClass('hidden').addClass('visible').addClass(changedJO[k]);
                        }

                    }
                    $('#changed[prod="'+production+'"]').text(redNum);
                    if(redNum==0){
                        $('#changedI').addClass('hidden');
                    }



                }
            }
            else{

            }

            //计划完成
            if(itemtime!=0){
                if(prodstop==0&&equipstop==0&&toolstop==0&&startflag=='1'){
                    yieldvarInterval=setInterval(function (){
                        yield+=1;
                        var params={};
                        params['id']=id;
                        params['yield']=yield;
                        updateProduction(params);

                    },itemtime*1000);
                }
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
            $('#yield').text(bodyParam['yield']);
            //alert("修改成功！");
            //window.location.reload();
            //window.location.href="interface.html?index="+interfaceIndex;
        }
    });
}
/**
 * 修改开始状态
 * @param id
 */
function startEndProduction(bodyParam){

    var httpR = new createHttpR(url+'startEndProduction','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        //var msg = obj['msg'];
        if(status=='0'){
            //$('#yield').text(bodyParam['yield']);
            //alert("修改成功！");
            window.location.reload();
            //window.location.href="interface.html?index="+interfaceIndex;
        }
    });
}
$('#startBtn').click(function () {
    statusParam.flag='1';
    statusParam.startstatus='1';
    startEndProduction(statusParam);
});
$('#endBtn').click(function () {
    statusParam.flag='0';
    statusParam.startstatus='1';
    startEndProduction(statusParam);
});

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
function  queryProduction (bodyParam,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;

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
                html+='<tr index='+o+' class="gradeX">\n'+
                    '<td>'+data[o].production+'</td>\n' +
                    '<td>'+data[o].plancomp+'</td>\n' +
                    '<td>'+data[o].actualcomp+'</td>\n' ;
                if(data[o].actualcomp!=''&&data[o].plancomp!=''){
                    html+='<td>'+((data[o].actualcomp/data[o].plancomp)*100).toFixed(0)+'</td>\n';
                }
                else{
                    html+='<td></td>\n';
                }
                html+='<td>'+data[o].prodtime+'</td>\n';

                if(data[o].actualcomp!=''&&data[o].power!=''&&data[o].power!=null){
                    html+='<td>'+((data[o].actualcomp/data[o].power)*100).toFixed(1)+'%</td>\n';
                }
                else{
                    html+='<td></td>\n';
                }
                html+='<td>'+data[o].adddate+'</td>\n' ;

                html+='</tr>';
            }
            $('#productionTbody').html(html);
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

 */
function  selectProdnum (type) {

    var bodyParam={};

    var httpR = new createHttpR(url+'allProdnum','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        if(status=='0'){
            var data= obj['msg'];
            var html='';
            html+='<option value="">全部</option>\n';
            if(type==0){
                for(var o in data){
                    html+='<option value="'+data[o].id+'" >'+data[o].production+'</option>\n';
                }
            }
            else if(type==1){
                for(var o in data){
                    if(data[o].id!='6'){
                        html+='<option value="'+data[o].id+'" >'+data[o].production+'</option>\n';
                    }

                }
            }
            else if(type==2){
                for(var o in data){
                    if(data[o].id=='6'){
                        html+='<option value="'+data[o].id+'" >'+data[o].production+'</option>\n';
                    }
                }
            }


            $('#production').html(html);

        }
    });
} /**
 * 时间秒数格式化
 * @param s 时间戳（单位：秒）
 * @returns {*} 格式化后的时分秒
 */
var sec_to_time = function(s) {
        var t;
        if(s > -1){
            var hour = Math.floor(s/3600);
            var min = Math.floor(s/60) % 60;
            var sec = s % 60;
            if(hour < 10) {
                t = '0'+ hour + ":";
            } else {
                t = hour + ":";
            }

            if(min < 10){t += "0";}
            t += min + ":";
            if(sec < 10){t += "0";}
            t += sec.toFixed(0);
        }
        return t;
    }