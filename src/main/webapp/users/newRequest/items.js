let lastExpandedTool;
let lastExpandedAccessory;

(function (){
    const jsonColumns = [
        {field: 'checkbox', title: 'Select', checkbox: true},
        {field: 'image', title: 'Image', sortable: false, formatter: "imageFormatter"},
        {field: 'name', title: 'Name', sortable: true},
        {field: 'location', title: 'Location', sortable: true},
    ];

    $('#itemsTable').bootstrapTable({
        classes: "table",
        columns: jsonColumns,
        search: true,
        uniqueId: "id",
        checkboxHeader: false,
        detailView: true,
        detailViewIcon: true,
        detailFormatter: "toolDetailFormatter",
        onExpandRow: function (index, row) {
            getAvailableAccessories(row.id);
            if(lastExpandedTool !== undefined && lastExpandedTool !== row){
                $('#itemsTable').bootstrapTable('collapseRowByUniqueId', lastExpandedTool.id);
            }
            lastExpandedTool = row;
        }
    });

    getAvailableTools()

    $('#itemsTable').on('check.bs.table', function (e, row) {
        if(!requestedItems.includes(row.id)){
            requestedItems.push(row.id);
        }
        console.log(requestedItems);
        updateCalendar();
    });
    $('#itemsTable').on('uncheck.bs.table', function (e, row) {
            requestedItems.splice(requestedItems.indexOf(row.id), 1);
            console.log(requestedItems);
            updateCalendar();
            //todo: if unchecked row is a tool, uncheck all linked selected accessories
    });

})();

function getAvailableTools(){

    makeCall("GET", contextPath + "/GetAvTools" + dateRangeString, null, null,
        function (req) {
            const jsonData = JSON.parse(req.responseText);
            $("#itemsTable").bootstrapTable('load', jsonData);
            let newRequestedTools = [];
            for(let i = 0; i < jsonData.length; i++){
                if(requestedItems.includes(jsonData[i].id)){
                    if(!newRequestedTools.includes(jsonData[i].id)) {
                        newRequestedTools.push(jsonData[i].id);
                    }
                    requestedItems.splice(requestedItems.indexOf(jsonData[i].id), 1);
                }
            }

            $("#itemsTable").bootstrapTable('checkBy', {field: "id", values: newRequestedTools});
        },
        function () {
            console.log("error");
        }
    );
}

function getAvailableAccessories(id){

    makeCall("GET", contextPath + "/GetAvAccessories" + (dateRangeString!=="" ? dateRangeString + "&id=" + id : "?id=" + id), null, null,
            function (req) {
                const jsonData = JSON.parse(req.responseText);
                console.log(jsonData);

                if(jsonData!== null && jsonData.length !== 0) {

                    const accessoriesTitle = document.createElement("h4");
                    accessoriesTitle.setAttribute("class", "m-3");
                    accessoriesTitle.innerHTML = "Available accessories: ";

                    const accessoriesTable = document.createElement("table");
                    accessoriesTable.setAttribute("class", "table");
                    accessoriesTable.setAttribute("id", id + "_accessoriesTable");

                    const jsonColumns = [
                        {field: 'checkbox', title: 'Select', checkbox: true},
                        {field: 'image', title: 'Image', sortable: false, formatter: "imageFormatter"},
                        {field: 'name', title: 'Name', sortable: true},
                        {field: 'location', title: 'Location', sortable: true},
                    ];

                    $(accessoriesTable).bootstrapTable({
                        classes: "",
                        columns: jsonColumns,
                        search: false,
                        checkboxHeader: false,
                        uniqueId: "id",
                        showToggle: true,
                        detailView: true,
                        detailFormatter: "accessoryDetailFormatter",
                        onExpandRow: function (index, row) {
                            if(lastExpandedAccessory !== undefined){
                                $(accessoriesTable).bootstrapTable('collapseRowByUniqueId', lastExpandedAccessory.id);
                            }
                            lastExpandedAccessory = row;
                        }

                    });

                    $(accessoriesTable).bootstrapTable('load', jsonData);

                    for (let i = 0; i < jsonData.length; i++) {
                        if(requestedItems.includes(jsonData[i].id)){
                            $(accessoriesTable).bootstrapTable('checkBy', {field: "id", values: [jsonData[i].id]});
                        }
                    }

                    document.getElementById(id + "_detailsDiv").appendChild(accessoriesTable);

                    //todo: keep track of selected accessories per tool

                    $(accessoriesTable).on('check.bs.table', function (e, row) {
                        $("#itemsTable").bootstrapTable('checkBy', {field: "id", values: [id]});
                    });
                    $(accessoriesTable).on('uncheck.bs.table', function (e, row) {
                    });


                }
            },
            function () {
                console.log("error");
            }
        );
}

