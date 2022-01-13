layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    form.on('submit(addOrUpdateSaleChance)', function(obj){

        var index=layer.msg("数据正在提交中，请稍等",{icon:16,time:false,shade:0.8})

        console.log(obj.field+"<<");

        var url=ctx+"/sale_chance/save";

        if($("input[name=id]").val()){
            url=ctx+"/sale_chance/update";
        }

        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            dataType:"json",
            success:function (obj) {
                if(obj.code==200){
                    layer.msg("添加ok");
                    window.parent.location.reload()
                }else {
                    layer.msg(obj.msg,{icon:6});
                }
            }
        })

        return false;
    });

    //取消功能
    $("#closeBtn").click(function(){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);
    });

    var assignMan=$("input[name='man']").val();
    //添加下拉框
    $.ajax({
        type:"post",
        url:ctx+"/user/sales",
        dataType: "json",
        success:function (data) {
            for (var x in data) {
                if(data[x].id==assignMan){
                    $("#assignMan").append("<option selected value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }else {
                    $("#assignMan").append("<option value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }

            }

            layui.form.render("select");
        }
    })

});