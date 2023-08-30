(function (){
    const urlParams = new URLSearchParams(window.location.search);
    const entries = urlParams.entries();
// build params object
    const params = {};
    for (entry of entries) {
        params[entry[0]] = entry[1];
    }

    if(params.changePasswordToken != null){
        document.getElementById("changePasswordToken").value = params.changePasswordToken;
        document.getElementById("oldPasswordDiv").style.display = "none";
        document.getElementById("old-password").required = false;
    } else {
        document.getElementById("changePasswordToken").value = "";
        document.getElementById("oldPasswordDiv").style.display = "block";
    }

    document.getElementById("changePwdForm").addEventListener('submit', (e) => {
        e.preventDefault();

        if(document.getElementById("password").value === document.getElementById("repeat-password").value) {

            makeCall("POST", contextPath + "/User/ChangePassword", document.getElementById("changePwdForm"), null,
                function (req) {
                    openAlertPrompt("Success", "Password changed successfully. You can now go to <a href='../homePage/homePage.html'>HomePage</a>.");
                },
                function (req) {
                    const messageDiv = document.getElementById("messageDiv");
                    messageDiv.className = "alert alert-danger";
                    messageDiv.innerHTML = "";
                    messageDiv.innerHTML = `<h6 class="mx-auto">${req.responseText}</h6>`;
                    messageDiv.style.display = "flex";
                }
            );
        } else {
            document.getElementById("repeat-password").className = "form-control is-invalid";
            const messageDiv = document.getElementById("messageDiv");
            messageDiv.className = "alert alert-danger";
            messageDiv.innerHTML = "";
            messageDiv.innerHTML = `<h6 class="mx-auto">Passwords do not match</h6>`;
            messageDiv.style.display = "flex";
        }
    });

})();