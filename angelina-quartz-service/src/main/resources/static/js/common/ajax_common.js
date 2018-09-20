var commonJS = {    /*共用js*/    //调用公用异步请求方法   ajaxObj对象主要包含请求参数(封装成对象)、请求地址url    "sendAjaxRequest": function (ajaxObj, callbackFn) {        var url = ajaxObj.url;        var param = ajaxObj.param;        var async = ajaxObj.async;//true、false 判断异步请求的类型，同步还是异步        if (!ajaxObj.hasOwnProperty("async")) async = true;        var traditional = ajaxObj.traditional;//true、false 判断异步请求的类型，同步还是异步        if (!ajaxObj.hasOwnProperty("traditional")) traditional = false;        var method = ajaxObj.method;        if (!method) method = "GET";        if (!param) param = {};        /*参数不存在 就给一个默认的参数*/        $.ajax({            type: method,            url: url,            data: param,            async: async,            dataType: 'json',            traditional: traditional,            beforeSend: function (xhr) {                var token = $("meta[name='_csrf']").attr("content");                var header = $("meta[name='_csrf_header']").attr("content");                if(header) {                  xhr.setRequestHeader(header, token);                }            },            success: function (json) {                if (callbackFn && typeof callbackFn === 'function') {                    callbackFn(json);                }            },            error: function (jqXHR, textStatus, errorThrown) {                if (jqXHR.status === 403||jqXHR.status === 0) {/*登录已过期*/                    alert("登录已超时，请重新登录。");                    window.location.href = "/logout"                }            }        });    },    "tips": function (msg) {        if (msg) layer.alert(msg, {icon: 5});    }};