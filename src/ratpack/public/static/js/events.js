(function() {
  var handler = function(action) {
      return function(message) {
        var pre = $("pre");
        var text = pre.text();
        if (text) {
          text += "\n";
        }
        pre.text(text + message.username + ": " + action);
      }
  };
  var eb = new vertx.EventBus('http://localhost:5050/eventbus');
  eb.onopen = function() {
    eb.registerHandler('auth.login.success', handler("logged in"));
    eb.registerHandler('auth.login.failure', handler("tried to log in and failed"));
    eb.registerHandler('auth.logout', handler("logged out"));
  }
})();



