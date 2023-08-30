(function() {

    document.getElementById("confirmForm").addEventListener('submit', (e) => {
        e.preventDefault()

        makeCall("POST", contextPath + "/ConfirmRegistration", document.getElementById("confirmForm"), null,
            function (req) {
                const userBean = JSON.parse(req.responseText);
                sessionStorage.setItem('user', JSON.stringify(userBean));
                window.location.href = contextPath + "/users/waitForRole/waitForRole.html";
            },
            function (req) {
                const messageDiv = document.getElementById("messageDiv");
                messageDiv.className = "alert alert-danger display-8";
                    messageDiv.innerHTML = "";
                    messageDiv.innerHTML = `<h6 class="mx-auto">${req.responseText}</h6>`;
                    messageDiv.style.display = "flex";
            }
        );
    });
})();