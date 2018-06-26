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

    if($('#timeslot').val() == PrefixInteger(hour,2)+':'+PrefixInteger(minute,2)+':'+PrefixInteger(second,2)){
        clearInterval(interval);
        playEndSound();
    }


    //$('#repairTime').text("检修时间："+hour+'时'+minute+'分'+second+'秒'+millisecond+'毫秒')
    $('#timeSpan').text(PrefixInteger(hour,2)+'时'+PrefixInteger(minute,2)+'分'+PrefixInteger(second,2)+'秒');

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