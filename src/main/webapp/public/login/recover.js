(function () {

    document.getElementById("recover_btn").onclick = function () {
        if(document.getElementById("email").validity.valid){
            makeCall("POST", contextPath + "/RecoverPassword", document.getElementById("loginForm"), null, function (req) {
                const messageDiv = document.getElementById("messageDiv");
                messageDiv.className = "alert alert-success";
                messageDiv.innerHTML = "";
                messageDiv.innerHTML = `<h6 class="mx-auto">${req.responseText}</h6>`;
                messageDiv.style.display = "flex";

            }, function (req) {
                const messageDiv = document.getElementById("messageDiv");
                messageDiv.className = "alert alert-danger";
                messageDiv.innerHTML = "";
                messageDiv.innerHTML = `<h6 class="mx-auto">${req.responseText}</h6>`;
                messageDiv.style.display = "flex";
            });
        } else {
            document.getElementById("email").className = "form-control is-invalid";
        }
    }
}());