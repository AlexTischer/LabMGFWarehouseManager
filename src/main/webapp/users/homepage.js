(function () {
    const user = JSON.parse(sessionStorage.getItem("user"));
    const userDiv = document.getElementById("userDiv");
    const superUserDiv = document.getElementById("superUserDiv");
    const adminDiv = document.getElementById("adminDiv");

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


})();