$(function() {
    $(".ajax-sign-in").click(function() {
         var button = $(this);

        function onLogin(data) {
          var div;
          if (data.success) {
            div = $("<div class='alert alert-success'>You've logged in! (redirect in two seconds)</div>");
            setTimeout(function() {
              window.location = "/";
            }, 2000);
          } else {
            div = $("<div class='alert alert-error'>Authentication failure: " + data.failure + " </div>");
          }
          button.removeAttr("disabled");
          div.insertBefore("form");
        }

        function onFailure(xhr, text) {
          button.removeAttr("disabled");
          $("<div class='alert alert-error'>Authentication error: " + text + " </div>").insertBefore("form");
        }

        var username = $("#username").val();
        var password = $("#password").val();
        button.attr("disabled", "disabled");
        $("div.alert").remove();
        $.ajax("/login", {
          type: "POST",
          contentType: "application/json",
          dataType: "json",
          data: JSON.stringify({username: username, password: password}),
          success: onLogin,
          error: onFailure
        });
        return false;
    });

    $("button.event-log").click(function() {
      window.open("/events","event log", 'height=400,width=400', false);
      return false;
    });
});
