(function (){

    const jsonColumns = [
        {field: 'linked', checkbox: true},
        {field: 'type', title: 'Type', sortable: true, formatter: "itemTypeFormatter"},
        {field: 'image', title: 'Image', sortable: false, formatter: "imageFormatter"},
        {field: 'name', title: 'Name', sortable: true},
        {field: 'description', title: 'Description', sortable: true},
        {field: 'serialNumber', title: 'Serial Number', sortable: true},
        {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
        {field: 'location', title: 'Location', sortable: true},
    ];

    $('#itemsTable').bootstrapTable({
        columns: jsonColumns,
        search: true,
    });

    let requests;

    makeCall("GET", contextPath + "/User/GetUserRequests", null, null,
        function (req) {
            requests = JSON.parse(req.responseText);
            console.log(requests);
            for (const requestsKey in requests) {
                const request = requests[requestsKey];
                let option = document.createElement("option");
                option.text = request.start + " - " + request.reason;
                option.value = requestsKey;
                document.getElementById("requestSelect").add(option);
            }
            document.getElementById("requestSelect").dispatchEvent(new Event("change"));
        },
        function () {
            console.log("error");
        }
    );

    document.getElementById("requestSelect").addEventListener("change", function() {
        const request = requests[this.value];
        console.log(request);
        console.log(request.requestedItems);
        $('#itemsTable').bootstrapTable('load', (request.requestedItems));
    });

    document.getElementById("submit_btn").addEventListener("click", function(e) {
        e.preventDefault();

        const data = JSON.stringify({"items": $('#itemsTable').bootstrapTable('getSelections').map(item => item.id)});

        openConfirmPrompt("Are you sure you want to submit this report?",
            function () {
                    makeCall("POST", contextPath + "/User/CreateReport", document.getElementById("reportForm"), data,
                        function (req) {
                            openSuccessPrompt("Report created", "Your report has been created successfully");
                        },
                        function () {
                            console.log("error");
                        }
                    )
            }
        );
    });

}());


