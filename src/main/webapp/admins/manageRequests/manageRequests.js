let lastExpandedRequest;


(function() {

    window.requestActionEvents = {
        'click .pending': function (e, value, row, index) {
            updateRequestStatus(row.id, 0);
        },
        'click .approve': function (e, value, row, index) {
            updateRequestStatus(row.id, 1);
        },
        'click .decline': function (e, value, row, index) {
            updateRequestStatus(row.id, 2);
        }
    };

    const jsonColumns = [
        {field: 'start', title: 'Start Date', sortable: true},
        {field: 'end', title: 'End Date', sortable: true},
        {field: 'status', title: 'Status', sortable: true},
        {field: 'requestingUser', title: 'Requesting User', formatter: userFormatter, sortable: true},
        {field: 'actions', title: 'Actions', formatter: adminRequestActionsFormatter, events: requestActionEvents}
    ];

    $('#requestsTable').bootstrapTable({
        classes: "table",
        columns: jsonColumns,
        search: true,
        uniqueId: "id",
        checkboxHeader: false,
        detailView: true,
        detailViewIcon: true,
        detailFormatter: "adminRequestDetailFormatter",
        onExpandRow: function (index, row, $detail) {
            if(lastExpandedRequest !== undefined && lastExpandedRequest !== row){
                $('#requestsTable').bootstrapTable('collapseRowByUniqueId', lastExpandedRequest.id);
            }
            lastExpandedRequest = row;
        },
    });

    let calendar = document.getElementById("calendar");
        calendar = new FullCalendar.Calendar(calendar, {
            themeSystem: 'bootstrap5',
            initialView: 'dayGridMonth',
            selectable: false,
            locale: 'en',
            firstDay: 1,
            headerToolbar: {
                themeSystem: 'bootstrap5',
                start: 'title',
                end: 'prevYear prev today next nextYear',
            },
            height: 'auto',
            datesSet: function(info) {
                makeCall("GET", contextPath + "/Admin/GetRequestsByTimeRange" + "?start=" + info.startStr + "&end=" + info.endStr, null, null,
                    function (req) {
                                const jsonData = JSON.parse(req.responseText);

                                $('#requestsTable').bootstrapTable('removeAll');
                                $('#requestsTable').bootstrapTable('load', jsonData);

                                calendar.removeAllEvents();

                                for(let i = 0; i < jsonData.length; i++){

                                    let color;
                                    if (jsonData[i].status === 'Pending') {
                                        color = 'yellow';
                                    } else if (jsonData[i].status === 'Approved') {
                                        color = 'green';
                                    } else {
                                        continue;
                                    }

                                    calendar.addEvent({
                                        id: jsonData[i].id,
                                        start: jsonData[i].start,
                                        end: jsonData[i].end,
                                        color: color,
                                    });
                                }

                                calendar.render();
                            }
                );
            }
        });

        calendar.render();

})();

function updateRequestStatus(id, status, items = null) {
    let documents;
    let adminNotes;
    if (status === 0) {
        sendUpdateRequestStatus(id, status);
    } else if (status === 1) {

        makeCall("GET", contextPath + "/SuperUser/LinkItems" + "?id=" + id + "&type=" + "3", null, null,
            function (req) {
                const jsonData = JSON.parse(req.responseText);

                const documentsDiv = document.createElement("div");
                documentsDiv.className = "mb-3";

                const title = document.createElement("h5");
                title.classList.add("display-6");

                if (jsonData.length !== 0) {

                    title.innerHTML = "Select the documents you want to link to this request and/or attach a new one.";

                    const documentsTable = document.createElement("table");
                    documentsTable.className = "table";
                    documentsTable.id = "documentsTable";

                    const jsonColumns = [
                        {field: 'linked', checkbox: true},
                        {field: 'name', title: 'Name', sortable: true},
                        {field: 'type', title: 'Type', sortable: true},
                        {field: 'language', title: 'Language', sortable: true},
                    ];

                    $(documentsTable).bootstrapTable({
                        classes: "table",
                        columns: jsonColumns,
                        search: true,
                        uniqueId: "id",
                        checkboxHeader: true,
                        data: jsonData,
                    });

                    documentsDiv.appendChild(documentsTable);

                } else {
                    title.innerHTML = "Attach a document to this request (optional)";
                }

                documentsDiv.insertBefore(title, documentsDiv.firstChild)

                const form = document.createElement("form");
                form.id = "documentsForm";


                const nameLabel = document.createElement("label");
                nameLabel.htmlFor = "nameInput";
                nameLabel.className = "form-label";
                nameLabel.textContent = "Name of the file";

                const nameInput = document.createElement("input");
                nameInput.type = "text";
                nameInput.name = "name";
                nameInput.className = "form-control";
                nameInput.id = "nameInput";


                const languageLabel = document.createElement("label");
                languageLabel.htmlFor = "languageInput";
                languageLabel.className = "form-label";
                languageLabel.textContent = "Language of the file";

                const languageInput = document.createElement("select");
                languageInput.name = "language";
                languageInput.className = "form-select";
                languageInput.id = "languageInput";

                const languageOption1 = document.createElement("option");
                languageOption1.value = "1";
                languageOption1.textContent = "English";

                const languageOption2 = document.createElement("option");
                languageOption2.value = "0";
                languageOption2.textContent = "Italian";

                languageInput.appendChild(languageOption1);
                languageInput.appendChild(languageOption2);


                const fileLabel = document.createElement("label");
                fileLabel.htmlFor = "fileInput";
                fileLabel.className = "form-label";
                fileLabel.textContent = "Attach a file to this request";

                const fileInput = document.createElement("input");
                fileInput.type = "file";
                fileInput.name = "file";
                fileInput.multiple = false;
                fileInput.className = "form-control";
                fileInput.id = "fileInput";


                const typeInput = document.createElement("input");
                typeInput.type = "hidden";
                typeInput.name = "documentType";
                typeInput.value = "4";

                form.appendChild(nameLabel);
                form.appendChild(nameInput)

                form.appendChild(languageLabel);
                form.appendChild(languageInput);

                form.appendChild(typeInput);

                form.appendChild(fileLabel);
                form.appendChild(fileInput);

                documentsDiv.appendChild(form);

                modal.appendChild(documentsDiv);
                openUpdateRequestModal(id, status, documentsDiv);
            }
        );

    } else if (status === 2) {
        openUpdateRequestModal(id, status);
    }
}

function openUpdateRequestModal(id, status, documentsDiv = null) {
    openModal(function () {
        const modal = document.getElementById("modal_content_body");
        const modalTitle = document.getElementById("modal_title");

        modalTitle.innerHTML = "Update Request";

        if(documentsDiv != null) {
            modal.appendChild(documentsDiv);
        }

        const adminNotesDiv = document.createElement("div");
        adminNotesDiv.className = "mb-3";

        const adminNotes = document.createElement("textarea");
        adminNotes.className = "form-control";
        adminNotes.id = "adminNotes";
        adminNotes.placeholder = "Add notes for the user here";

        adminNotesDiv.appendChild(adminNotes);
        modal.appendChild(adminNotesDiv);

        const confirm_btn = document.createElement("button");
        confirm_btn.textContent = "Confirm";
        confirm_btn.className = "btn btn-primary";

        const cancel_btn = document.createElement("button");
        cancel_btn.textContent = "Cancel";
        cancel_btn.className = "btn btn-secondary";

        modal.nextElementSibling.appendChild(confirm_btn);
        modal.nextElementSibling.appendChild(cancel_btn);

        confirm_btn.addEventListener('click', (e) => {
            if(documentsDiv != null) {
                const documentsTableSelectedData = $("#documentsTable").bootstrapTable('getSelections');

                const documents = [];

                for (let i = 0; i < documentsTableSelectedData.length; i++) {
                    documents.push(documentsTableSelectedData[i].id);
                }

                const form = document.getElementById("documentsForm");

                if(form.file.files.length !== 0){
                makeCall("POST", contextPath + "/SuperUser/InsertDocument" + "?id=" + id + "&type=" + "3", form, null,
                    function (req) {
                        const response = JSON.parse(req.responseText);
                        documents.push(response.id);
                        sendUpdateRequestStatus(id, status, adminNotes.value, documents);
                    }
                );
                } else {
                    sendUpdateRequestStatus(id, status, adminNotes.value, documents);
                }


            } else {
            sendUpdateRequestStatus(id, status, adminNotes.value);
            }
        });

        cancel_btn.addEventListener('click', (e) => {
            document.getElementById("modalWindow").style.display = "none";
        });
    });
}

function sendUpdateRequestStatus(id, status, adminNotes = null, documents = null) {
    const responseMap = new Map();
    responseMap.set("id", id);
    responseMap.set("status", status);
    responseMap.set("documents", documents);
    responseMap.set("adminNotes", adminNotes);

    openConfirmPrompt("Are you sure you want to update this request?", function () {
        makeCall("POST", contextPath + "/Admin/UpdateRequest", null, Array.from(responseMap),
            function (req) {
                openSuccessPrompt("Request updated", "The request has been updated successfully");
            }
        );
    });
}

