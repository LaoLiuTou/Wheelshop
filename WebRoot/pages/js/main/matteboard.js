$(document).ready(function(){

    //选取的matteboard
    var matteboardList;
    var matteboardIndex;
    var currentMatteboard;
});


////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////哑光板管理////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
/**
 * 添加哑光板
 */
function addMatteboard(bodyParam){

    var httpR = new createHttpR(url+'addMatteboard','post','text',bodyParam,'callBack');
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
 * 添加多条亚光板
 */
function addmulMatteboard(bodyParam){

    var httpR = new createJSONHttpR(url+'addMulMatteboard','post','text',JSON.stringify(bodyParam),'callBack');
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
 * 修改哑光板
 * @param id
 */
function updateMatteboard(param){

    var httpR = new createHttpR(url+'updateMatteboard','post','text',param,'callBack');
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
 * 删除哑光板
 * @param id
 */
function deleteMatteboard(id){
    var bodyParam={'id':id};
    var httpR = new createHttpR(url+'deleteMatteboard','post','text',bodyParam,'callBack');
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
 * 查询哑光板
 * @param Matteboardname
 * @param currentPage
 * @param pageSize
 */
function  queryMatteboard (bodyParam,currentPage,pageSize) {

    //分页显示的页码数  必须为奇数
    var showPage=7;
    //var bodyParam={'page':currentPage,'size':pageSize};
    //currentPage=bodyParam['page'];
    //pageSize=bodyParam['size'];
    var httpR = new createHttpR(url+'listMatteboard','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);
        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
            var data=msg['data'];
            matteboardList=msg['data'];
            var html='';
            var sum_1=0,sum_2=0,sum_3=0,sum_4=0,sum_5=0,sum_6=0,sum_7=0,
                sum_8=0,sum_9=0,sum_10=0,sum_11=0,sum_12=0,sum_13=0;
            var subData=data.slice((currentPage-1)*pageSize,currentPage*pageSize);
            for(var o in subData){
                var sb_specs=subData[o].sb_spec.split('*');
                var sb_cube=subData[o].sb_cube;
                if(sb_specs[2]>1.25&&(subData[o].layer=='上'||subData[o].layer=='下')){
                    sb_cube=(sb_cube/2).toFixed(3);
                }

                sum_1=(Number(sum_1)+Number(sb_specs[0])).toFixed(2);
                sum_2=(Number(sum_2)+Number(sb_specs[1])).toFixed(2);
                sum_3=(Number(sum_3)+Number(sb_specs[2])).toFixed(2);
                sum_4=(Number(sum_4)+Number(subData[o].sb_cube)).toFixed(3);
                sum_5=(Number(sum_5)+Number(sb_cube)).toFixed(3);
                sum_6=Number(sum_6)+Number(subData[o].blocknumber);
                sum_7=(Number(sum_7)+Number(subData[o].square)).toFixed(2);
                sum_8=Number(sum_8)+Number(subData[o].blocknumber-subData[o].belowgradeblock);
                sum_9=(Number(sum_9)+Number(subData[o].square-subData[o].belowgradesquare)).toFixed(2);
                sum_10=Number(sum_10)+Number(subData[o].belowgradeblock);
                sum_11=(Number(sum_11)+Number(subData[o].belowgradesquare)).toFixed(2);
                sum_12=(Number(sum_12)+Number(subData[o].price)).toFixed(2);
                sum_13=(Number(sum_13)+Number(subData[o].sum)).toFixed(2);

                html+='<tr index='+o+' class="gradeX">\n' +
                    '<td>'+(Number(o)+1)+'</td>\n' +
                    '<td>'+subData[o].sb_code+'</td>\n' +
                    '<td>'+sb_specs[0]+'</td>\n' +
                    '<td>'+sb_specs[1]+'</td>\n' +
                    '<td>'+sb_specs[2]+'</td>\n' +
                    '<td>'+subData[o].sb_cube+'</td>\n' +
                    '<td>'+subData[o].layer+'</td>\n' +
                    '<td>'+sb_cube+'</td>\n' +
                    '<td>'+subData[o].m_dt+'</td>\n' +
                    '<td>'+subData[o].code+'</td>\n' +
                    '<td>'+subData[o].msize+'</td>\n' +
                    '<td>'+subData[o].height+'</td>\n' +
                    '<td>'+subData[o].blocknumber+'</td>\n' +
                    '<td>'+Number(subData[o].square).toFixed(2)+'</td>\n' +
                    '<td>'+Number(subData[o].blocknumber-subData[o].belowgradeblock)+'</td>\n' +
                    '<td>'+Number(subData[o].square-subData[o].belowgradesquare).toFixed(2)+'</td>\n' +
                    '<td>'+subData[o].belowgradeblock+'</td>\n' +
                    '<td>'+Number(subData[o].belowgradesquare).toFixed(2)+'</td>\n' +
                    '<td>'+subData[o].price+'</td>\n' +
                    '<td>'+subData[o].sum+'</td>\n' +
                    '<td>'+subData[o].comment+'</td>\n' +
                 '</tr>';
                //alert(html);
            }
            html+='<tr  class="gradeX">\n' +
                '<td>合计</td>\n' +
                '<td></td>\n' +
                '<td>'+sum_1+'</td>\n' +
                '<td>'+sum_2+'</td>\n' +
                '<td>'+sum_3+'</td>\n' +
                '<td>'+sum_4+'</td>\n' +
                '<td></td>\n' +
                '<td>'+sum_5+'</td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td></td>\n' +
                '<td>'+sum_6+'</td>\n' +
                '<td>'+sum_7+'</td>\n' +
                '<td>'+sum_8+'</td>\n' +
                '<td>'+sum_9+'</td>\n' +
                '<td>'+sum_10+'</td>\n' +
                '<td>'+sum_11+'</td>\n' +
                '<td>'+sum_12+'</td>\n' +
                '<td>'+sum_13+'</td>\n' +
                '<td></td>\n' +
                '</tr>';
            //总合计
            if(data.length>pageSize) {
                sum_1 = 0, sum_2 = 0, sum_3 = 0, sum_4 = 0, sum_5 = 0, sum_6 = 0, sum_7 = 0,
                    sum_8 = 0, sum_9 = 0, sum_10 = 0, sum_11 = 0, sum_12 = 0, sum_13 = 0;
                for (var o in data) {
                    var sb_specs = data[o].sb_spec.split('*');
                    var sb_cube = data[o].sb_cube;
                    if (sb_specs[2] > 1.25 && (data[o].layer == '上' || data[o].layer == '下')) {
                        sb_cube = (sb_cube / 2).toFixed(3);
                    }

                    sum_1 = (Number(sum_1) + Number(sb_specs[0])).toFixed(2);
                    sum_2 = (Number(sum_2) + Number(sb_specs[1])).toFixed(2);
                    sum_3 = (Number(sum_3) + Number(sb_specs[2])).toFixed(2);
                    sum_4 = (Number(sum_4) + Number(data[o].sb_cube)).toFixed(3);
                    sum_5 = (Number(sum_5) + Number(sb_cube)).toFixed(3);
                    sum_6 = Number(sum_6) + Number(data[o].blocknumber);
                    sum_7 = (Number(sum_7) + Number(data[o].square)).toFixed(2);
                    sum_8 = Number(sum_8) + Number(data[o].blocknumber - data[o].belowgradeblock);
                    sum_9 = (Number(sum_9) + Number(data[o].square - data[o].belowgradesquare)).toFixed(2);
                    sum_10 = Number(sum_10) + Number(data[o].belowgradeblock);
                    sum_11 = (Number(sum_11) + Number(data[o].belowgradesquare)).toFixed(2);
                    sum_12 = (Number(sum_12) + Number(data[o].price)).toFixed(2);
                    sum_13 = (Number(sum_13) + Number(data[o].sum)).toFixed(2);

                }
                html += '<tr  class="gradeX">\n' +
                    '<td>总合计</td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_1 + '</td>\n' +
                    '<td>' + sum_2 + '</td>\n' +
                    '<td>' + sum_3 + '</td>\n' +
                    '<td>' + sum_4 + '</td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_5 + '</td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td></td>\n' +
                    '<td>' + sum_6 + '</td>\n' +
                    '<td>' + sum_7 + '</td>\n' +
                    '<td>' + sum_8 + '</td>\n' +
                    '<td>' + sum_9 + '</td>\n' +
                    '<td>' + sum_10 + '</td>\n' +
                    '<td>' + sum_11 + '</td>\n' +
                    '<td>' + sum_12 + '</td>\n' +
                    '<td>' + sum_13 + '</td>\n' +
                    '<td></td>\n' +
                    '</tr>';
            }
            $('#reportTbody').html(html);
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
 * 修改库存查询压光板
 * @param id
 */
function  queryMatteboardById (id) {

    var bodyParam={'id':id};

    var httpR = new createHttpR(url+'selectMatteboard','post','text',bodyParam,'callBack');
    httpR.HttpRequest(function(response){
        var obj = JSON.parse(response);

        var status = obj['status'];
        var msg = obj['msg'];
        if(status=='0'){
             var html='<div class="form-group col-md-6">\n' +
                 '    <label>荒料编号</label>\n' +
                 '    <input type="text" class="form-control" id="update_sb_code" name="sb_code" value="'+msg['sb_code']+'" placeholder="请输入荒料编号">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>荒料规格</label>\n' +
                 '    <input type="text" class="form-control" id="update_sb_spec" name="sb_spec" value="'+msg['sb_spec']+'" placeholder="格式为 长*宽*高">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>立方数</label>\n' +
                 '    <input type="text" class="form-control" id="update_sb_cube" name="sb_cube" value="'+msg['sb_cube']+'" placeholder="请输入立方数">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>单据编号</label>\n' +
                 '    <input type="text" class="form-control" id="update_code" name="code" value="'+msg['code']+'" placeholder="请输入单据编号">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>验收人</label>\n' +
                 '    <input type="text" class="form-control" id="update_auditor" name="auditor" value="'+msg['auditor']+'" placeholder="请输入验收人">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>单据日期</label>\n' +
                 '    <input type="text" class="form-control form-control-inline input-medium default-date-picker" id="update_m_dt" name="m_dt" value="'+msg['m_dt']+'" placeholder="请输入单据日期">\n' +
                 '</div>\n' +
                 /*'<div class="form-group col-md-6">\n' +
                 '    <label>工组</label>\n' +
                 '    <input type="text" class="form-control" id="update_workgroup" name="workgroup" value="'+msg['workgroup']+'" placeholder="请输入工组">\n' +
                 '</div>\n' +*/

                 '<div class="form-group col-md-6">\n' +
                 '    <label>板材尺寸</label>\n' +
                 '    <input type="text" class="form-control" id="update_msize" name="msize" value="'+msg['msize']+'" placeholder="格式为 长*宽">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>料层</label>\n' +
                 '    <select class="form-control m-bot15" id="update_layer" name="layer" >\n' +
                 '        <option value="上">上</option>\n' +
                 '        <option value="中">中</option>\n' +
                 '        <option value="下">下</option>\n' +
                 '    </select>\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>厚度</label>\n' +
                 '    <input type="text" class="form-control" id="update_height" name="height" onkeyup="this.value=this.value.replace(/[^0-9-]+/,\'\');" value="'+msg['height']+'" placeholder="请输入板材厚度">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>总块数</label>\n' +
                 '    <input type="text" class="form-control" id="update_blocknumber" onkeyup="this.value=this.value.replace(/[^0-9-]+/,\'\');" name="blocknumber" value="'+msg['blocknumber']+'" placeholder="请输入总块数">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>平方数</label>\n' +
                 '    <input type="text" class="form-control" id="update_square" name="square" value="'+msg['square']+'" placeholder="请输入平方数">\n' +
                 '</div>\n' +

                 '<div class="form-group col-md-6">\n' +
                 '    <label>不合格块数</label>\n' +
                 '    <input type="text" class="form-control" id="update_belowgradeblock" onkeyup="this.value=this.value.replace(/[^0-9-]+/,\'\');" value="'+msg['belowgradeblock']+'" name="belowgradeblock" placeholder="请输入不合格块数">\n' +
                 '</div>\n' +
                 '<div class="form-group col-md-6">\n' +
                 '    <label>不合格平方数</label>\n' +
                 '    <input type="text" class="form-control" id="update_belowgradesquare" value="'+msg['belowgradesquare']+'" name="belowgradesquare" placeholder="请输入不合格平方数">\n' +
                 '</div>\n'+
                '<div class="form-group col-md-6">\n' +
                '    <label>单价</label>\n' +
                '    <input type="text" class="form-control" id="update_price" name="price" value="'+msg['price']+'"  >\n' +
                '</div>\n' +
                '<div class="form-group col-md-6">\n' +
                '    <label>金额</label>\n' +
                '    <input type="text" class="form-control" id="update_sum" name="sum" value="'+msg['sum']+'"  >\n' +
                '</div>\n' ;

            $('#updateForms').html(html);
            $('#update_layer').val(msg['layer']);
            $('.default-date-picker').datepicker({
                format: 'yyyy-mm-dd'
            });


            //计算

            $('#updateForms').on('blur','#update_msize',function(){
                cal();
            });
            $('#updateForms').on('blur','#update_blocknumber',function(){
                cal();
            });
            $('#updateForms').on('blur','#update_belowgradeblock',function(){
                cal();
            });
            $('#updateForms').on('blur','#update_price',function(){
                cal();
            });



        }
        //合格数
        var gradeblock=0;
        function cal(){
            var msize=$('#update_msize').val();
            var blocknumber=$('#update_blocknumber').val();
            var belowgradeblock=$('#update_belowgradeblock').val();
            var msizes=msize.split('*');
            if(msize!=''&&blocknumber!=''){
                $('#update_square').val((msizes[0]*msizes[1]*blocknumber/1000000).toFixed(2));
                $('#update_square').trigger("blur");
            }
            if(msize!=''&&belowgradeblock!=''){
                $('#update_belowgradesquare').val((msizes[0]*msizes[1]*belowgradeblock/1000000).toFixed(2));
                $('#update_belowgradesquare').trigger("blur");
            }
            var price=$('#update_price').val();
            if(blocknumber!=''&&price!=''&&msize!=''){
                if(belowgradeblock.length==0){
                    belowgradeblock=0;
                    $('#update_belowgradeblock').val('0')
                    $('#update_belowgradesquare').val('0');
                }
                gradeblock=Number(blocknumber)-Number(belowgradeblock);

                //$('input[index="'+index+'"][id="sum"]').val((gradeblock*price*(msizes[0]*msizes[1]*blocknumber/1000000)).toFixed(2));
                $('#update_sum').val((gradeblock*price*(msizes[0]*msizes[1]/1000000)).toFixed(2));
            }
        }
    });
}