const jsonItemsColumns = [
    {field: 'linked', checkbox: true},
    {field: 'name', title: 'Name', sortable: true},
    {field: 'description', title: 'Description', sortable: true},
    {field: 'serialNumber', title: 'Serial Number', sortable: true},
    {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
    {field: 'location', title: 'Location', formatter: "locationFormatter", sortable: true}
];

const jsonDocumentsColumns = [
    {field: 'linked', checkbox: true},
    {field: 'name', title: 'Name', sortable: true},
    {field: 'type', title: 'Type', sortable: true},
    {field: 'language', title: 'Language', sortable: true},
];

function linkTool(item) {
    linkItem(item, 0);
}

function linkAccessory(item){
    linkItem(item, 1);
}

function linkDocument(item){
    makeCall("GET", contextPath + "/SuperUser/LinkItems" + "?id=" + item.id + "&type=" + 2, null, null,
        function (req){
            const jsonData = JSON.parse(req.responseText);
            const linkedTools = jsonData.linkedTools;
            const nonLinkedTools = jsonData.nonLinkedTools;
            const linkedAccessories = jsonData.linkedAccessories
            const nonLinkedAccessories = jsonData.nonLinkedAccessories;

            openModal(function () {
                const modalTitle = document.getElementById("modal_title");
                modalTitle.innerHTML = "Link " + item.name;

                const modal = document.getElementById("modal_content_body");
                modal.innerHTML = "";

                const subtitle = document.createElement("h5");
                subtitle.innerHTML = "Select the tools and accessories you want to link to " + item.name +
                    " (type: " + item.type +
                    ", language: " + item.language + ").";
                subtitle.classList.add("display-7", "mt-4");
                modal.appendChild(subtitle);

                const toolsTableLabel = document.createElement("h6");
                toolsTableLabel.innerHTML = "Tools";
                toolsTableLabel.classList.add("display-5", "mt-4");
                modal.appendChild(toolsTableLabel);

                const toolsTable = document.createElement("table");
                toolsTable.setAttribute("id", "linkableToolsTable");
                toolsTable.classList.add("table");

                modal.appendChild(toolsTable);

                $(toolsTable).bootstrapTable({
                        data: linkedTools,
                        columns: jsonItemsColumns,
                        search: true,
                    }
                );
                $(toolsTable).bootstrapTable('checkAll');

                $(toolsTable).bootstrapTable('append', nonLinkedTools);

                const accessoriesTableLabel = document.createElement("h6");
                accessoriesTableLabel.innerHTML = "Accessories";
                accessoriesTableLabel.classList.add("display-5", "mt-4");
                modal.appendChild(accessoriesTableLabel);

                const accessoriesTable = document.createElement("table");
                accessoriesTable.setAttribute("id", "linkableAccessoriesTable");
                accessoriesTable.classList.add("table");

                $(accessoriesTable).bootstrapTable({
                        data: linkedAccessories,
                        columns: jsonItemsColumns,
                        search: true,
                    }
                );
                $(accessoriesTable).bootstrapTable('checkAll');
                $(accessoriesTable).bootstrapTable('append', nonLinkedAccessories);

                modal.appendChild(accessoriesTable);

                const modalFooter = document.getElementById("modal_footer");
                modalFooter.innerHTML = "";

                const saveButton = document.createElement("button");
                saveButton.setAttribute("type", "button");
                saveButton.setAttribute("data-dismiss", "modal");
                saveButton.classList.add("btn", "btn-primary");
                saveButton.innerHTML = "Save";
                saveButton.addEventListener("click", function () {
                        const tools = $(toolsTable).bootstrapTable('getSelections');
                        const accessories = $(accessoriesTable).bootstrapTable('getSelections');
                        const dataMap = new Map();
                        dataMap.set("type", 2);
                        dataMap.set("id", item.id);
                        dataMap.set("tools", tools.map(item => item.id));
                        dataMap.set("accessories", accessories.map(item => item.id));
                        const objFromMap = Array.from(dataMap.entries());
                        makeCall("POST", contextPath + "/SuperUser/LinkItems ", null, objFromMap,
                            function (req){
                                openSuccessPrompt("Success!", "Successfully linked " + item.name + " to the selected tools and accessories.");
                            }
                        );
                    });
                modalFooter.appendChild(saveButton);
            });
            }
    );
}

function linkItem(item, type) {
    makeCall("GET", contextPath + "/SuperUser/LinkItems" + "?id=" + item.id + "&type=" + type, null, null,
        function (req) {
            const jsonData = JSON.parse(req.responseText);
            const linkedItems= type === 0 ? jsonData.linkedAccessories : jsonData.linkedTools;
            const nonLinkedItems = type === 0 ? jsonData.nonLinkedAccessories : jsonData.nonLinkedTools;
            openModal(function () {
                const modalTitle = document.getElementById("modal_title");
                modalTitle.innerHTML = "Link " + item.name;

                const modal = document.getElementById("modal_content_body");
                modal.innerHTML = "";

                const subtitle = document.createElement("h5");
                subtitle.innerHTML = (type === 0 ? "Select the accessories and documents you want to link to " : "Select the tools and documents you want to link to ")
                    + item.name +
                    " (serial Number: " + item.serialNumber +
                    ", inventory Number: " + item.inventoryNumber + ").";
                subtitle.classList.add("display-7", "mt-4");
                modal.appendChild(subtitle);

                const itemsTableLabel = document.createElement("h6");
                itemsTableLabel.innerHTML = type === 0 ? "Accessories" : "Tools";
                itemsTableLabel.classList.add("display-5", "mt-4");
                modal.appendChild(itemsTableLabel);


                const itemsTable = document.createElement("table");
                itemsTable.setAttribute("id", "linkableItemsTable");
                itemsTable.classList.add("table");

                modal.appendChild(itemsTable);

                $(itemsTable).bootstrapTable({
                        data: linkedItems,
                        columns: jsonItemsColumns,
                        search: true,
                    }
                );
                $(itemsTable).bootstrapTable('checkAll');

                $(itemsTable).bootstrapTable('append', nonLinkedItems);

                const documentsTableLabel = document.createElement("h6");
                documentsTableLabel.innerHTML = "Documents";
                documentsTableLabel.classList.add("display-5", "mt-4");
                modal.appendChild(documentsTableLabel);

                const documentsTable = document.createElement("table");
                documentsTable.setAttribute("id", "linkableDocumentsTable");
                documentsTable.classList.add("table");

                $(documentsTable).bootstrapTable({
                        data: jsonData.linkedDocuments,
                        columns: jsonDocumentsColumns,
                        search: true,
                    }
                );
                $(documentsTable).bootstrapTable('checkAll');
                $(documentsTable).bootstrapTable('append', jsonData.nonLinkedDocuments);

                modal.appendChild(documentsTable);

                const modalFooter = document.getElementById("modal_footer");
                modalFooter.innerHTML = "";

                const saveButton = document.createElement("button");
                saveButton.setAttribute("type", "button");
                saveButton.setAttribute("data-dismiss", "modal");
                saveButton.classList.add("btn", "btn-primary");
                saveButton.innerHTML = "Save";
                saveButton.addEventListener("click", function () {
                        const items = $(itemsTable).bootstrapTable('getSelections');
                        const documents = $(documentsTable).bootstrapTable('getSelections');
                        const dataMap = new Map();
                        dataMap.set("type", type);
                        dataMap.set("id", item.id);
                        dataMap.set(type === 0 ? "accessories" : "tools", items.map(item => item.id));
                        dataMap.set("documents", documents.map(document => document.id));
                        const objFromMap = Array.from(dataMap.entries());
                        makeCall("POST", contextPath + "/SuperUser/LinkItems", null, objFromMap,
                            function (req){
                                openSuccessPrompt("Success!", "Successfully linked " + item.name + " to the selected " + (type === 0 ? "accessories " : "tools ") + "and documents");
                            }
                        );
                    }
                );
                modalFooter.appendChild(saveButton);

            })
        });
}