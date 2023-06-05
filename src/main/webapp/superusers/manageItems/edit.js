
(function () {
    window.editActionEvents = {
        'click .edit': function (e, value, row, index) {
            alert('You click edit icon, row: ' + JSON.stringify(row));
            //todo: OpenModal with linking section and edit name, description, photo, location
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