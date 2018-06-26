$(document).ready(function () {
    //按键切换
    $('body').on('keydown','.dom', function (e) {

        var key = e.which;
        switch (key) {
            case 38: //上


                break;
            case 40: //下

                break;
            case 37: //左(會導致輸入時無法使用左右移)
                e.preventDefault();
                var keyIndex=$(this).attr('keyIndex');
                $('.dom[keyIndex="'+Number(Number(keyIndex)-1)+'"]').focus();
                break;
            case 39: //右(會導致輸入時無法使用左右移)
                e.preventDefault();
                var keyIndex=$(this).attr('keyIndex');
                $('.dom[keyIndex="'+Number(Number(keyIndex)+1)+'"]').focus();
                break;
            case 13: //右(會導致輸入時無法使用左右移)
                e.preventDefault();
                var keyIndex=$(this).attr('keyIndex');
                $('.dom[keyIndex="'+Number(Number(keyIndex)+1)+'"]').focus();
                break;
            default:
                return;
        }

    });
});