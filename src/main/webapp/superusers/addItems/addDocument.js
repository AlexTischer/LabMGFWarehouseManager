function createNewDocumentForm() {

    const form = document.createElement("form");
    form.setAttribute("class", "mt-4");
    form.setAttribute("id", "addDocumentForm");

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

    const typeLabel = document.createElement("label");
    typeLabel.classList.add("form-label", "display-8", "mt-3");
    typeLabel.setAttribute("for", "typeSelect");
    typeLabel.innerText = "Type";
    form.appendChild(typeLabel);

    const typeSelect = document.createElement("select");
    typeSelect.classList.add("form-control", "display-8");
    typeSelect.required = true;
    typeSelect.setAttribute("id", "typeSelect");
    typeSelect.setAttribute("name", "documentType");

    const typeOption = document.createElement("option");
    typeOption.setAttribute("value", "0");
    typeOption.innerText= "Generic";
    typeSelect.appendChild(typeOption);

    const typeOption2 = document.createElement("option");
    typeOption2.setAttribute("value", "1");
    typeOption2.innerText= "Instructions";
    typeSelect.appendChild(typeOption2);

    const typeOption3 = document.createElement("option");
    typeOption3.setAttribute("value", "2");
    typeOption3.innerText= "Manual";
    typeSelect.appendChild(typeOption3);

    const typeOption4 = document.createElement("option");
    typeOption4.setAttribute("value", "3");
    typeOption4.innerText= "Safety";
    typeSelect.appendChild(typeOption4);

    form.appendChild(typeSelect);

    const languageLabel = document.createElement("label");
    languageLabel.classList.add("form-label", "display-8", "mt-3");
    languageLabel.setAttribute("for", "languageSelect");
    languageLabel.innerText = "Language";
    form.appendChild(languageLabel);

    const languageSelect = document.createElement("select");
    languageSelect.classList.add("form-control", "display-8");
    languageSelect.required = true;
    languageSelect.setAttribute("id", "languageSelect");
    languageSelect.setAttribute("name", "language");

    const languageOption = document.createElement("option");
    languageOption.setAttribute("value", "0");
    languageOption.innerText= "Italian";
    languageSelect.appendChild(languageOption);

    const languageOption2 = document.createElement("option");
    languageOption2.setAttribute("value", "1");
    languageOption2.innerText= "English";
    languageSelect.appendChild(languageOption2);

    form.appendChild(languageSelect);

    const documentLabel = document.createElement("label");
    documentLabel.classList.add("form-label", "display-8", "mt-3");
    documentLabel.setAttribute("for", "ItemDocumentInput");
    documentLabel.innerText = "Document";
    form.appendChild(documentLabel);

    const documentInput = document.createElement("input");
    documentInput.classList.add("form-control", "display-8");
    documentInput.required = true;
    documentInput.setAttribute("type", "file");
    documentInput.setAttribute("id", "ItemDocumentInput");
    documentInput.setAttribute("name", "file");
    form.appendChild(documentInput);

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

        makeCall("POST", contextPath + "/SuperUser/InsertDocument", document.getElementById("addDocumentForm"), data,
            function (req) {
                const item = JSON.parse(req.responseText);
                const type = item.type;
                console.log(item);

                linkDocument(item);
            }
        );
    });


    submitRow.appendChild(submitButton);

    form.appendChild(submitRow);

    document.getElementById("formContainer").innerHTML = "";
    document.getElementById("formContainer").appendChild(form);

}