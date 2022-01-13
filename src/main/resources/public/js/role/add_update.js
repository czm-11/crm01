layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    //取消
    $("#closeBtn").click(function () {
        var index=parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    })

    form.on("submit(addOrUpdateRole)",function (obj) {
        var index=top.layer.msg("数据正在加载中...",{icon:16,time:false,shade:0.8})
        //发送ajax添加
        var url=ctx+"/role/save";
        if($("input[name=id]").val()){
            url=ctx+"/role/update";
        }

        $.post(url,obj.field,function (result) {
            if(result.code==200){
                //关闭
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("添加OK",{icon:6});
                    layer.closeAll("iframe");
                    parent.location.reload();
                },500);

            }else {
                layer.msg(result.msg,{icon:5});
            }
        },"json")
    })
});