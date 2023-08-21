(function () {

    const dropdownMenuRow = document.createElement("div");
    dropdownMenuRow.classList.add("row", "justify-content-end");
    const dropdownMenu = document.createElement("div");
    dropdownMenu.classList.add("col-2", "dropdown");
    const dropdownMenuButton = document.createElement("button");
    dropdownMenuButton.classList.add("btn", "btn-info", "dropdown-toggle");
    dropdownMenuButton.setAttribute("type", "button");
    dropdownMenuButton.setAttribute("data-bs-toggle", "dropdown");
    dropdownMenuButton.setAttribute("data-bs-auto-close", "outside");
    dropdownMenuButton.setAttribute("aria-expanded", "false");

    const dropdownMenuNotificationBadge = document.createElement("span");
    dropdownMenuNotificationBadge.classList.add("badge", "bg-danger", "d-none", "me-2");
    dropdownMenuNotificationBadge.setAttribute("id", "notification_badge");

    const dropdownMenuIcon = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    dropdownMenuIcon.setAttribute("width", "16");
    dropdownMenuIcon.setAttribute("height", "16");
    dropdownMenuIcon.setAttribute("fill", "currentColor");
    dropdownMenuIcon.classList.add("bi", "bi-person-fill");
    dropdownMenuIcon.setAttribute("viewBox", "0 0 16 16");

    const dropdownMenuIconPath = document.createElementNS("http://www.w3.org/2000/svg", "path");
    dropdownMenuIconPath.setAttribute("d", "M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z");

    dropdownMenuIcon.appendChild(dropdownMenuIconPath);

    dropdownMenuButton.appendChild(dropdownMenuNotificationBadge);
    dropdownMenuButton.appendChild(dropdownMenuIcon);

    const dropdownMenuList = document.createElement("ul");
    dropdownMenuList.classList.add("dropdown-menu");
    dropdownMenuList.setAttribute("aria-labelledby", "dropdownMenuButton");


    const ChangePasswordElement = document.createElement("li");
    const ChangePasswordButton = document.createElement("button");
    ChangePasswordButton.classList.add("btn","dropdown-item");
    ChangePasswordButton.setAttribute("type", "button");
    ChangePasswordButton.setAttribute("id", "changePassword_btn");
    ChangePasswordButton.textContent = "Change Password";
    ChangePasswordElement.appendChild(ChangePasswordButton);

    dropdownMenuList.appendChild(ChangePasswordElement);


    const LogoutElement = document.createElement("li");
    const LogoutButton = document.createElement("button");
    LogoutButton.classList.add("btn","dropdown-item");
    LogoutButton.setAttribute("type", "button");
    LogoutButton.setAttribute("id", "logout_btn");
    LogoutButton.textContent = "Logout";
    LogoutElement.appendChild(LogoutButton);

    dropdownMenuList.appendChild(LogoutElement);

    let notifications = [];


    makeCall("GET", contextPath + "/User/GetNotifications", null, null,
        function (req) {
            notifications = JSON.parse(req.responseText);
            if(notifications && notifications.length > 0) {

                const dividerElement = document.createElement("li");
                dividerElement.classList.add("dropdown-divider");
                dropdownMenuList.appendChild(dividerElement);

                const dropStartElement = document.createElement("li");
                const dropStartMenu = document.createElement("div");
                dropStartMenu.classList.add("dropstart", "dropdown-item");
                const dropStartButton = document.createElement("button");
                dropStartButton.classList.add("btn","dropdown-toggle");
                dropStartButton.setAttribute("type", "button");
                dropStartButton.setAttribute("id", "dropstartMenuButton");
                dropStartButton.setAttribute("aria-expanded", "false");
                dropStartButton.setAttribute("data-bs-toggle", "dropdown");
                dropStartButton.textContent = "Notifications";
                const dropStartMenuList = document.createElement("ul");
                dropStartMenuList.classList.add("dropdown-menu");
                dropStartMenuList.setAttribute("aria-labelledby", "dropstartMenuButton");

                notifications.forEach(function (notification) {
                    const notificationElement = document.createElement("li");
                    const notificationLink = document.createElement("button");
                    notificationLink.classList.add("dropdown-item");
                    notificationLink.setAttribute("id", notification.id);
                    notificationLink.setAttribute("type", "button");
                    notificationLink.textContent = notification.type;
                    notificationElement.appendChild(notificationLink);
                    dropStartMenuList.appendChild(notificationElement);

                    notificationLink.onclick = function () {
                        makeCall("POST", contextPath + "/User/MarkNotificationAsRead" + "?id=" + notification.id, null, null,
                            function (req) {
                                        openModal(function () {
                                            const modalTitle = document.getElementById("modal_title");
                                            modalTitle.innerHTML = notification.type;

                                            const modal = document.getElementById("modal_content_body");

                                            modal.innerHTML = `<h6 class="text-start">${notification.message}</h6>`;

                                            const modalFooter = document.getElementById("modal_footer");

                                            const proceedButton = document.createElement("button");
                                            proceedButton.setAttribute("type", "button");
                                            proceedButton.setAttribute("class", "btn btn-primary");
                                            proceedButton.innerHTML = "Proceed";
                                            proceedButton.addEventListener("click", function () {
                                                window.location.href = contextPath + notification.url;
                                            });
                                            modalFooter.appendChild(proceedButton);
                                        });
                                    },
                            function (req) {
                                            console.log("Error marking notification as read");
                                    }
                        );
                    }
                });

                dropStartMenu.appendChild(dropStartButton);
                dropStartMenu.appendChild(dropStartMenuList);
                dropStartElement.appendChild(dropStartMenu);
                dropdownMenuList.appendChild(dropStartElement);


                const notificationCount = notifications.length;
                updateNotificationBadge(notificationCount);
            }
        },
        function (req) {
            console.log("Error retrieving notifications");
        }
    );





    dropdownMenu.appendChild(dropdownMenuButton);
    dropdownMenu.appendChild(dropdownMenuList);

    dropdownMenuRow.appendChild(dropdownMenu);

    document.getElementById("container_div").insertBefore(dropdownMenuRow, document.getElementById("container_div").firstChild);

    document.getElementById("changePassword_btn").onclick = function () {
        window.location.href = contextPath + "/users/changePassword/changePassword.html";
    }

    document.getElementById("logout_btn").onclick = function () {
        openConfirmPrompt("Are you sure you want to logout?", function () {
            makeCall("GET", contextPath + "/Logout", null, null, function (req) {
                sessionStorage.clear();
                window.location.href = contextPath + "/public/login/login.html";
            });
        });
    }


})();

function updateNotificationBadge(count) {
    const badge = document.getElementById("notification_badge");
    if (count > 0) {
        badge.textContent = count;
        badge.classList.remove("d-none");
    } else {
        badge.textContent = "";
        badge.classList.add("d-none");
    }
}