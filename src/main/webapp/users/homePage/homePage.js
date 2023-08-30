(function () {

    const userDiv = document.getElementById("userDiv");
    const superUserDiv = document.getElementById("superUserDiv");
    const adminDiv = document.getElementById("adminDiv");


    let userBean = JSON.parse(sessionStorage.getItem("user"));
    if (userBean === null) {
        makeCall("GET", contextPath + "/User/GetMyUser", null, null,
            function (req) {
                userBean = JSON.parse(req.responseText);
                sessionStorage.setItem('user', JSON.stringify(userBean));
                if (userBean.role === "Unregistered") {
                    sessionStorage.setItem("registrationInProgress", "true");
                    window.location.href = contextPath + "/public/register/register.html";
                } else if (userBean.role === "None") {
                    window.location.href = contextPath + "/users/waitForRole/waitForRole.html";
                }
                displayElements(userBean);
            }
        );
    } else {
        displayElements(userBean);
    }
})();

function displayElements(userBean) {
    if (userBean.role === "User") {
        userDiv.style.display = "block";
    } else if (userBean.role === "SuperUser") {
        userDiv.style.display = "block";
        superUserDiv.style.display = "block";
    } else if (userBean.role === "Admin") {
        userDiv.style.display = "block";
        superUserDiv.style.display = "block";
        adminDiv.style.display = "block";
    }
}