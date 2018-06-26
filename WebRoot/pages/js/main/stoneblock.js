$(document).ready(function(){

    //选取的stoneblock
    var stoneblockList;
    var stoneblockIndex;
    var currentStoneblock;
});


////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////荒料管理////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
/**
 * 添加荒料
 */
function addStoneblock(bodyParam){

    var httpR = new createHttpR(url+'addStoneblock','post','text',bodyParam,'callBack');
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
 * 添加多条荒料
 */
function addmulStoneblock(bodyParam){

    var httpR = new createJSONHttpR(url+'addMulStoneblock','post','text',JSON.stringify(bodyParam),'callBack');
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
 * 修改荒料
 * @param id
 */
function updateStoneblock(param){
    var httpR = new createHttpR(url+'updateStoneblock','post','text',param,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        //var msg = obj['msg'];
        if(status=='0'){
            alert("修改成功！");
            //window.location.reload();
            $('#hideUpdate').click();
            queryStock($('#searchCode').val(),$('#m_dtFrom').val(),$('#m_dtTo').val(),currentPage,pageSize);
            //window.location.href="interface.html?index="+interfaceIndex;
        }
    });
}
/**
 * 修改荒料
 * @param id
 */
function updatesingleStoneblock(param){
    var httpR = new createHttpR(url+'singleupdateStoneblock','post','text',param,'callBack');
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
 * 删除荒料
 * @param id
 */
function deleteStoneblock(id){
    var bodyParam={'id':id};
    var httpR = new createHttpR(url+'deleteStoneblock','post','text',bodyParam,'callBack');
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
 * 查询荒料
 * @param stoneblockname
 * @param currentPage
 * @param pageSize
 */
function  queryStoneblock (bodyParam,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;
    /*if(stoneblockname==null||stoneblockname==''){
        var bodyParam={'page':currentPage,'size':pageSize};
    }
    else{
        var bodyParam={'page':currentPage,'size':pageSize,'name':'%'+stoneblockname+'%'};
    }*/
    //currentPage=bodyParam['page'];
    //pageSize=bodyParam['size'];
    var httpR = new createHttpR(url+'listStoneblock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            stoneblockList=msg['data'];
            var html='';
            var sum_1=0,sum_2=0,sum_3=0;
            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);
            for(var o in subData){
                sum_1=(Number(sum_1)+Number(subData[o].cube)).toFixed(3);
                sum_2=(Number(sum_2)+Number(subData[o].price)).toFixed(2);
                sum_3=(Number(sum_3)+Number(subData[o].sum)).toFixed(2);
                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+subData[o].s_dt+'</td>\n' +
                    '<td>'+subData[o].code+'</td>\n' +
                    '<td>'+subData[o].source+'</td>\n' +
                    '<td>'+subData[o].place+'</td>\n' +
                    '<td>'+subData[o].color+'</td>\n'  +
                    '<td>'+subData[o].number+'</td>\n'  +
                    '<td>'+subData[o].length+'</td>\n'  +
                    '<td>'+subData[o].width+'</td>\n'  +
                    '<td>'+subData[o].height+'</td>\n'  +
                    '<td>'+subData[o].cube+'</td>\n'  +
                    '<td>'+subData[o].price+'</td>\n'  +
                    '<td>'+subData[o].sum+'</td>\n'  +
                    '<td>'+subData[o].accountdiff+'</td>\n'  +
                    '<td>'+subData[o].platenumber+'</td>\n';
                if(subData[o].rz_dt==''){
                    html+='<td>'+'未入账'+'</td>\n';
                }
                else{
                    html+='<td>'+subData[o].rz_dt+'</td>\n';
                }
                if(subData[o].yf_dt==''){
                    html+='<td>'+'未结算'+'</td>\n';
                }
                else{
                    html+='<td>'+subData[o].yf_dt+'</td>\n';
                }
                html+= '<td>'+subData[o].comment+'</td></tr>';

            }
            html+='<tr  class="gradeX">\n' +
                '<td>合计</td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_1+'</td>\n' +
                //'<td>'+(sum_2/data.length).toFixed(2)+'</td>\n' +
                '<td></td>\n' +
                '<td>'+sum_3+'</td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '</tr>';

            //总合计
            if(data.length>pageSize) {
                sum_1=0,sum_2=0,sum_3=0;
                for (var o in data) {
                    sum_1 = (Number(sum_1) + Number(data[o].cube)).toFixed(3);
                    sum_2 = (Number(sum_2) + Number(data[o].price)).toFixed(2);
                    sum_3 = (Number(sum_3) + Number(data[o].sum)).toFixed(2);

                }

                html += '<tr  class="gradeX">\n' +
                    '<td>总合计</td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_1 + '</td>\n' +
                    //'<td>' + (sum_2 / data.length).toFixed(2) + '</td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_3 + '</td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '</tr>';
            }
            $('#stoneblockTbody').html(html);


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
 * 根据编号查询荒料
 * @param code
 * @param currentPage
 * @param pageSize
 */
function  queryStoneblockByCode (code) {

    var bodyParam={'page':1,'size':1,'code':code};

    var httpR = new createHttpR(url+'listStoneblock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            stoneblockList=msg['data'];
            for(var o in data){
                $('#sb_spec').val(data[o].length+'*'+data[o].width+'*'+data[o].height);
                $('#sb_cube').val(data[o].cube);
                $('#update_sb_spec').val(data[o].length+'*'+data[o].width+'*'+data[o].height);
                $('#update_sb_cube').val(data[o].cube);
            }
        }
    });
}
function  queryStoneblockByCode (number,index) {

    var bodyParam={'page':1,'size':1,'number':number};

    var httpR = new createHttpR(url+'listStoneblock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            for(var o in data){
                $('input[index="'+index+'"][id="sb_spec"]').val(data[o].length+'*'+data[o].width+'*'+data[o].height);
                $('input[index="'+index+'"][id="sb_cube"]').val(data[o].cube);
            }
        }
    });
}

/**
 * 修改库存查询荒料
 * @param id
 */
function  queryStoneblockById (id) {

    var bodyParam={'id':id};

    var httpR = new createHttpR(url+'selectStoneblock','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);

        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){

            var  html='<div class="form-group col-md-6">\n' +
                '    <label>单据编号</label>\n' +
                '    <input type="text" class="form-control" id="update_code" name="code" value="'+msg['code']+'" placeholder="请输入单据编号">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>来源</label>\n' +
                '    <input type="text" class="form-control" id="update_source" name="source" value="'+msg['source']+'" placeholder="请输入来源">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>单据日期</label>\n' +
                '    <input type="text" class="form-control form-control-inline input-medium default-date-picker" id="update_s_dt" name="s_dt" value="'+msg['s_dt']+'" placeholder="请输入单据日期">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>入库地点</label>\n' +
                '    <input type="text" class="form-control" id="update_place" name="place" value="'+msg['place']+'" placeholder="请输入入库地点">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>厂内料号</label>\n' +
                '    <input type="text" class="form-control" id="update_number" name="number" value="'+msg['number']+'" placeholder="请输入厂内料号">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>颜色</label>\n' +
                '    <input type="text" class="form-control" id="update_color" name="color" value="'+msg['color']+'" placeholder="请输入颜色">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>长</label>\n' +
                '    <input type="text" class="form-control" id="update_length" name="length" onkeyup="this.value=this.value.replace(/[^0-9-]+/,\'\');" value="'+msg['length']+'" placeholder="请输入长度">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>宽</label>\n' +
                '    <input type="text" class="form-control" id="update_width" name="width" onkeyup="this.value=this.value.replace(/[^0-9-]+/,\'\');" value="'+msg['width']+'" placeholder="请输入长度">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>高</label>\n' +
                '    <input type="text" class="form-control" id="update_height" name="height" onkeyup="this.value=this.value.replace(/[^0-9-]+/,\'\');" value="'+msg['height']+'" placeholder="请输入长度">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>立方数</label>\n' +
                '    <input type="text" class="form-control" id="update_cube" name="cube" value="'+msg['cube']+'" placeholder="请输入立方数">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>单价</label>\n' +
                '    <input type="text" class="form-control" id="update_price" name="price" value="'+msg['price']+'" placeholder="请输入单价">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>总块数</label>\n' +
                '    <input type="text" class="form-control" id="update_blocknumber" onkeyup="this.value=this.value.replace(/[^0-9-]+/,\'\');" name="blocknumber" value="'+msg['blocknumber']+'" placeholder="请输入总块数">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>金额</label>\n' +
                '    <input type="text" class="form-control" id="update_sum" name="sum" value="'+msg['sum']+'" placeholder="请输入金额">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>入账时间</label>\n' +
                '    <input type="text" class="form-control form-control-inline input-medium default-date-picker" id="update_rz_dt" name="rz_dt" value="'+msg['rz_dt']+'" placeholder="如未入账则为空">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>运费结算时间</label>\n' +
                '    <input type="text" class="form-control form-control-inline input-medium default-date-picker" id="update_yf_dt" name="yf_dt" value="'+msg['yf_dt']+'" placeholder="如未结算运费则为空">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>车牌号</label>\n' +
                '    <input type="text" class="form-control" id="update_platenumber" name="platenumber" value="'+msg['platenumber']+'" placeholder="请输入车牌号">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>账差</label>\n' +
                '    <input type="text" class="form-control" id="update_accountdiff" name="accountdiff" value="'+msg['accountdiff']+'" placeholder="请输入账差">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>审核人</label>\n' +
                '    <input type="text" class="form-control" id="update_auditor" name="auditor" value="'+msg['auditor']+'" placeholder="请输入审核人">\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>录入人</label>\n' +
                '    <input type="text" class="form-control" id="update_editor" name="editor" value="'+msg['editor']+'" placeholder="请输入录入人">\n' +
                '</div> \n' ;
            $('#updateForms').html(html);
            $('.default-date-picker').datepicker({
                format: 'yyyy-mm-dd'
            });


            //计算
            $('#updateForms').on('blur','#update_length',function(){
                var length=$('#update_length').val();
                var width=$('#update_width').val();
                var height=$('#update_height').val();
                if(length!=''&&width!=''&&height!=''){
                    $('#update_cube').val((length*width*height).toFixed(3));
                    $('#update_cube').trigger("blur");
                }
            });
            $('#updateForms').on('blur','#update_width',function(){
                var length=$('#update_length').val();
                var width=$('#update_width').val();
                var height=$('#update_height').val();
                if(length!=''&&width!=''&&height!=''){
                    $('#update_cube').val((length*width*height).toFixed(3));
                    $('#update_cube').trigger("blur");
                }
            });
            $('#updateForms').on('blur','#update_height',function(){
                var length=$('#update_length').val();
                var width=$('#update_width').val();
                var height=$('#update_height').val();
                if(length!=''&&width!=''&&height!=''){
                    $('#update_cube').val((length*width*height).toFixed(3));
                    $('#update_cube').trigger("blur");
                }
            });
            $('#updateForms').on('blur','#update_price',function(){
                var price=$('#update_price').val();
                var cube=$('#update_cube').val();
                if(price!=''&&cube!=''){
                    $('#update_sum').val((price*cube).toFixed(2));
                    $('#update_sum').trigger("blur");
                }
            });
            //求和
            $('#updateForms').on('blur','#update_cube',function(){
                var price=$('#update_price').val();
                var cube=$('#update_cube').val();
                if(price!=''&&cube!=''){
                    $('#update_sum').val((price*cube).toFixed(2));
                    $('#update_sum').trigger("blur");
                }

            });
            /*$('#updateForms').on('blur','#update_sum',function(){
                var sum=0;
                $('.sum').each(function(){
                    if($(this).val!=''){
                        sum+=Number($(this).val());
                    }
                });
                sum+=Number($('#accountdiff').val());
                $('#cubePrice').text(sum.toFixed(2));
            });*/

            /*$('#update_accountdiff').on('blur',function(){
                var sum=0;
                $('.sum').each(function(){
                    if($(this).val!=''){
                        sum+=Number($(this).val());
                    }
                });
                $('#cubePrice').text((Number($('#accountdiff').val())+Number(sum)).toFixed(2));
            });*/



        }
    });
}