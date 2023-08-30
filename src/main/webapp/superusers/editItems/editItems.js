function editItems(type){

    window.editActionEvents = {
        'click .edit': function (e, value, row, index) {editItemAction(row)},
        'click .remove': function (e, value, row, index) {removeItemAction(row)},
        'click .maintenance': function (e, value, row, index) {maintenanceAction(row)},
    }

    const jsonItemsColumns = [
        {field: 'name', title: 'Name', sortable: true},
        {field: 'description', title: 'Description', sortable: true},
        {field: 'serialNumber', title: 'Serial Number', sortable: true},
        {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
        {field: 'location', title: 'Location', sortable: true},
        {field: 'action', formatter: "editActionFormatter", events: "editActionEvents"}
    ];

    const container = document.getElementById("editTablesContainer");
    container.innerHTML = "";

    const itemsTableLabel = document.createElement("h6");
    itemsTableLabel.innerHTML = type === 0 ? "Tools" : "Accessories";
    itemsTableLabel.classList.add("display-5", "mv-4");
    container.appendChild(itemsTableLabel);

    const itemsTable = document.createElement("table");
    itemsTable.setAttribute("id", "editItemsTable");
    itemsTable.classList.add("table");

    container.appendChild(itemsTable);

    $(itemsTable).bootstrapTable({
        columns: jsonItemsColumns,
        search: true,
        rowStyle: function (row, index) {
            if (row.isInMaintenance) {
                return {
                    classes: 'table-warning'
                };
            } else {
                return {
                    classes: ''
                };
            }
        }
        }
    );

    makeCall("GET", contextPath + (type === 0 ? "/GetAvTools" : "/GetAvAccessories"), null, null,
        function (req) {
            const jsonData = JSON.parse(req.responseText);
            console.log(jsonData);
            $(function () {
                $(itemsTable).bootstrapTable('load', jsonData);
            });
        }
    );
}

function editItemAction(row){
    openModal(function () {
        console.log(JSON.stringify(row));

        const modalTitle = document.getElementById("modal_title");
        modalTitle.innerHTML = "Edit " + row.name;

        const modal = document.getElementById("modal_content_body");

        const form = document.createElement("form");
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
        photoDiv.setAttribute("class", "mb-3", "row");

        const photoLabel = document.createElement("label");
        photoLabel.setAttribute("for", "photo");
        photoLabel.innerHTML = "Photo:";
        photoDiv.appendChild(photoLabel);

        const photo = document.createElement("img");
        photo.setAttribute("alt", "Photo");
        photo.setAttribute("class", "img-thumbnail");
        photo.setAttribute("width", "200");
        photo.setAttribute("height", "auto");
        photo.setAttribute("src", contextPath + "/GetFile" + "?path=" + encodeURIComponent(row.imagePath));
        photoDiv.appendChild(photo);

        const photoInput = document.createElement("input");
        photoInput.setAttribute("type", "file");
        photoInput.classList.add("form-control", "display-8");
        photoInput.setAttribute("id", "edited_photo");
        photoInput.setAttribute("name", "file");
        photoInput.setAttribute("value", row.photo);
        photoDiv.appendChild(photoInput);

        photoInput.addEventListener("change", function (event) {
            const selectedFile = event.target.files[0]; // Ottieni il file selezionato

            if (selectedFile) {
                const reader = new FileReader();

                // Leggi i dati del file e aggiorna l'attributo src dell'elemento img
                reader.onload = function (e) {
                    photo.setAttribute("src", e.target.result);
                };

                reader.readAsDataURL(selectedFile); // Leggi i dati del file come URL dati
            }
        });

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
        locationOption2.text = "Piacenza";
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
            makeCall("POST", contextPath + "/SuperUser/EditItem" + "?id=" + row.id, form, null,
                function () {
                    openSuccessPrompt("Success!", "Successfully saved changes to " + row.name);
                }
            );
        });
        modalFooter.appendChild(saveButton);

        const linkButton = document.createElement("button");
        linkButton.setAttribute("type", "button");
        linkButton.setAttribute("class", "btn btn-secondary");
        linkButton.innerHTML = "Edit links";
        linkButton.addEventListener("click", function () {
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

            if(row.type === 1) {
                linkAccessory(item);
            } else {
                linkTool(item);
            }
        });
        modalFooter.appendChild(linkButton);
    });}

function removeItemAction(row){
    openConfirmPrompt("Are you sure you want to remove this item?", function () {
        makeCall("POST", contextPath + "/SuperUser/RemoveItem" + "?id=" + row.id , null, null,
            function (req) {
                editItems(row.type);
            }
        );
    });
}

function maintenanceAction(row){
    if(row.isInMaintenance){
        openConfirmPrompt("Are you sure you want to bring this item back from maintenance?", function () {
            makeCall("POST", contextPath + "/SuperUser/UpdateMaintenance" + "?id=" + row.id + "&status=" + 0, null, null,
                function () {
                    editItems(row.type);
                }
                );
        });
    } else {
        openModal(function () {
            const modalTitle = document.getElementById("modal_title");
            modalTitle.innerHTML = "Maintenance for " + row.name;

            const modal = document.getElementById("modal_content_body");

            const form = document.createElement("form");
            form.setAttribute("id", "maintenanceForm");

            const reasonDiv = document.createElement("div");
            reasonDiv.setAttribute("class", "mb-3");

            const reasonLabel = document.createElement("label");
            reasonLabel.setAttribute("for", "reason");
            reasonLabel.innerHTML = "Reason:";
            reasonDiv.appendChild(reasonLabel);

            const reasonInput = document.createElement("textarea");
            reasonInput.setAttribute("id", "reason");
            reasonInput.setAttribute("name", "reason");
            reasonInput.setAttribute("required", "required");
            reasonInput.setAttribute("class", "form-control");
            reasonDiv.appendChild(reasonInput);
            form.appendChild(reasonDiv);

            const startDiv = document.createElement("div");
            startDiv.setAttribute("class", "mb-3");

            const startLabel = document.createElement("label");
            startLabel.setAttribute("for", "start");
            startLabel.innerHTML = "Start:";
            startDiv.appendChild(startLabel);

            const startInput = document.createElement("input");
            startInput.setAttribute("type", "date");
            startInput.setAttribute("id", "start");
            startInput.setAttribute("name", "start");
            startInput.setAttribute("required", "required");
            startInput.setAttribute("class", "form-control");
            startDiv.appendChild(startInput);
            form.appendChild(startDiv);

            const endDiv = document.createElement("div");
            endDiv.setAttribute("class", "mb-3");

            const endLabel = document.createElement("label");
            endLabel.setAttribute("for", "end");
            endLabel.innerHTML = "End:";
            endDiv.appendChild(endLabel);

            const endInput = document.createElement("input");
            endInput.setAttribute("type", "date");
            endInput.setAttribute("id", "end");
            endInput.setAttribute("name", "end");
            endInput.setAttribute("required", "required");
            endInput.setAttribute("class", "form-control");
            endDiv.appendChild(endInput);
            form.appendChild(endDiv);

            modal.appendChild(form);

            const modalFooter = document.getElementById("modal_footer");

            const saveButton = document.createElement("button");
            saveButton.setAttribute("type", "button");
            saveButton.setAttribute("class", "btn btn-primary");
            saveButton.innerHTML = "Save changes";
            saveButton.addEventListener("click", function () {
                document.getElementById("modalWindow").style.display = "none";
                makeCall("POST", contextPath + "/SuperUser/UpdateMaintenance" + "?id=" + row.id + "&status=" + 1, form, null,
                    function () {
                        editItems(row.type);
                    });
            });
            modalFooter.appendChild(saveButton);
        });
    }
}