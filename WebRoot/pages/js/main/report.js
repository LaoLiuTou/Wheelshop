$(document).ready(function(){


});


////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////统计管理////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////

/**
 * 统计库存
 * @param c_dt
 * @param stocktype
 * @param workshop
 */
function  reportListStock (bodyParam,currentPage,pageSize) {

    var showPage=7;
    var httpR = new createHttpR(url+'listStock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];

        if(status=='0'){
            var data=msg['data'];
            var html='';
            var sum_1=0,sum_2=0,sum_3=0,sum_4=0,sum_5=0;
            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);
            for(var o in subData){
                var msizes=subData[o].msize.split('*');
                sum_1=Number(sum_1)+Number(subData[o].number);
                sum_2=(Number(sum_2)+Number(msizes[0]*msizes[1]*subData[o].number/1000000)).toFixed(2);
                sum_3=(Number(sum_3)+Number(subData[o].sprice)).toFixed(3);
                sum_4=(Number(sum_4)+Number(subData[o].ssum)).toFixed(0);
                var length=msizes[0],width=msizes[1];
                if(msizes[0]%300>0){
                    length=msizes[0]-msizes[0]%300+300;
                }
                if(msizes[1]%300>0){
                    width=msizes[1]-msizes[1]%300+300;
                }
                sum_5=(Number(sum_5)+Number(length*width*subData[o].number/1000000)).toFixed(2);
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+(Number(o)+1)+'</td>\n' +
                    '<td>'+subData[o].m_dt+'</td>\n' +
                    '<td>'+subData[o].code+'</td>\n' +
                    '<td>'+subData[o].process+'</td>\n' +
                    '<td>'+subData[o].msize+'</td>\n' +
                    '<td>'+subData[o].height+'</td>\n' +
                    '<td>'+subData[o].number+'</td>\n' +
                    '<td>'+(msizes[0]*msizes[1]*subData[o].number/1000000).toFixed(2)+'</td>\n' +
                    //'<td>'+data[o].maochi+'</td>\n' +
                    '<td>'+(length*width*subData[o].number/1000000).toFixed(2)+'</td>\n' +
                    '<td>'+subData[o].comment+'</td>\n' +
                    '<td>'+subData[o].worker+'</td>\n' +
                    '<td>'+subData[o].auditor+'</td>\n' +
                    '<td>'+Number(subData[o].sprice).toFixed(3)+'</td>\n' +
                    '<td>'+Number(subData[o].ssum).toFixed(0)+'</td>\n' +
                    '</tr>' ;

            }
            html+='<tr  class="gradeX">\n' +
                '<td>合计</td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_1+'</td>\n' +
                '<td>'+sum_2+'</td>\n' +
                '<td>'+sum_5+'</td>\n' +

                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_4+'</td>\n' +
                '</tr>';
            //总合计
            if(data.length>pageSize) {
                sum_1 = 0, sum_2 = 0, sum_3 = 0, sum_4 = 0, sum_5 = 0;
                for (var o in data) {
                    var msizes = data[o].msize.split('*');
                    sum_1 = Number(sum_1) + Number(data[o].number);
                    sum_2 = (Number(sum_2) + Number(msizes[0] * msizes[1] * data[o].number / 1000000)).toFixed(2);
                    sum_3 = (Number(sum_3) + Number(data[o].sprice)).toFixed(2);
                    sum_4 = (Number(sum_4) + Number(data[o].ssum)).toFixed(2);
                    var length = msizes[0], width = msizes[1];
                    if (msizes[0] % 300 > 0) {
                        length = msizes[0] - msizes[0] % 300 + 300;
                    }
                    if (msizes[1] % 300 > 0) {
                        width = msizes[1] - msizes[1] % 300 + 300;
                    }
                    sum_5 = (Number(sum_5) + Number(length * width * data[o].number / 1000000)).toFixed(2);
                }
                html += '<tr  class="gradeX">\n' +
                    '<td>总合计</td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_1 + '</td>\n' +
                    '<td>' + sum_2 + '</td>\n' +
                    '<td>' + sum_5 + '</td>\n' +

                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_3 + '</td>\n' +
                    '<td>' + sum_4 + '</td>\n' +
                    '</tr>';
            }

            var num=data.length;
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

            $('#reportTbody').html(html);


        }
    });
}
/**
 * 统计库存
 * @param c_dt
 * @param stocktype
 * @param workshop
 */
function  reportReceiveListStock (bodyParam,currentPage,pageSize) {

    var showPage=7;
    var httpR = new createHttpR(url+'listStock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];

        if(status=='0'){
            var data=msg['data'];
            var html='';

            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);
            var sum_1=0,sum_2=0;
            for(var o in subData){
                var msizes=subData[o].msize.split('*');
                if(subData[o].number!=''){
                    sum_1=Number(sum_1)+Number(subData[o].number);
                    if(subData[o].msize!=''){
                        sum_2=(Number(sum_2)+Number(msizes[0]*msizes[1]*subData[o].number/1000000)).toFixed(2);
                    }

                }

                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+(Number(o)+1)+'</td>\n' +
                    '<td>'+subData[o].m_dt+'</td>\n' +
                    '<td>'+subData[o].code+'</td>\n' +
                    '<td>'+subData[o].outtype+'</td>\n' +
                    '<td>'+subData[o].material_name+'</td>\n' +
                    '<td>'+subData[o].msize+'</td>\n' +
                    '<td>'+subData[o].height+'</td>\n' +
                    '<td>'+subData[o].number+'</td>\n' +
                    '<td>'+(msizes[0]*msizes[1]*subData[o].number/1000000).toFixed(2)+'</td>\n' +
                    //'<td>'+data[o].maochi+'</td>\n' +
                    '<td>'+subData[o].comment+'</td>\n' +
                    '<td>'+subData[o].worker+'</td>\n' +
                    '<td>'+subData[o].auditor+'</td>\n' +
                    '</tr>' ;

            }
            html+='<tr  class="gradeX">\n' +
                '<td>合计</td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_1+'</td>\n' +
                '<td>'+sum_2+'</td>\n' +

                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '</tr>';






            //总合计
            if(data.length>pageSize) {
                sum_1=0,sum_2=0;
                for(var o in data){
                    var msizes=data[o].msize.split('*');
                    if(data[o].number!=''){
                        sum_1=Number(sum_1)+Number(data[o].number);
                        if(data[o].msize!=''){
                            sum_2=(Number(sum_2)+Number(msizes[0]*msizes[1]*data[o].number/1000000)).toFixed(2);
                        }

                    }
                }
                html+='<tr  class="gradeX">\n' +
                    '<td>总合计</td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>'+sum_1+'</td>\n' +
                    '<td>'+sum_2+'</td>\n' +

                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '</tr>';
            }

            var num=data.length;
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


            $('#reportTbody').html(html);


        }
    });
}
/**
 * 统计库存
 * @param c_dt
 * @param stocktype
 * @param workshop
 */
function  reportQJListStock (bodyParam,currentPage,pageSize) {

    var showPage=7;
    var httpR = new createHttpR(url+'listStock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];

        if(status=='0'){
            var data=msg['data'];
            var html='';
            var sum_1=0,sum_2=0,sum_3=0,sum_4=0,sum_5=0,sum_6=0;
            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);
            for(var o in subData){
                var msizes=subData[o].msize.split('*');
                sum_1=Number(sum_1)+Number(subData[o].number);
                sum_2=(Number(sum_2)+Number(msizes[0]*msizes[1]*subData[o].number/1000000)).toFixed(2);
                sum_3=(Number(sum_3)+Number(subData[o].sprice)).toFixed(3);
                sum_4=(Number(sum_4)+Number(subData[o].ssum)).toFixed(0);
                var length=msizes[0],width=msizes[1];
                if(msizes[0]%300>0){
                    length=msizes[0]-msizes[0]%300+300;
                }
                if(msizes[1]%300>0){
                    width=msizes[1]-msizes[1]%300+300;
                }
                sum_5=(Number(sum_5)+Number(length*width*subData[o].number/1000000)).toFixed(2);

                sum_6=(Number(sum_6)+Number(subData[o].yanmi)).toFixed(2);

                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+(Number(o)+1)+'</td>\n' +
                    '<td>'+subData[o].m_dt+'</td>\n' +
                    '<td>'+subData[o].code+'</td>\n' +
                    '<td>'+subData[o].process+'</td>\n' +
                    '<td>'+subData[o].msize+'</td>\n' +
                    '<td>'+subData[o].height+'</td>\n' +
                    '<td>'+subData[o].number+'</td>\n' +
                    '<td>'+(msizes[0]*msizes[1]*subData[o].number/1000000).toFixed(2)+'</td>\n' +
                    //'<td>'+data[o].maochi+'</td>\n' +
                    '<td>'+(length*width*subData[o].number/1000000).toFixed(2)+'</td>\n' +
                    '<td>'+subData[o].comment+'</td>\n' +
                    '<td>'+subData[o].worker+'</td>\n' +
                    '<td>'+subData[o].auditor+'</td>\n' +
                    '<td>'+subData[o].yanmi+'</td>\n' +
                    '<td>'+Number(subData[o].sprice).toFixed(3)+'</td>\n' +
                    '<td>'+Number(subData[o].ssum).toFixed(0)+'</td>\n' +
                    '</tr>' ;

            }
            html+='<tr  class="gradeX">\n' +
                '<td>合计</td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_1+'</td>\n' +
                '<td>'+sum_2+'</td>\n' +
                '<td>'+sum_5+'</td>\n' +

                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_6+'</td>\n' +
                '<td></td>\n' +
                '<td>'+sum_4+'</td>\n' +
                '</tr>';
            //总合计
            if(data.length>pageSize) {
                sum_1 = 0, sum_2 = 0, sum_3 = 0, sum_4 = 0, sum_5 = 0;
                for (var o in data) {
                    var msizes = data[o].msize.split('*');
                    sum_1 = Number(sum_1) + Number(data[o].number);
                    sum_2 = (Number(sum_2) + Number(msizes[0] * msizes[1] * data[o].number / 1000000)).toFixed(2);
                    sum_3 = (Number(sum_3) + Number(data[o].sprice)).toFixed(2);
                    sum_4 = (Number(sum_4) + Number(data[o].ssum)).toFixed(2);
                    var length = msizes[0], width = msizes[1];
                    if (msizes[0] % 300 > 0) {
                        length = msizes[0] - msizes[0] % 300 + 300;
                    }
                    if (msizes[1] % 300 > 0) {
                        width = msizes[1] - msizes[1] % 300 + 300;
                    }
                    sum_5 = (Number(sum_5) + Number(length * width * data[o].number / 1000000)).toFixed(2);
                    var length = msizes[0], width = msizes[1];
                    if (msizes[0] % 300 > 0) {
                        length = msizes[0] - msizes[0] % 300 + 300;
                    }
                    if (msizes[1] % 300 > 0) {
                        width = msizes[1] - msizes[1] % 300 + 300;
                    }


                }
                html += '<tr  class="gradeX">\n' +
                    '<td>总合计</td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_1 + '</td>\n' +
                    '<td>' + sum_2 + '</td>\n' +
                    '<td>' + sum_5 + '</td>\n' +

                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_3 + '</td>\n' +
                    '<td>' + sum_4 + '</td>\n' +
                    '</tr>';
            }
            $('#reportTbody').html(html);
            var num=data.length;
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
 * 统计库存
 * @param c_dt
 * @param stocktype
 * @param workshop
 */
function  reportWJGListStock (bodyParam,currentPage,pageSize) {

    var showPage=7;
    var httpR = new createHttpR(url+'listStock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];

        if(status=='0'){
            var data=msg['data'];
            var html='';
            var sum_1=0,sum_2=0,sum_3=0,sum_4=0,sum_5=0;
            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);
            for(var o in subData){
                var msizes=subData[o].msize.split('*');
                sum_1=Number(sum_1)+Number(subData[o].number);
                if(msizes!=''&&data[o].number!='') {
                    sum_2 = (Number(sum_2) + Number(msizes[0] * msizes[1] * subData[o].number / 1000000)).toFixed(2);
                }
                sum_3=(Number(sum_3)+Number(subData[o].sprice)).toFixed(3);
                sum_4=(Number(sum_4)+Number(subData[o].ssum)).toFixed(0);
                sum_5=(Number(sum_5)+Number(subData[o].yanmi)).toFixed(2);
                var length=msizes[0],width=msizes[1];
                if(msizes[0]%300>0){
                    length=msizes[0]-msizes[0]%300+300;
                }
                if(msizes[1]%300>0){
                    width=msizes[1]-msizes[1]%300+300;
                }
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+(Number(o)+1)+'</td>\n' +
                    '<td>'+subData[o].m_dt+'</td>\n' +
                    '<td>'+subData[o].code+'</td>\n' +
                    '<td>'+subData[o].process+'</td>\n' +
                    '<td>'+subData[o].msize+'</td>\n' +
                    '<td>'+subData[o].height+'</td>\n' +
                    '<td>'+subData[o].number+'</td>\n' ;
                if(msizes!=''&&subData[o].number!=''){
                    html+='<td>'+(msizes[0]*msizes[1]*subData[o].number/1000000).toFixed(2)+'</td>\n' ;
                }
                else{
                    html+='<td></td>\n' ;
                }

                html+='<td>'+subData[o].yanmi+'</td>\n' +
                    //'<td>'+data[o].maochi+'</td>\n' +
                    //'<td>'+(length*width*data[o].number/1000000).toFixed(2)+'</td>\n' +
                    '<td>'+subData[o].comment+'</td>\n' +
                    '<td>'+subData[o].worker+'</td>\n' +
                    '<td>'+subData[o].auditor+'</td>\n' +

                    '<td>'+Number(subData[o].sprice).toFixed(3)+'</td>\n' +
                    '<td>'+Number(subData[o].ssum).toFixed(0)+'</td>\n' +
                    '</tr>' ;

            }
            html+='<tr  class="gradeX">\n' +
                '<td>合计</td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_1+'</td>\n' +
                '<td>'+sum_2+'</td>\n' +
                '<td>'+sum_5+'</td>\n' +

                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_4+'</td>\n' +
                '</tr>';

            //总合计
            if(data.length>pageSize){
                sum_1=0,sum_2=0,sum_3=0,sum_4=0,sum_5=0;
                for(var o in data){
                    var msizes=data[o].msize.split('*');
                    sum_1=Number(sum_1)+Number(data[o].number);
                    if(msizes!=''&&data[o].number!='') {
                        sum_2 = (Number(sum_2) + Number(msizes[0] * msizes[1] * data[o].number / 1000000)).toFixed(2);
                    }
                    sum_3=(Number(sum_3)+Number(data[o].sprice)).toFixed(2);
                    sum_4=(Number(sum_4)+Number(data[o].ssum)).toFixed(2);
                    sum_5=(Number(sum_5)+Number(data[o].yanmi)).toFixed(2);
                    var length=msizes[0],width=msizes[1];
                    if(msizes[0]%300>0){
                        length=msizes[0]-msizes[0]%300+300;
                    }
                    if(msizes[1]%300>0){
                        width=msizes[1]-msizes[1]%300+300;
                    }

                }
                html+='<tr  class="gradeX">\n' +
                    '<td>总合计</td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>'+sum_1+'</td>\n' +
                    '<td>'+sum_2+'</td>\n' +
                    '<td>'+sum_5+'</td>\n' +

                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>'+sum_3+'</td>\n' +
                    '<td>'+sum_4+'</td>\n' +
                    '</tr>';
            }


            $('#reportTbody').html(html);

            var num=data.length;
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
 * 统计库存
 * @param c_dt
 * @param stocktype
 * @param workshop
 */
function  reportStock (c_dt,stocktype,workshop) {
    var bodyParam={};
    if(c_dt!=''){
        bodyParam['c_dtFrom']=c_dt+' 00:00:00';
        bodyParam['c_dtTo']=c_dt+' 23:59:59';
    }
    if(stocktype!=''){
        bodyParam['stocktype']=stocktype;
    }
    if(workshop!=''){
        bodyParam['workshop']=workshop;
    }
    var httpR = new createHttpR(url+'reportStock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var data = obj['msg'];
        if(status=='0'){
            var html='';
            for(var o in data){
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+data[o].material_name+'</td>\n' +
                    '<td>'+data[o].height+'</td>\n' +
                    '<td>'+data[o].msize+'</td>\n' +
                    '<td>'+data[o].unit+'</td>\n' +
                    '<td>'+data[o].sum_number+'</td>\n</tr>' ;
            }
            $('#reportTbody').html(html);
            selectWorkshop(1,100,workshop);
            selectStocktype(1,100,stocktype);
        }
    });
}
/**
 * 出入库明细
 * @param c_dt
 * @param stocktype
 * @param workshop
 */
function  reportStockInOut (stocktype,workshop,material) {
    var bodyParam={};

    if(stocktype!=''){
        bodyParam['stocktype']=stocktype;
    }
    if(workshop!=''){
        bodyParam['workshop']=workshop;
    }
    if(material!=''){
        bodyParam['material']=material;
    }
    var httpR = new createHttpR(url+'reportStockInOut','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var data = obj['msg'];
        if(status=='0'){
            var html='';
            for(var o in data){
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+data[o].c_dt+'</td>\n' +
                    '<td>'+data[o].height+'</td>\n' +
                    '<td>'+data[o].msize+'</td>\n' +
                    '<td>'+data[o].unit+'</td>\n' +
                    '<td>'+Math.abs(data[o].sum_in)+'</td>\n' +
                    '<td>'+Math.abs(data[o].sum_out)+'</td>\n</tr>' ;
            }
            $('#reportTbody').html(html);
            selectWorkshop(1,100,workshop);
            selectMaterial(1,100,material);
            selectStocktype(1,100,stocktype);
        }
    });
}
/**
 * 车间进出配比
 * @param c_dt
 * @param stocktype
 * @param workshop
 */
function  reportWorkshopInOut (fromm_dt,tom_dt,outtype,currentPage,pageSize) {
    var showPage=7;
    var bodyParam={'pid':0};
    if(fromm_dt!=''){
        bodyParam['m_dtFrom']=fromm_dt+' 00:00:00';
    }
    if(tom_dt!=''){
        bodyParam['m_dtTo']=tom_dt+' 23:59:59';
    }
    if(outtype!=''){
        bodyParam['outtype']=outtype;
    }
    var httpR = new createHttpR(url+'reportWorkshopInOut','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var data = obj['msg'];
        if(status=='0'){
            var html='';
            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);

            for(var o in subData){
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+subData[o].outtype+'</td>\n' +
                    '<td>'+subData[o].unit+'</td>\n' +
                    '<td>'+Math.abs(subData[o].sum_in).toFixed(2)+'</td>\n' +
                    '<td>'+(Math.abs(subData[o].sum_out)-Math.abs(subData[o].damage_num)).toFixed(2)+'</td>\n' +
                    '<td>'+Math.abs(subData[o].damage_num).toFixed(2)+'</td>\n' +
                    '<td>'+(Math.abs(subData[o].sum_in)-Math.abs(subData[o].sum_out)).toFixed(2)+'</td>\n' +
                    '<td>'+Math.abs(subData[o].outside_sum).toFixed(2)+'</td>\n' +
                    '<td>'+((Math.abs(subData[o].sum_out)-Math.abs(subData[o].damage_num))+Math.abs(subData[o].outside_sum)).toFixed(2)+'</td>\n' +
                    '</tr>' ;
            }
            $('#reportTbody').html(html);
            var num=data.length;
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

            selectDistinctOuttype(outtype);
        }
    });
}
/**
 * 大锯产量统计
 * @param m_dt
 * @param currentPage
 * @param pageSize
 */
function  reportMatteboard (from_dt,to_dt,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;
    var bodyParam={};
    if(from_dt!=''){
        bodyParam['m_dtFrom']=from_dt+' 00:00:00';
    }
    if(to_dt!=''){
        bodyParam['m_dtTo']=to_dt+' 23:59:59';
    }

    var httpR = new createHttpR(url+'reportMatteboard','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            var html='';
            var sum_1=0,sum_2=0,sum_3=0,sum_4=0,sum_5=0,sum_6=0;

            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);
            for(var o in subData){
                sum_1=(Number(sum_1)+Number(subData[o].sum_blocknumber)).toFixed(2);
                sum_2=(Number(sum_2)+Number(subData[o].sum_square)).toFixed(2);
                sum_3=(Number(sum_3)+Number(subData[o].sum_belowgradeblock)).toFixed(2);
                sum_4=(Number(sum_4)+Number(subData[o].sum_belowgradesquare)).toFixed(2);
                sum_5=(Number(sum_5)+Number(subData[o].sum_gradeblock)).toFixed(2);
                sum_6=(Number(sum_6)+Number(subData[o].sum_gradesquare)).toFixed(2);
                html+='<tr index='+o+' class="gradeX">\n' +
                    //'<td>'+subData[o].m_dt+'</td>\n' +
                    '<td>'+subData[o].height+'</td>\n' +
                    '<td>'+subData[o].sum_blocknumber+'</td>\n' +
                    '<td>'+subData[o].sum_square+'</td>\n' +
                    '<td>'+subData[o].sum_belowgradeblock+'</td>\n' +
                    '<td>'+subData[o].sum_belowgradesquare+'</td>\n' +
                    '<td>'+subData[o].sum_gradeblock+'</td>\n' +
                    '<td>'+subData[o].sum_gradesquare+'</td>\n' +
                    '</tr>';
            }
            html+='<tr  class="gradeX">\n' +
                '<td>合计</td>\n' +
                //'<td></td>\n' +
                '<td>'+sum_1+'</td>\n' +
                '<td>'+sum_2+'</td>\n' +
                '<td>'+sum_3+'</td>\n' +
                '<td>'+sum_4+'</td>\n' +
                '<td>'+sum_5+'</td>\n' +
                '<td>'+sum_6+'</td>\n' +
                '</tr>';
            //总合计
            if(data.length>pageSize) {
                sum_1 = 0, sum_2 = 0, sum_3 = 0, sum_4 = 0, sum_5 = 0, sum_6 = 0;
                for (var o in data) {
                    sum_1 = (Number(sum_1) + Number(data[o].sum_blocknumber)).toFixed(2);
                    sum_2 = (Number(sum_2) + Number(data[o].sum_square)).toFixed(2);
                    sum_3 = (Number(sum_3) + Number(data[o].sum_belowgradeblock)).toFixed(2);
                    sum_4 = (Number(sum_4) + Number(data[o].sum_belowgradesquare)).toFixed(2);
                    sum_5 = (Number(sum_5) + Number(data[o].sum_gradeblock)).toFixed(2);
                    sum_6 = (Number(sum_6) + Number(data[o].sum_gradesquare)).toFixed(2);

                }
                html += '<tr  class="gradeX">\n' +
                    '<td>总合计</td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_1 + '</td>\n' +
                    '<td>' + sum_2 + '</td>\n' +
                    '<td>' + sum_3 + '</td>\n' +
                    '<td>' + sum_4 + '</td>\n' +
                    '<td>' + sum_5 + '</td>\n' +
                    '<td>' + sum_6 + '</td>\n' +
                    '</tr>';
            }
            $('#reportTbody').html(html);


            var num=data.length;
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
 * 日报
 * @param c_dt
 * @param workshop
 */
function  reportYield (c_dt,outtype,currentPage,pageSize) {

    var showPage=7;
    var bodyParam={'pid':0,'state':1};
    if(c_dt!=''){
        bodyParam['m_dtFrom']=c_dt+' 00:00:00';
        bodyParam['m_dtTo']=c_dt+' 23:59:59';
    }
    if(outtype!=''){
        bodyParam['outtype']=outtype;
    }
    var httpR = new createHttpR(url+'reportYield','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var data = obj['msg'];

        if(status=='0'){
            var html='';
            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);
            for(var o in subData){

                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+subData[o].outtype+'</td>\n' +
                    '<td>'+subData[o].height+'</td>\n' +
                    '<td>'+subData[o].unit+'</td>\n' +
                    /*'<td>'+(subData[o].sum_in*data[o].msize.split('*')[0]*subData[o].msize.split('*')[1]/1000000).toFixed(2)+'</td>\n' +
                    '<td>'+(subData[o].sum_out*data[o].msize.split('*')[0]*subData[o].msize.split('*')[1]/1000000).toFixed(2)+'</td>\n' +*/
                    '<td>'+Number(subData[o].sum_in).toFixed(2)+'</td>\n' +
                    '<td>'+Number(subData[o].sum_out).toFixed(2)+'</td>\n' +
                    '<td>'+Number(subData[o].sum_number).toFixed(2)+'</td>\n' +
                    '</tr>' ;
            }



            var num=data.length;
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





            $('#reportTbody').html(html);
            selectDistinctOuttype(outtype);
            //selectWorkshop(1,100,workshop);
        }
    });
}
/**
 * 破损
 * @param c_dt
 * @param workshop
 */
function  reportDamage (c_dt,workshop) {
    var bodyParam={};
    if(c_dt!=''){
        bodyParam['c_dtFrom']=c_dt+' 00:00:00';
        bodyParam['c_dtTo']=c_dt+' 23:59:59';
    }
    if(workshop!=''){
        bodyParam['workshop']=workshop;
    }
    bodyParam['damage']='是';
    var httpR = new createHttpR(url+'reportDamage','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var data = obj['msg'];
        if(status=='0'){
            var html='';
            for(var o in data){
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+data[o].height+'</td>\n' +
                    '<td>'+data[o].unit+'</td>\n' +
                    '<td>'+data[o].sum_out+'</td></tr>' ;
            }

            $('#reportTbody').html(html);
            selectWorkshop(1,100,workshop);
        }
    });
}
/**
 * 车间select

 * @param currentPage
 * @param pageSize
 */
function  selectWorkshop (currentPage,pageSize,workshop) {
    var bodyParam={'page':currentPage,'size':pageSize,'state':'0'};
    var httpR = new createHttpR(url+'listWorkshop','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            var html='<option value="">全部</option>';
            for(var o in data){
                html+='<option value="'+data[o].id+'">'+data[o].name+'</option>\n';
            }
            $('#searchWorkshop').html(html);
            $('#searchWorkshop').val(workshop);
        }
    });
}
/**
 * 物品select
 * @param currentPage
 * @param pageSize
 */
function  selectMaterial (currentPage,pageSize,material) {
    var bodyParam={'page':currentPage,'size':pageSize,'state':'0'};
    var httpR = new createHttpR(url+'listMaterial','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            var html='<option value="">全部</option>';
            for(var o in data){
                html+='<option value="'+data[o].id+'">'+data[o].name+'</option>\n';
            }
            $('#searchMaterial').html(html);
            $('#searchMaterial').val(material);
        }
    });
}

/**
 * 库别select
 * @param currentPage
 * @param pageSize
 */
function  selectStocktype (currentPage,pageSize,stocktype) {
    var bodyParam={'page':currentPage,'size':pageSize,'state':'0'};
    var httpR = new createHttpR(url+'listStocktype','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            var html='<option value="">全部</option>';
            for(var o in data){
                html+='<option value="'+data[o].id+'">'+data[o].name+'</option>\n';
            }
            $('#searchStocktype').html(html);
            $('#searchStocktype').val(stocktype);
        }
    });
}/**
 * 唯一类型
 */
function  selectDistinctOuttype (outtype) {
    var bodyParam={};
    var httpR = new createHttpR(url+'distinctType','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var data = obj['msg'];

        if(status=='0'){
            var html='<option value="">全部</option>';
            for(var o in data){
                if(data[o].outtype!=''){
                    html+='<option value="'+data[o].outtype+'">'+data[o].outtype+'</option>\n';
                }
            }
            $('#distinctOuttype').html(html);
            $('#distinctOuttype').val(outtype);
            $('#outtype').html(html);
            $('#outtype').val(outtype);
        }
    });
}