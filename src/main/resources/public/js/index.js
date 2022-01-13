layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交
    form.on('submit(login)', function(data){
        //layer.alert(JSON.stringify(data.field), {})

        var fieldData = data.field;
        if(fieldData.username=='undefined' || fieldData.username.trim()==''){
            layer.msg("用户名为空");
            return false;
        }
        if(fieldData.password=='undefined' || fieldData.password.trim()==''){
            layer.msg("密码不能为空");
            return false;
        }

        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:fieldData.username,
                userPwd:fieldData.password
            },
            dataType:"json",
            success:function(result){
                if(result.code == 200){
                    layer.msg("登录成功了",function () {
                        $.cookie("userIdStr",result.result.userIdStr);
                        $.cookie("userName",result.result.userName);
                        $.cookie("trueName",result.result.trueName);

                        //判断是否选择记住我
                        if($("input[type='checkbox']").is(":checked")){
                            $.cookie("userIdStr",result.result.userIdStr,{expires:7});
                            $.cookie("userName",result.result.userName,{expires:7});
                            $.cookie("trueName",result.result.trueName,{expires:7});
                        }

                        window.location.href=ctx+"/main";
                    });


                }else {
                    layer.msg(result.msg);
                }
            }


        })

        return false;
    });

});