(function () {

    document.getElementById("login_btn").onclick = function () {
        window.location.href = contextPath + "/public/login/login.html";
    };

    document.getElementById("register_btn").onclick = function () {
        window.location.href = contextPath + "/public/register/register.html";
    }
})();