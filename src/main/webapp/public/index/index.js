(function () {

    document.getElementById("login_btn").onclick = function () {
        window.location.href = contextPath + "/public/login/login.html";
    };

    document.getElementById("register_btn").onclick = function () {
        window.location.href = contextPath + "/public/register/register.html";
    }

    const jsonColumns = [
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

    makeCall("GET", contextPath + "/GetAvTools", null, null,
        function (req) {
            const jsonData = JSON.parse(req.responseText);
            console.log(jsonData);
            $('#itemsTable').bootstrapTable('load', jsonData);
            makeCall("GET", contextPath + "/GetAvAccessories", null, null,
                function (req) {
                    const jsonData = JSON.parse(req.responseText);
                    console.log(jsonData);
                    $('#itemsTable').bootstrapTable('append', jsonData);
                },
                function (req) {
                    console.log("Error");
                }
            );
        },
        function (req) {
            console.log("Error");
        }
    );

})();
