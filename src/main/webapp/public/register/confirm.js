function confirm() {

    sessionStorage.setItem("registrationInProgress", "true");

    document.getElementById("registerDiv").style.display = "none";
    document.getElementById("confirmDiv").style.display = "flex";

    document.getElementById("confirmForm").addEventListener('submit', (e) => {
        e.preventDefault()

        makeCall("POST", contextPath + "/ConfirmRegistration", document.getElementById("confirmForm"), null,
            function (req) {
                const userBean = JSON.parse(req.responseText);
                sessionStorage.setItem('user', JSON.stringify(userBean));
                sessionStorage.removeItem("registrationInProgress");
                window.location.href = contextPath + "/users/homepage.html";
            },
            function (req) {
                    var messageDiv = document.getElementById("messageDiv");
                    messageDiv.className = "alert alert-danger";
                    messageDiv.innerHTML = "";
                    messageDiv.innerHTML = `<h6 class="mx-auto">${req.responseText}</h6>`;
                    messageDiv.style.display = "flex";
            }
        );
    });
}