layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
//角色列表展示
    var tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150,
                templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });
// 多条件搜索
    $(".search_btn").on("click",function(){
        table.reload("roleListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                roleName: $("input[name='roleName']").val()
            }
        })
    });

    //头部工具栏
    table.on('toolbar(roles)', function(obj){
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event){
            case 'add':
                openAddOrUpdatePage();
                break;
            case 'grant':
                toRoleGrant(checkStatus.data);
                break;
        };
    });
    
    function toRoleGrant(datas) {
        if(datas.length==0){
            layer.msg("请选择授权的角色",{icon:5});
            return;
        }

        if(datas.length>1){
            layer.msg("不支持批量授权",{icon:5});
            return;
        }

        var title="<h2>角色模块---授权</h2>";
        var url=ctx+"/role/toRoleGrantPage?roleId="+datas[0].id;
        layer.open({
            title:title,
            type:2,
            maxmin:true,
            area:["600px","280px"],
            content:url
        })
    }

    function openAddOrUpdatePage(roleId){
        var title="<h2>角色模块--添加</h2>";
        var url=ctx+"/role/toAddOrUpdate";

        //判断是添加还是修改
        if(roleId){
            title="<h2>角色模块--修改</h2>";
            url=url+"?roleId="+roleId;
        }

        layer.open({
            title:title,
            content:url,
            type:2,
            area:["650px","280px"],
            maxmin:true
        })
    }

    //行内工具栏
    table.on('tool(roles)', function(obj){ //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
        var data = obj.data; //获得当前行数据

        if(obj.event==='del'){
            layer.confirm("真的删除吗？",function (index) {
                $.post(ctx+"/role/delete",{"id":data.id},function (result) {
                    if(result.code==200){
                        layer.msg("栓除成功了",{icon:6});
                        layer.close(index);
                        tableIns.reload();
                    }else {
                        layer.msg(result.msg,{icon:5});
                    }
                },"json")
            });
        }else if (obj.event==='edit'){
            openAddOrUpdatePage(data.id);
        }
    });
});
