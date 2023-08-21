let requestedTools = [];
let requestedMap = new Map();

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
        }
    });

    getAvailableTools()

    $('#itemsTable').on('check.bs.table', function (e, row) {
        if(row.type === 0){
            if(!requestedTools.includes(row.id)){
                requestedTools.push(row.id);
            }
        } else {
            let toolId = parseInt(e.target.id.substring(0, e.target.id.indexOf("_")));
            if(!requestedMap.has(toolId)) {
                requestedMap.set(toolId, []);
            }
            if(!requestedMap.get(toolId).includes(row.id)){
                requestedMap.get(toolId).push(row.id);
            }
        }

        updateRequestedItems();

        updateCalendar();
    });
    $('#itemsTable').on('uncheck.bs.table', function (e, row) {
        if(row.type === 0){
            requestedTools.splice(requestedTools.indexOf(row.id), 1);
        } else {
            for(let tool of requestedMap.entries()){
                if(tool[1].includes(row.id)){
                    let accessories = tool[1];
                    accessories.splice(accessories.indexOf(row.id), 1);
                    requestedMap.set(tool[0], accessories);
                }
            }
        }

        updateRequestedItems();

        updateCalendar();
    });

})();

function getAvailableTools(){

    makeCall("GET", contextPath + "/GetAvTools" + dateRangeString, null, null,
        function (req) {

            $("#itemsTable").bootstrapTable('collapseAllRows');

            const jsonData = JSON.parse(req.responseText);
            $("#itemsTable").bootstrapTable('load', jsonData);

            for(let tool of jsonData){
                if(requestedTools.includes(tool.id)){
                    $("#itemsTable").bootstrapTable('checkBy', {field: 'id', values: [tool.id]});
                }
            }
            for(let tool of requestedMap.entries()){
                if(!jsonData.map(tool => tool.id).includes(tool[0])){
                    requestedMap.delete(tool[0]);
                }
            }
            for(let tool of requestedTools){
                if(!jsonData.map(tool => tool.id).includes(tool)){
                    requestedTools.splice(requestedTools.indexOf(tool), 1);
                }
            }
            updateRequestedItems();

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

                });

                $(accessoriesTable).bootstrapTable('load', jsonData);

                for(let accessory of jsonData){
                    for(let tool of requestedMap.entries()){
                        if(tool[1].includes(accessory.id)){
                            $(accessoriesTable).bootstrapTable('checkBy', {field: 'id', values: [accessory.id]});
                        }
                    }
                }

                for(let tool of requestedMap.entries()){
                    for(let accessory of tool[1]){
                        if(!jsonData.map(acc => acc.id).includes(accessory)){
                            tool[1].splice(tool[1].indexOf(accessory), 1);
                        }
                    }
                }

                document.getElementById(id + "_detailsDiv").appendChild(accessoriesTable);

            }
        },
        function () {
            console.log("error");
        }
    );
}

function updateRequestedItems(){
    requestedItems = [];
    requestedItems.push(...requestedTools)
    console.log(requestedItems);
    for(let tool of requestedMap.entries()){
        for(let accessory of tool[1]){
            if(!requestedItems.includes(accessory)) {
                requestedItems.push(accessory);
            }
        }
    }
    console.log(requestedItems);
}

