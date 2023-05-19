document.getElementById("logout_btn").onclick = function () {
    openConfirmPrompt("Are you sure you want to logout?", function () {
        makeCall("GET", contextPath + "/Logout", null, null, function (req) {
            sessionStorage.clear();
            window.location.href = contextPath + "/public/login/login.html";
        });
    });
}