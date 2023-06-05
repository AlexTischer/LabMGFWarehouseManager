//todo: add Title, instructions, submit button
//todo: check whether items are correctly selected from db
//todo $table.bootstrapTable('getSelections') returns an array of all selected items


function linkTool(item){

    console.log(item);

    makeCall("GET", contextPath + "/SuperUser/LinkItems" + "?id=" + item.id + "&type=" + 0, null, null,
        function (req){
                    const jsonData = JSON.parse(req.responseText);
                    const linkedAccessories = jsonData.linkedAccessories;
                    const nonLinkedAccessories = jsonData.nonLinkedAccessories;
                    const linkedDocuments = jsonData.linkedDocuments;
                    const nonLinkedDocuments = jsonData.nonLinkedDocuments;
                    const jsonItemsColumns = [
                        {field: 'linked', checkbox: true},
                        {field: 'name', title: 'Name', sortable: true},
                        {field: 'description', title: 'Description', sortable: true},
                        {field: 'serialNumber', title: 'Serial Number', sortable: true},
                        {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
                        {field: 'location', title: 'Location', formatter: "locationFormatter", sortable: true}
                    ];
                    $(function() {
                        const $accessoriesTable = $('#linkableAccessoriesTable').bootstrapTable({
                            data: linkedAccessories,
                            columns: jsonItemsColumns,
                            search: true,
                        });
                        $accessoriesTable.bootstrapTable('checkAll')
                        $accessoriesTable.bootstrapTable('append', nonLinkedAccessories);
                    });
                    const jsonDocumentsColumns = [
                        {field: 'linked', checkbox: true},
                        {field: 'name', title: 'Name', sortable: true},
                        {field: 'type', title: 'Type', sortable: true},
                        {field: 'language', title: 'Language', sortable: true},
                    ];
                    $(function() {
                        const $documentsTable = $('#linkableDocumentsTable').bootstrapTable({
                            data: linkedDocuments,
                            columns: jsonDocumentsColumns,
                            search: true,
                        });
                        $documentsTable.bootstrapTable('checkAll')
                        $documentsTable.bootstrapTable('append', nonLinkedDocuments);
                    });

                    document.getElementById("linkInstruction").textContent = "Select the accessories and documents you want to link to "
                        + item.name +
                        " (serial Number: " + item.serialNumber +
                        ", inventory Number: " + item.inventoryNumber + ").";
                    document.getElementById("actionSelectDiv").style.display = "none";
                    document.getElementById("addItemDiv").style.display = "none";
                    document.getElementById("linkableAccessoriesDiv").style.display = "table";
                    document.getElementById("linkableDocumentsDiv").style.display = "table";
                    document.getElementById("linkableToolsDiv").style.display = "none";
                    document.getElementById("linkItemDiv").style.display = "block";
                },

        function (){
                    console.log("error");
                }
    );
}

function linkAccessory(item){

    makeCall("GET", contextPath + "/SuperUser/LinkItems" + "?id=" + item.id + "&type=" + 1, null, null,
        function (req){
            const jsonData = JSON.parse(req.responseText);
            const linkedTools = jsonData.linkedTools;
            const nonLinkedTools = jsonData.nonLinkedTools;
            const linkedDocuments = jsonData.linkedDocuments;
            const nonLinkedDocuments = jsonData.nonLinkedDocuments;
            const jsonItemsColumns = [
                {field: 'linked', checkbox: true},
                {field: 'name', title: 'Name', sortable: true},
                {field: 'description', title: 'Description', sortable: true},
                {field: 'serialNumber', title: 'Serial Number', sortable: true},
                {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
                {field: 'location', title: 'Location', formatter: "locationFormatter", sortable: true}
            ];
            $(function() {
                const $toolsTable = $('#linkableToolsTable').bootstrapTable({
                    data: linkedTools,
                    columns: jsonItemsColumns,
                    search: true,
                });
                $toolsTable.bootstrapTable('checkAll')
                $toolsTable.bootstrapTable('append', nonLinkedTools);
            });
            const jsonDocumentsColumns = [
                {field: 'linked', checkbox: true},
                {field: 'name', title: 'Name', sortable: true},
                {field: 'type', title: 'Type', sortable: true},
                {field: 'language', title: 'Language', sortable: true},
            ];
            $(function() {
                const $documentsTable = $('#linkableDocumentsTable').bootstrapTable({
                    data: linkedDocuments,
                    columns: jsonDocumentsColumns,
                    search: true,
                });
                $documentsTable.bootstrapTable('checkAll')
                $documentsTable.bootstrapTable('append', nonLinkedDocuments);
            });

            document.getElementById("linkInstruction").textContent = "Select the tools and documents you want to link to "
                + item.name +
                " (serial Number: " + item.serialNumber +
                ", inventory Number: " + item.inventoryNumber + ").";
            document.getElementById("actionSelectDiv").style.display = "none";
            document.getElementById("addItemDiv").style.display = "none";
            document.getElementById("linkableToolsDiv").style.display = "table";
            document.getElementById("linkableDocumentsDiv").style.display = "table";
            document.getElementById("linkableAccessoriesDiv").style.display = "none";
            document.getElementById("linkItemDiv").style.display = "block";
            },

        function (){
            console.log("error");
        }
    );
}

function linkDocument(item){

    makeCall("GET", contextPath + "/SuperUser/LinkItems" + "?id=" + item.id + "&type=" + 2, null, null,
        function (req){
            const jsonData = JSON.parse(req.responseText);
            const linkedTools = jsonData.linkedTools;
            const nonLinkedTools = jsonData.nonLinkedTools;
            const linkedAccessories = jsonData.linkedAccessories
            const nonLinkedAccessories = jsonData.nonLinkedAccessories;
            const jsonItemsColumns = [
                {field: 'linked', checkbox: true},
                {field: 'name', title: 'Name', sortable: true},
                {field: 'description', title: 'Description', sortable: true},
                {field: 'serialNumber', title: 'Serial Number', sortable: true},
                {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
                {field: 'location', title: 'Location', formatter: "locationFormatter", sortable: true}
            ];
            $(function() {
                const $toolsTable = $('#linkableToolsTable').bootstrapTable({
                    data: linkedTools,
                    columns: jsonItemsColumns,
                    search: true,
                });
                $toolsTable.bootstrapTable('checkAll')
                $toolsTable.bootstrapTable('append', nonLinkedTools);
            });
            $(function() {
                const $accessoriesTable = $('#linkableAccessoriesTable').bootstrapTable({
                    data: linkedAccessories,
                    columns: jsonItemsColumns,
                    search: true,
                });
                $accessoriesTable.bootstrapTable('checkAll')
                $accessoriesTable.bootstrapTable('append', nonLinkedAccessories);
            });

            document.getElementById("linkInstruction").textContent = "Select the tools and accessories you want to link to "
                + item.name +
                " (type: " + item.type +
                ", language: " + item.language + ").";
            document.getElementById("actionSelectDiv").style.display = "none";
            document.getElementById("addItemDiv").style.display = "none";
            document.getElementById("linkInstruction").innerHTML = "Select the tools and accessories to link to the document";
            document.getElementById("linkableToolsDiv").style.display = "table";
            document.getElementById("linkableAccessoriesDiv").style.display = "table";
            document.getElementById("linkableDocumentsDiv").style.display = "none";
            document.getElementById("linkItemDiv").style.display = "block";
            },

        function (){
            console.log("error");
        }
    );
}