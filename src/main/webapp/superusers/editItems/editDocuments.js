function editDocuments(){

    window.editDocumentActionEvents = {
        'click .remove': function (e, value, row, index) {removeDocumentAction(row)},
    }

    const jsonDocumentsColumns = [
        {field: 'name', title: 'Name', sortable: true},
        {field: 'language', title: 'Language', sortable: true},
        {field: 'type', title: 'Type', sortable: true},
        {field: 'action', formatter: "editDocumentActionFormatter", events: "editDocumentActionEvents"}
    ];

    const container = document.getElementById("editTablesContainer");
    container.innerHTML = "";

    const documentsTableLabel = document.createElement("h6");
    documentsTableLabel.innerHTML = "Documents";
    documentsTableLabel.classList.add("display-5", "mv-4");
    container.appendChild(documentsTableLabel);

    const documentsTable = document.createElement("table");
    documentsTable.setAttribute("id", "editDocumentsTable");
    documentsTable.classList.add("table");

    container.appendChild(documentsTable);

    $(documentsTable).bootstrapTable({
            columns: jsonDocumentsColumns,
            search: true,
        }
    );

    makeCall("GET", contextPath + "/SuperUser/GetDocuments", null, null,
        function (req) {
            const jsonData = JSON.parse(req.responseText);
            console.log(jsonData);
            $(function () {
                $(documentsTable).bootstrapTable('load', jsonData);
            });
        }
    );
}

function removeDocumentAction(row){
    openConfirmPrompt("Are you sure you want to remove this document?", function () {
        makeCall("POST", contextPath + "/SuperUser/RemoveDocument" + "?id=" + row.id , null, null,            function (req) {
                editDocuments();
            }
        );
    });
}
