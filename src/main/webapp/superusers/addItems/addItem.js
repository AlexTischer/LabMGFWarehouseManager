function createNewItemForm() {

    const form = document.createElement("form");
    form.setAttribute("class", "mt-4");
    form.setAttribute("id", "addItemForm");

    const nameLabel = document.createElement("label");
    nameLabel.classList.add("form-label", "display-8", "mt-3");
    nameLabel.setAttribute("for", "nameInput");
    nameLabel.innerText = "Name";
    form.appendChild(nameLabel);

    const nameInput = document.createElement("input");
    nameInput.classList.add("form-control", "display-8");
    nameInput.required = true;
    nameInput.setAttribute("type", "text");
    nameInput.setAttribute("id", "nameInput");
    nameInput.setAttribute("name", "name");
    nameInput.setAttribute("placeholder", "Name");
    form.appendChild(nameInput);

    const serialNumberLabel = document.createElement("label");
    serialNumberLabel.classList.add("form-label", "display-8", "mt-3");
    serialNumberLabel.setAttribute("for", "serialNumberInput");
    serialNumberLabel.innerText = "Serial Number";
    form.appendChild(serialNumberLabel);

    const serialNumberInput = document.createElement("input");
    serialNumberInput.classList.add("form-control", "display-8");
    serialNumberInput.required = true;
    serialNumberInput.setAttribute("type", "text");
    serialNumberInput.setAttribute("id", "serialNumberInput");
    serialNumberInput.setAttribute("name", "serialNumber");
    serialNumberInput.setAttribute("placeholder", "Serial Number");
    form.appendChild(serialNumberInput);

    const inventoryNumberLabel = document.createElement("label");
    inventoryNumberLabel.classList.add("form-label", "display-8", "mt-3");
    inventoryNumberLabel.setAttribute("for", "inventoryNumberInput");
    inventoryNumberLabel.innerText = "Inventory Number";
    form.appendChild(inventoryNumberLabel);

    const inventoryNumberInput = document.createElement("input");
    inventoryNumberInput.classList.add("form-control", "display-8");
    inventoryNumberInput.required = true;
    inventoryNumberInput.setAttribute("type", "text");
    inventoryNumberInput.setAttribute("id", "inventoryNumberInput");
    inventoryNumberInput.setAttribute("name", "inventoryNumber");
    inventoryNumberInput.setAttribute("placeholder", "Inventory Number");
    form.appendChild(inventoryNumberInput);

    const locationLabel = document.createElement("label");
    locationLabel.classList.add("form-label", "display-8", "mt-3");
    locationLabel.setAttribute("for", "locationSelect");
    locationLabel.innerText = "Location";
    form.appendChild(locationLabel);

    const locationSelect = document.createElement("select");
    locationSelect.classList.add("form-control", "display-8");
    locationSelect.required = true;
    locationSelect.setAttribute("id", "locationSelect");
    locationSelect.setAttribute("name", "location");

    const locationOption = document.createElement("option");
    locationOption.setAttribute("value", "0");
    locationOption.innerText= "Milano-Leonardo";
    locationSelect.appendChild(locationOption);

    const locationOption2 = document.createElement("option");
    locationOption2.setAttribute("value", "1");
    locationOption2.innerText= "Piacenza";
    locationSelect.appendChild(locationOption2);

    form.appendChild(locationSelect);

    const descriptionLabel = document.createElement("label");
    descriptionLabel.classList.add("form-label", "display-8", "mt-3");
    descriptionLabel.setAttribute("for", "descriptionInput");
    descriptionLabel.innerText = "Description";
    form.appendChild(descriptionLabel);

    const descriptionInput = document.createElement("textarea");
    descriptionInput.classList.add("form-control", "display-8");
    descriptionInput.required = true;
    descriptionInput.setAttribute("id", "descriptionInput");
    descriptionInput.setAttribute("name", "description");
    descriptionInput.setAttribute("placeholder", "Description");
    form.appendChild(descriptionInput);

    const photoLabel = document.createElement("label");
    photoLabel.classList.add("form-label", "display-8", "mt-3");
    photoLabel.setAttribute("for", "ItemPhotoInput");
    photoLabel.innerText = "Photo";
    form.appendChild(photoLabel);

    const photoInput = document.createElement("input");
    photoInput.classList.add("form-control", "display-8");
    photoInput.required = true;
    photoInput.setAttribute("type", "file");
    photoInput.setAttribute("id", "ItemPhotoInput");
    photoInput.setAttribute("name", "file");
    photoInput.setAttribute("accept", "image/*");
    form.appendChild(photoInput);

    const submitRow = document.createElement("div");
    submitRow.classList.add("row", "justify-content-end", "mt-4");
    submitRow.setAttribute("id", "submitRow");
    const submitButton = document.createElement("button");

    submitButton.setAttribute("class", "btn btn-primary mt-4 col-2");
    submitButton.setAttribute("type", "submit");
    submitButton.innerText = "Submit";

    submitButton.addEventListener("click", function (event) {
        event.preventDefault();

        let data = JSON.stringify({"type": document.getElementById("ItemTypeSelect").value});

        makeCall("POST", contextPath + "/SuperUser/InsertItem", document.getElementById("addItemForm"), data,
            function (req) {
                const item = JSON.parse(req.responseText);
                const type = item.type;

                console.log(item);

                if(type === 0) {
                    linkTool(item);
                } else if(type === 1) {
                    linkAccessory(item);
                }
            }
        );
    });

    submitRow.appendChild(submitButton);

    form.appendChild(submitRow);

    document.getElementById("formContainer").innerHTML = "";
    document.getElementById("formContainer").appendChild(form);

}