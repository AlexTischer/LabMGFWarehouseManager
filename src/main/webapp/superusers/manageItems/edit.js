//todo: add option to set items in maintenance mode and to bring them back from maintenance mode:
//  modal window with:
//      text area for reason of maintenance,
//      datepickers for start and end of maintenance
//      confirm/cancel buttons

(function () {
    window.editActionEvents = {
        'click .edit': function (e, value, row, index) {
            openModal(function () {
                makeCall("GET", contextPath + "/GetFile" + "?path=" + encodeURIComponent(row.imagePath), null, null,
                    function (req) {
                        var reader = new FileReader();
                        reader.onload = function(e) {
                            photo.setAttribute('src', e.target.result);
                        };
                        reader.readAsDataURL(req.response);
                    },
                    function () {
                        console.log("error");
                    }, "blob"
                );


                console.log(JSON.stringify(row));

                const modalTitle = document.getElementById("modal_title");
                modalTitle.innerHTML = "Edit " + row.name;

                const modal = document.getElementById("modal_content_body");

                var form = document.createElement("form");
                form.setAttribute("id", "editItemForm");

                const nameDiv = document.createElement("div");
                nameDiv.setAttribute("class", "mb-3");

                const nameLabel = document.createElement("label");
                nameLabel.setAttribute("for", "name");
                nameLabel.innerHTML = "Name:";
                nameDiv.appendChild(nameLabel);

                const nameInput = document.createElement("input");
                nameInput.setAttribute("type", "text");
                nameInput.setAttribute("id", "edited_name");
                nameInput.setAttribute("name", "name");
                nameInput.setAttribute("value", row.name);
                nameInput.setAttribute("required", "required");
                nameInput.setAttribute("class", "form-control");
                nameDiv.appendChild(nameInput);

                form.appendChild(nameDiv);

                const descriptionDiv = document.createElement("div");
                descriptionDiv.setAttribute("class", "mb-3");

                const descriptionLabel = document.createElement("label");
                descriptionLabel.setAttribute("for", "description");
                descriptionLabel.innerHTML = "Description:";
                descriptionDiv.appendChild(descriptionLabel);

                const descriptionInput = document.createElement("textarea");
                descriptionInput.setAttribute("id", "edited_description");
                descriptionInput.setAttribute("name", "description");
                descriptionInput.textContent = row.description;
                descriptionInput.setAttribute("required", "required");
                descriptionInput.setAttribute("class", "form-control");
                descriptionDiv.appendChild(descriptionInput);
                form.appendChild(descriptionDiv);

                const photoDiv = document.createElement("div");
                photoDiv.setAttribute("class", "mb-3");

                const photoLabel = document.createElement("label");
                photoLabel.setAttribute("for", "photo");
                photoLabel.innerHTML = "Photo:";
                photoDiv.appendChild(photoLabel);

                const photo = document.createElement("img");
                photo.setAttribute("alt", "Photo");
                photo.setAttribute("class", "img-thumbnail");
                photo.setAttribute("width", "200");
                photo.setAttribute("height", "auto");
                photoDiv.appendChild(photo);

                const photoInput = document.createElement("input");
                photoInput.setAttribute("type", "file");
                photoInput.setAttribute("id", "edited_photo");
                photoInput.setAttribute("name", "photo");
                photoInput.setAttribute("value", row.photo);
                photoDiv.appendChild(photoInput);
                form.appendChild(photoDiv);

                const locationDiv = document.createElement("div");
                locationDiv.setAttribute("class", "mb-3");

                const locationLabel = document.createElement("label");
                locationLabel.setAttribute("for", "location");
                locationLabel.innerHTML = "Location:";
                locationDiv.appendChild(locationLabel);

                const locationInput = document.createElement("select");
                locationInput.setAttribute("id", "edited_location");
                locationInput.setAttribute("name", "location");
                locationInput.setAttribute("required", "required");
                locationInput.setAttribute("class", "form-control");

                const locationOption = document.createElement("option");
                locationOption.setAttribute("value", "0");
                locationOption.text = "Milano-Leonardo";
                locationInput.appendChild(locationOption);

                const locationOption2 = document.createElement("option");
                locationOption2.setAttribute("value", "1");
                locationOption2.text = "Pavia";
                locationInput.appendChild(locationOption2);

                for(let i = 0; i < locationInput.options.length; i++) {
                    if(locationInput.options[i].text === row.location) {
                        locationInput.selectedIndex = i;
                        break;
                    }
                }

                locationDiv.appendChild(locationInput);

                form.appendChild(locationDiv);

                modal.appendChild(form);

                const modalFooter = document.getElementById("modal_footer");

                const saveButton = document.createElement("button");
                saveButton.setAttribute("type", "button");
                saveButton.setAttribute("class", "btn btn-primary");
                saveButton.innerHTML = "Save changes";
                saveButton.addEventListener("click", function () {
                    document.getElementById("modalWindow").style.display = "none";
                    makeCall("POST", contextPath + "/SuperUser/EditItem" + "?id=" + row.id, form, null,
                        function () {
                            edit();
                        },
                        function () {
                            console.log("error");
                        }
                    );
                });
                modalFooter.appendChild(saveButton);

                const linkButton = document.createElement("button");
                linkButton.setAttribute("type", "button");
                linkButton.setAttribute("class", "btn btn-secondary");
                linkButton.innerHTML = "Edit links";
                linkButton.addEventListener("click", function () {

                    saveButton.dispatchEvent(new Event("click"));

                    if(row.type == 2) {
                        const item = {
                            "id": row.id,
                            "name": row.name,
                            "type": row.type,
                            "language": row.language,
                            "path": row.path,
                            "isLinked": row.isLinked

                        }
                       linkDocument(item);
                    } else {
                        const item = {
                            "id": row.id,
                            "name": row.name,
                            "description": row.description,
                            "type": row.type,
                            "serialNumber": row.serialNumber,
                            "inventoryNumber": row.inventoryNumber,
                            "location": row.location,
                            "imagePath": row.imagePath,
                            "isLinked": row.isLinked
                        }
                        if(row.type == 1) {
                            linkAccessory(item);
                        } else {
                            linkTool(item);
                        }
                    }
                    document.getElementById("editItemDiv").style.display = "none";
                    document.getElementById("modalWindow").style.display = "none";
                });
                modalFooter.appendChild(linkButton);
            });
        },
        'click .remove': function (e, value, row, index) {
            openConfirmPrompt("Are you sure you want to remove this item?", function () {
                makeCall("POST", contextPath + (row.type !== 2 ? "/SuperUser/RemoveItem" : "/SuperUser/RemoveDocument") + "?id=" + row.id , null, null,
                    function (req) {
                                edit();
                    },
                    function (req) {
                                console.log("error");
                    }
                );
            });
        }
    };

    const jsonColumns = [
        {field: 'name', title: 'Name', sortable: true},
        {field: 'description', title: 'Description', sortable: true},
        {field: 'serialNumber', title: 'Serial Number', sortable: true},
        {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
        {field: 'location', title: 'Location', sortable: true},
        {field: 'action', formatter: "editActionFormatter", events: "editActionEvents"}
    ];

    $('#editItemsTable').bootstrapTable({
        columns: jsonColumns,
        search: true,
    });
})();

function edit() {
    document.getElementById("ItemTypeSelect").addEventListener("change", function () {
        if(this.value !== "2") {
            let path;
            if (this.value === "0") {
                path = "/GetAvTools";
            } else if (this.value === "1") {
                path = "/GetAvAccessories";
            }
            makeCall("GET", contextPath + path, null, null,
                function (req) {
                    const jsonData = JSON.parse(req.responseText);
                    console.log(jsonData);
                    $(function () {
                        $('#editItemsTable').bootstrapTable('load', jsonData);
                    });
                },
                function () {
                    console.log("error");
                }
            );
        } else {
            //todo: get documents
        }
    });

    document.getElementById("ItemTypeSelect").dispatchEvent(new Event("change"));
}