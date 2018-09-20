$(document).ready(function () {

  $(document).on("click",".delete",function () {

    var button = $(this);
    var param = {};
    param["jobName"] = button.attr("data-jobName");
    param["jobGroup"] = button.attr("data-jobGroup");
    var ajaxObj = {url: '/job/delete', param: param, method: "POST", async: false};
    commonJS.sendAjaxRequest(ajaxObj, function (data) {
      if (data.code === "1") {
        alert("删除成功");
        location.reload();
      } else {
        alert(data.message);
      }
    });
  });

  $(document).on("click",".pause",function () {

    var button = $(this);
    var param = {};
    param["jobName"] = button.attr("data-jobName");
    param["jobGroup"] = button.attr("data-jobGroup");
    var ajaxObj = {url: '/job/pause', param: param, method: "POST", async: true};
    commonJS.sendAjaxRequest(ajaxObj, function (data) {
      if (data.code === "1") {
        alert("暂停成功");
        location.reload();
      } else {
        alert(data.message);
      }
    });
  });

  $(document).on("click",".restart",function () {

    var button = $(this);
    var param = {};
    param["jobName"] = button.attr("data-jobName");
    param["jobGroup"] = button.attr("data-jobGroup");
    var ajaxObj = {url: '/job/resume', param: param, method: "POST", async: false};
    commonJS.sendAjaxRequest(ajaxObj, function (data) {
      if (data.code === "1") {
        alert("重启成功");
        location.reload();
      } else {
        alert(data.message);
      }
    });
  });

  $("#jobType").on("change",function () {
    var jobType = $(this).val();
    if(jobType=="1"){
      $("#cronExpressionDisplay").hide();
    }else{
      $("#cronExpressionDisplay").show();
    }
  });


  $('#startTime').datetimepicker({
    language:  'zh-CN',
    autoclose : true,
    todayBtn : true,
    format: 'yyyy-mm-dd hh:ii',
    minuteStep:1
  });

  var validateJob = function () {
    if($("#jobName").val()==""){
      $("#error_msg").html("任务名称不能为空");
      return false;
    }

    if($("#jobGroup").val()==""){
      $("#error_msg").html("任务组不能为空");
      return false;
    }


    if($("#jobDescription").val()==""){
      $("#error_msg").html("任务描述不能为空");
      return false;
    }

    if($("#startTime").val()==""){
      $("#error_msg").html("任务开始时间不能为空");
      return false;
    }

    if("2"==$("#jobType").val()&&""==$("#cronExpression").val()) {
      $("#error_msg").html("任务任务执行表达式不能为空");
      return false;
    }

    if($("#serviceClassName").val()==""){
      $("#error_msg").html("执行接口全路径不能为空");
      return false;
    }

    if($("#methodName").val()==""){
      $("#error_msg").html("执行方法名称不能为空");
      return false;
    }

    $("#error_msg").html("");
    return true;
  };


  $("#saveJob").on("click", function () {

    if(!validateJob()){
      return;
    }

    var param = $('#jobForm').serialize();
    var ajaxObj = {url: '/job/save', param: param, method: "POST", async: false};
    commonJS.sendAjaxRequest(ajaxObj, function (data) {
      if (data.code === "1") {
        alert("保存成功");
        location.reload();
      } else {
        $("#error_msg").html(data.message);
      }
    });

  });

});