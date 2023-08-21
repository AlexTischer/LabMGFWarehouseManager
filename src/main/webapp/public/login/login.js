(function () {

    document.getElementById("register_btn").onclick = function () {
        window.location.href = contextPath + "/public/register/register.html";
    }

    document.getElementById("loginForm").addEventListener('submit', (e) => {
        e.preventDefault();

        makeCall("POST", contextPath + "/Login", document.getElementById("loginForm"), null,
            function (req) {
                        const userBean = JSON.parse(req.responseText);
                        sessionStorage.setItem('user', JSON.stringify(userBean));
                        if(userBean.role === "Unregistered") {
                            sessionStorage.setItem("registrationInProgress", "true");
                            window.location.href = contextPath + "/public/register/register.html";
                        } else if (userBean.role === "None") {
                            window.location.href = contextPath + "/users/waitForRole.html";
                        } else {
                            window.location.href = contextPath + "/users/homepage.html";
                        }
                    },
            function (req) {
                const messageDiv = document.getElementById("messageDiv");
                messageDiv.className = "alert alert-danger";
                        messageDiv.innerHTML = "";
                        messageDiv.innerHTML = `<h6 class="mx-auto">${req.responseText}</h6>`;
                        messageDiv.style.display = "flex";
                    }
            );
    });

})();