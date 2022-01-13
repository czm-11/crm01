layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交
    form.on('submit(saveBtn)', function(data){

        var fieldData=data.field;

        $.ajax({
            type:"post",
            url:ctx+"/user/update",
            data:{
                "oldPassword":fieldData.old_password,
                "newPassword":fieldData.new_password,
                "confirmPwd":fieldData.again_password
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    layer.msg("修改密码成功，系统3秒后退出",function () {
                        //清空cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        window.parent.location.href=ctx+"/index";
                    });
                }else {
                    //登陆页面的提示
                    layer.msg(data.msg);
                }
            }

        })

        return false;
    });

});