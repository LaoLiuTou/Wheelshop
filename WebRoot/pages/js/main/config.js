//后台服务地址
var url = 'http://127.0.0.1/Wheelshop/';
//websocket地址
var websocketurl = 'ws://127.0.0.1:8888';

//后台服务地址
//var url = 'http://192.168.1.108/Wheelshop/';
//websocket地址
//var websocketurl = 'ws://192.168.1.108:8888';
//secret key
var sk = 'TTILY';

var yieldvarInterval;

$(document).ready(function(){
    //logo
    $('.logo').html(' <a href="index.html" style="padding-top: 10px;">生产信息系统</a>');
    $('footer').html("版权所有 © 2018  长春一汽富维汽车零部件股份有限公司车轮分公司");
    //$('.logo').html(' <a href=""><img src="images/logo.png" alt=""></a>');
    //$('.logo-icon').html(' <a href=""><img src="images/logo.png" alt=""></a>');
    $('#logoutBtn').click(function () {
        sessionStorage.clear();
        window.location.href='login.html';
    });

    var userinfo=sessionStorage.getItem('userinfo');
    if(userinfo!=null){
        $('#loginName').text(JSON.parse(userinfo)['nickname']);
        /*if(JSON.parse(userinfo)['id']!='1'){
            $('.custom-nav>li').last().hide();
        }*/

        ///////////////////////权限
        var state=JSON.parse(userinfo)['state'];
        if(state!='0'){
            $('#menu4').hide();
            $('#menu2 li').hide();
            $('#menu2-'+state).show();
        }
    }
    $('#menu4-1').hide();
    $('#menu4-2').hide();




});

/**
 * 登录
 */
function login() {
    $.ajax({
        url : url+'login',
        type : 'POST',
        data : {
            'username' : $('#username').val(),
            'password' : $('#password').val()
        },
        success : function(response) {
            console.log(JSON.stringify(response));
            if(response['status']=='0'){
                var token = response['token'];
                var userinfo = JSON.stringify(response['msg']);
                //var timestamp = Date.parse(new Date());
                //var hash = md5(token + timestamp + sk);
                sessionStorage.setItem('username',$('#username').val());
                sessionStorage.setItem('userpwd',$('#password').val());
                sessionStorage.setItem('userinfo',userinfo);
                sessionStorage.setItem('token',token);

                //window.location.href='default-page.html?backurl='+window.location.href;
                window.location.href='index.html';
            }
            else{
                alert(response['msg']);
            }

        },
        error : function(response) {
            alert('登录失败！');
        }
    });

}

/**
 * 大屏用户登录
 */
function guestLogin(prodction) {
    $.ajax({
        url : url+'login',
        type : 'POST',
        data : {
            'username' : 'guest',
            'password' : '123456'
        },
        success : function(response) {
            console.log(JSON.stringify(response));
            if(response['status']=='0'){
                var token = response['token'];
                var userinfo = JSON.stringify(response['msg']);
                //var timestamp = Date.parse(new Date());
                //var hash = md5(token + timestamp + sk);
                sessionStorage.setItem('username',$('#username').val());
                sessionStorage.setItem('userpwd',$('#password').val());
                sessionStorage.setItem('userinfo',userinfo);
                sessionStorage.setItem('token',token);

                lastProduction2(prodction);

                initwebsocket(prodction);
            }
            else{
                alert('链接服务失败！');
            }

        },
        error : function(response) {
            alert('链接服务失败！');
        }
    });

}

function createHttpR(url,type,dataType,bodyParam){
    this.url = url;
    this.type = type;
    this.dataType = dataType;
    this.bodyParam = bodyParam;
}
createHttpR.prototype.HttpRequest = function(callBack){

    if(sessionStorage.getItem('username')!=null||sessionStorage.getItem('token')!=null){
        var  token = sessionStorage.getItem('token');
        var timestamp = Date.parse(new Date());
        var hash = md5(token+timestamp+sk);
        $.ajax({
            url:this.url,
            type:this.type,
            cache:false,
            timeout:20,
            dataType:this.dataType,
            data :this.bodyParam,
            async:false,
            headers: {
                'token' : token,
                'timesamp' : timestamp,
                'sign' : hash
                //'content-Type': 'application/json'
            },
            success:function(response) {
                var obj = JSON.parse(response);
                var status = obj['status'];
                var msg = obj['msg'];
                if(status=='mismatch'||status=='expire'){
                    console.log(msg);
                    alert('验证信息错误，请重新登录！');
                    //无用户信息，重新登录
                    window.location.href='login.html';
                    //login();
                }
                else if(status=='0'){
                    callBack(response);
                }
                else{
                    alert(msg);
                }
            },
            error:function(response){
                alert('请求失败！');
                return false;
            },
            beforeSend:function(){
                //alert('before');
            },
            complete:function(){
                //alert('complete');
            }

        });
    }
    else{
        alert('访问权限已过期，请重新登录！');
        //无用户信息，重新登录
        window.location.href='login.html';
    }

}

function createJSONHttpR(url,type,dataType,bodyParam){
    this.url = url;
    this.type = type;
    this.dataType = dataType;
    this.bodyParam = bodyParam;
}
createJSONHttpR.prototype.HttpRequest = function(callBack){

    if(sessionStorage.getItem('username')!=null||sessionStorage.getItem('token')!=null){
        var  token = sessionStorage.getItem('token');
        var timestamp = Date.parse(new Date());
        var hash = md5(token+timestamp+sk);
        $.ajax({
            url:this.url,
            type:this.type,
            cache:false,
            timeout:20,
            dataType:this.dataType,
            data :this.bodyParam,
            async:false,
            headers: {
                'token' : token,
                'timesamp' : timestamp,
                'sign' : hash,
                'content-Type': 'application/json'
            },
            success:function(response) {
                var obj = JSON.parse(response);
                var status = obj['status'];
                var msg = obj['msg'];
                if(status=='mismatch'||status=='expire'){
                    console.log(msg);
                    alert('验证信息错误，请重新登录！');
                    //无用户信息，重新登录
                    window.location.href='login.html';
                    //login();
                }
                else if(status=='0'){
                    callBack(response);
                }
                else{
                    alert(msg);
                }
            },
            error:function(response){
                alert('请求失败！');
                return false;
            },
            beforeSend:function(){
                //alert('before');
            },
            complete:function(){
                //alert('complete');
            }

        });
    }
    else{
        alert('访问权限已过期，请重新登录！');
        //无用户信息，重新登录
        window.location.href='login.html';
    }

}


function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


function initwebsocket(type){

    var audio = new Audio();
    /*audioBZ.src = "audio/banzhang.mp3";
    audioBZ.loop = true;
    var audioGZ = new Audio();
    audioGZ.src = "audio/gongzhuang.mp3";
    audioGZ.loop = true;
    var audioSB = new Audio();
    audioSB.src = "audio/shebei.mp3";*/
    audio.loop = true;



    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket(websocketurl);
    }
    else {
        alert('当前浏览器不支持websocket')
    }


    //连接发生错误的回调方法
    websocket.onerror = function () {
        alert("WebSocket连接发生错误");
        //console.log('WebSocket连接成功');
    };
    //连接成功建立的回调方法
    websocket.onopen = function () {
        //alert("WebSocket连接成功");
        console.log('WebSocket连接成功');
        var sendMsg={"T":"1","UI":type+"_"+addNumber(6),"UN":type,"UH":""};
        websocket.send(JSON.stringify(sendMsg));
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        console.log(event.data);
        var msg=JSON.parse(event.data);

        if(msg['T']=='3'){

            //停止产量计数
            clearInterval(yieldvarInterval);

            if(msg['STATE']=='04'){//正常 取消按钮

                lastProduction2(msg['PRO']);

                $('#'+msg['NUM']+'_01[prod="'+msg['PRO']+'"]').removeClass('bg-green-red').addClass('bg-green');
                $('#'+msg['NUM']+'_02[prod="'+msg['PRO']+'"]').removeClass('bg-green-red').addClass('bg-green');
                $('#'+msg['NUM']+'_03[prod="'+msg['PRO']+'"]').removeClass('bg-green-red').addClass('bg-green');


                if($('.bg-green-red[id$="01"]').length==0){
                    $('#shebei[prod="'+msg['PRO']+'"]').removeClass('bg-green-red');
                    audio.pause();
                }
                if($('.bg-green-red[id$="02"]').length==0){
                    $('#gongzhuang[prod="'+msg['PRO']+'"]').removeClass('bg-green-red');
                    audio.pause();
                }
                if($('.bg-green-red[id$="03"]').length==0){
                    $('#shengchan[prod="'+msg['PRO']+'"]').removeClass('bg-green-red');
                    audio.pause();
                }

            }
            else if(msg['STATE']=='01'){//设备异常
                $('#'+msg['NUM']+'_01[prod="'+msg['PRO']+'"]').removeClass('bg-green').addClass('bg-green-red');
                //$('#equipstop[prod="'+msg['PRO']+'"]').text($('.bg-green-red[id$="01"][prod="'+msg['PRO']+'"]').length);
                $('#shebei[prod="'+msg['PRO']+'"]').addClass('bg-green-red');

                //alert(msg['PRO']+"/1/"+$('#'+msg['NUM']+'_02[prod="'+msg['PRO']+'"]').text()+".mp3");
                audio.src = "audio/"+msg['PRO']+"/1/"+$('#'+msg['NUM']+'_02[prod="'+msg['PRO']+'"]').text()+".mp3";
                audio.play();
            }
            else if(msg['STATE']=='02'){//工装异常
                $('#'+msg['NUM']+'_02[prod="'+msg['PRO']+'"]').removeClass('bg-green').addClass('bg-green-red');
                //$('#toolstop[prod="'+msg['PRO']+'"]').text($('.bg-green-red[id$="02"][prod="'+msg['PRO']+'"]').length);
                $('#gongzhuang[prod="'+msg['PRO']+'"]').addClass('bg-green-red');
                audio.src = "audio/"+msg['PRO']+"/2/"+$('#'+msg['NUM']+'_02[prod="'+msg['PRO']+'"]').text()+".mp3";
                audio.play();
            }
            else if(msg['STATE']=='03'){//生产异常
                $('#'+msg['NUM']+'_03[prod="'+msg['PRO']+'"]').removeClass('bg-green').addClass('bg-green-red');
                //$('#prodstop[prod="'+msg['PRO']+'"]').text($('.bg-green-red[id$="03"][prod="'+msg['PRO']+'"]').length);
                $('#shengchan[prod="'+msg['PRO']+'"]').addClass('bg-green-red');
                audio.src = "audio/"+msg['PRO']+"/3/"+$('#'+msg['NUM']+'_02[prod="'+msg['PRO']+'"]').text()+".mp3";
                audio.play();
            }
            else if(msg['STATE']=='05'){//关闭声音

                var audioSrc = audio.src;
                var tempSrc="/"+$('#'+msg['NUM']+'_02[prod="'+msg['PRO']+'"]').text()+".mp3";
                if(audioSrc.indexOf(tempSrc) != -1){

                    audio.pause();
                }


            }
            else{
                $('#'+msg['NUM']+'_'+msg['STATE']+'[prod="'+msg['PRO']+'"]').removeClass('bg-green-red').addClass('bg-green');
            }




        }
        else if(msg['T']=='4'){
            $('#actualcomp[prod="'+msg['PRO']+'"]').text(msg['AC']);
            if(msg['POWER']!=''){
                $('#rate[prod="'+msg['PRO']+'"]').text(((msg['AC']/msg['POWER'])*100).toFixed(0));
            }
        }
        else if(msg['T']=='5'){


            //lastProduction(1,msg['PRO']);
            var pageName="";
            if(msg['PRO']=='1'){
                if(msg['TYPE']=='生产时间'){
                    pageName='show_first1.html';
                }
                else if(msg['TYPE']=='检修时间'){
                    pageName='show_first2.html';
                }
                else if(msg['TYPE']=='换模时间'){
                    pageName='show_time.html?restType='+ escape('换模')+'&prod='+msg['PRO']+'&times='+msg['TIMES'];
                }

            }
            else if(msg['PRO']=='2'){
                if(msg['TYPE']=='生产时间'){
                    pageName='show_second1.html';
                }
                else if(msg['TYPE']=='检修时间'){
                    pageName='show_second2.html';
                }
                else if(msg['TYPE']=='换模时间'){
                    pageName='show_time.html?restType='+ escape('换模')+'&prod='+msg['PRO']+'&times='+msg['TIMES'];
                }
            }
            else if(msg['PRO']=='3'){
                if(msg['TYPE']=='生产时间'){
                    pageName='show_third1.html';
                }
                else if(msg['TYPE']=='检修时间'){
                    pageName='show_third2.html';
                }
                else if(msg['TYPE']=='换模时间'){
                    pageName='show_time.html?restType='+ escape('换模')+'&prod='+msg['PRO']+'&times='+msg['TIMES'];
                }
            }
            else if(msg['PRO']=='4'){
                if(msg['TYPE']=='生产时间'){
                    pageName='show_fourth1.html';
                }
                else if(msg['TYPE']=='检修时间'){
                    pageName='show_fourth2.html';
                }
                else if(msg['TYPE']=='换模时间'){
                    pageName='show_time.html?restType='+ escape('换模')+'&prod='+msg['PRO']+'&times='+msg['TIMES'];
                }
            }
            else if(msg['PRO']=='5'){
                if(msg['TYPE']=='生产时间'){
                    pageName='show_fifth1.html';
                }
                else if(msg['TYPE']=='检修时间'){
                    pageName='show_fifth2.html';
                }
                else if(msg['TYPE']=='换模时间'){
                    pageName='show_time.html?restType='+ escape('换模')+'&prod='+msg['PRO']+'&times='+msg['TIMES'];
                }
            }
            else if(msg['PRO']=='6'){
                if(msg['TYPE']=='生产时间'){
                    pageName='show_sixth1.html';
                }
                else if(msg['TYPE']=='检修时间'){
                    pageName='show_sixth2.html';
                }
                else if(msg['TYPE']=='换模时间'){
                    pageName='show_time.html?restType='+ escape('换模')+'&prod='+msg['PRO']+'&times='+msg['TIMES'];
                }
            }
            window.location.href=pageName;
        }
        else if(msg['T']=='6'){
            window.location.href='show_time.html?restType='+ escape(msg['TYPE'])+'&prod='+msg['PRO']+'&times='+msg['TIMES'];
        }
        else if(msg['T']=='7'){
            var pageName="";
            if(msg['PRO']=='1'){
                pageName='show_first1.html';
            }
            else if(msg['PRO']=='2'){
                pageName='show_second1.html';
            }
            else if(msg['PRO']=='3'){
                pageName='show_third1.html';
            }
            else if(msg['PRO']=='4'){
                pageName='show_fourth1.html';
            }
            else if(msg['PRO']=='5'){
                pageName='show_fifth1.html';
            }
            else if(msg['PRO']=='6'){
                pageName='show_sixth1.html';
            }

            window.location.href=pageName;
        }

    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        //setMessageInnerHTML("WebSocket连接关闭");
        console.log('WebSocket连接关闭');
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
    }

}
function addNumber(_idx){
    var str = '';
    for(var i = 0; i < _idx; i += 1){
        str += Math.floor(Math.random() * 10);
    }
    return str;
}


///////////
//初始化变量
/*var interval;
var hour,minute,second;//时 分 秒
hour=minute=second=0;//初始化
var millisecond=0;//毫秒
//计时函数
function timer()
{
    millisecond=millisecond+1000;
    if(millisecond>=1000)
    {
        millisecond=0;
        second=second+1;
    }
    if(second>=60)
    {
        second=0;
        minute=minute+1;
    }

    if(minute>=60)
    {
        minute=0;
        hour=hour+1;
    }

    /!*if($('#timeslot').val() == PrefixInteger(hour,2)+':'+PrefixInteger(minute,2)+':'+PrefixInteger(second,2)){
        clearInterval(interval);
        playEndSound();
    }*!/

    //$('#repairTime').text("检修时间："+hour+'时'+minute+'分'+second+'秒'+millisecond+'毫秒')
    $('#timeSpan').text(PrefixInteger(hour,2)+'时'+PrefixInteger(minute,2)+'分'+PrefixInteger(second,2)+'秒');

}*/
function PrefixInteger(num, length) {
    return (Array(length).join('0') + num).slice(-length);
}
//////////////////

Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}

/////////
function playStartSound(){
    var audio = new Audio();
    if (audio.canPlayType("audio/mp3")) {
        audio.src = "audio/audio.mp3";
    }else if(audio.canPlayType("audio/ogg")) {
        audio.src = "audio/audio.ogg";
    }
    audio.loop = true;
    //播放(继续播放)
    audio.play();
}
function playEndSound(){
    var audio = new Audio();
    if (audio.canPlayType("audio/mp3")) {
        audio.src = "audio/audio.mp3";
    }else if(audio.canPlayType("audio/ogg")) {
        audio.src = "audio/audio.ogg";
    }
    //播放(继续播放)
    audio.play();
}


///////////
//初始化变量
var interval;
var hour,minute,second;//时 分 秒
hour=minute=second=0;//初始化
var millisecond=0;//毫秒
//计时函数
function timer()
{
    millisecond=millisecond+1000;
    if(millisecond>=1000)
    {
        millisecond=0;
        second=second+1;
    }
    if(second>=60)
    {
        second=0;
        minute=minute+1;
    }

    if(minute>=60)
    {
        minute=0;
        hour=hour+1;
    }

    /*if($('#timeslot').val() == PrefixInteger(hour,2)+':'+PrefixInteger(minute,2)+':'+PrefixInteger(second,2)){
        clearInterval(interval);
        playEndSound();
    }*/


    //$('#repairTime').text("检修时间："+hour+'时'+minute+'分'+second+'秒'+millisecond+'毫秒')
    $('#timeSpan').text(PrefixInteger(hour,2)+':'+PrefixInteger(minute,2)+':'+PrefixInteger(second,2));

}
function PrefixInteger(num, length) {
    return (Array(length).join('0') + num).slice(-length);
}
//////////////////

Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}

/////////
function playStartSound(){
    var audio = new Audio();
    if (audio.canPlayType("audio/mp3")) {
        audio.src = "audio/audio.mp3";
    }else if(audio.canPlayType("audio/ogg")) {
        audio.src = "audio/audio.ogg";
    }
    audio.loop = true;
    //播放(继续播放)
    audio.play();
}
function playEndSound(){
    var audio = new Audio();
    if (audio.canPlayType("audio/mp3")) {
        audio.src = "audio/audio.mp3";
    }else if(audio.canPlayType("audio/ogg")) {
        audio.src = "audio/audio.ogg";
    }
    //播放(继续播放)
    audio.play();
}

function setMenu(par,sub){
    var menuStr='<ul class="nav nav-pills nav-stacked custom-nav">\n' +
        '      <li id="menu1"><a href="index.html"><i class="fa fa-home"></i> <span>首页</span></a></li>\n' +
        '      <li id="menu2" class="menu-list"><a href=""><i class="fa fa-desktop"></i> <span>生产大屏控制</span></a>\n' +
        '          <ul class="sub-menu-list">\n' +
        '               <li id="menu2_1"><a href="ws_first1.html">旋压A线</a></li>\n' +
        '               <li id="menu2_2"><a href="ws_second1.html">旋压B线</a></li>\n' +
        '               <li id="menu2_3"><a href="ws_third1.html">滚型轮辋</a></li>\n' +
        '               <li id="menu2_4"><a href="ws_fourth1.html">型钢轮辋</a></li>\n' +
        '               <li id="menu2_5"><a href="ws_fifth1.html">旋压轮辐</a></li>\n' +
        '               <li id="menu2_6"><a href="ws_sixth1.html">滚型轮辐</a></li>\n' +
        '           </ul>\n' +
        '       </li>\n' +
        '       <li id="menu3" class="menu-list"><a href=""><i class="fa fa-bar-chart-o"></i> <span>生产数据统计</span></a>\n' +
        '          <ul class="sub-menu-list">\n' +
        '               <li id="menu3_1"><a href="ws_statistics.html">停台数据统计</a></li>\n' +
        '               <li id="menu3_2"><a href="ws_statistics.html">生产完成统计</a></li>\n' +
        '          </ul>\n' +
        '       </li>\n' +
        '       <li id="menu4" class="menu-list"><a href=""><i class="fa fa-cogs"></i> <span>系统设置</span></a>\n' +
        '           <ul class="sub-menu-list">\n' +
        '               <li id="menu4_1"><a href="ws_user.html">账号管理</a></li>\n' +
        '               <li id="menu4_2"><a href="ws_permission.html">权限管理</a></li>\n' +
        '               <li id="menu4_3"><a href="ws_datamanager.html">数据管理</a></li>\n' +
        '               <li id="menu4_4"><a href="ws_timer.html">时间管理</a></li>\n' +
        '           </ul>\n' +
        '       </li>\n' +
        '</ul>';
    $('#menu').html(menuStr);

    $('#menu'+par).addClass("active");
    $('#menu'+par+'_'+sub).addClass("active");




}