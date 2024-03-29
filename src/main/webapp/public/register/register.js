(function () {

    document.getElementById("login_btn").onclick = function () {
        window.location.href = contextPath + "/public/login/login.html";
    };

        document.getElementById("registerDiv").style.display = "block";

        document.getElementById("registerForm").addEventListener('submit', (e) => {
            e.preventDefault();

            if(document.getElementById("password").value === document.getElementById("repeat-password").value) {

                makeCall("POST", contextPath + "/Register", document.getElementById("registerForm"), null,
                    function () {window.location.href = contextPath + "/users/confirmRegistration/confirmRegistration.html";},
                    function (req) {
                        const messageDiv = document.getElementById("messageDiv");
                        messageDiv.className = "alert alert-danger display-8";
                        messageDiv.innerHTML = "";
                        messageDiv.innerHTML = `<h6 class="mx-auto">${req.responseText}</h6>`;
                        messageDiv.style.display = "flex";
                    }
                );
            } else {
                document.getElementById("repeat-password").className = "form-control is-invalid";
                const messageDiv = document.getElementById("messageDiv");
                messageDiv.className = "alert alert-danger display-8";
                messageDiv.innerHTML = "";
                messageDiv.innerHTML = `<h6 class="mx-auto">Passwords do not match</h6>`;
                messageDiv.style.display = "flex";
            }
        });

})();