package content

import geb.Page

class LoginDialog extends Page {

    static at = {
        waitFor { $(".modal-header h1").text() } == "Please log in"
    }

    static content = {
        modal(cache: true) { $("div.modal") }
        username { modal.find("#inputUsername") }
        password { modal.find("#inputPassword") }
        signIn { modal.find("button[type=submit]") }
        errorAlert(wait: true) { modal.find(".alert-error") }
        errorAlertText { errorAlert.text() }
    }
}
