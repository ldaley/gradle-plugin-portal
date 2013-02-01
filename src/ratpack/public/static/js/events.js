(function() {
  var handler = function(action) {
      return function(message) {
        $("#events").append("<li>“" + message.username + "” " + action + "</li>");
      }
  };
  var eb = new vertx.EventBus('http://localhost:5050/eventbus');
  eb.onopen = function() {
    eb.registerHandler('auth.login.success', handler("logged in"));
    eb.registerHandler('auth.login.failure', handler("tried to log in and failed"));
    eb.registerHandler('auth.logout', handler("logged out"));
  }
})();



