(function () {
    var user = sessionStorage.getItem("user");
    var userDiv = document.getElementById("userDiv");
    var superUserDiv = document.getElementById("superUserDiv");
    var adminDiv = document.getElementById("adminDiv");

    if (user.role === "User") {
        userDiv.style.display = "block";
    } else if (user.role === "SuperUser") {
        userDiv.style.display = "block";
        superUserDiv.style.display = "block";
    } else if (user.role == "Admin") {
        userDiv.style.display = "block";
        superUserDiv.style.display = "block";
        adminDiv.style.display = "block";
    }

    document.getElementById("changePassword_btn").onclick = function () {
        window.location.href = contextPath + "/users/changePassword/changePassword.html";
    }


})();