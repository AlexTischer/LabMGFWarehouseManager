(function (){

    makeCall("GET", contextPath + "/Admin/GetUsers", null, null,
        function (req) {
                const jsonData = JSON.parse(req.responseText);
                const jsonColumns = [
                {field: 'name', title: 'Name', sortable: true},
                {field: 'surname', title: 'Surname', sortable: true},
                {field: 'email', title: 'Email', sortable: true},
                {field: 'role', title: 'Role', formatter: roleFormatter, sortable: true}
            ];
                $(function() {
                    $('#usersTable').bootstrapTable({
                        data: jsonData,
                        columns: jsonColumns,
                        search: true,
                    });
                });
            }
    );

    document.getElementById("setRoles_btn").addEventListener("click", function(e) {
        e.preventDefault();

        console.log("setRoles_btn clicked");

        openConfirmPrompt("Are you sure you want to assign those roles?", function () {
            const selects = document.getElementById("usersTable").querySelectorAll('select');
            const rolesMap = new Map();
            for(let selection of Array.from(selects) ){
                let id = selection.id.split('_')[1];
                let role = selection.value;
                rolesMap.set(id, role);
            }
            console.log(rolesMap);
            makeCall("POST", contextPath + "/Admin/SetUsersRoles", null, Array.from(rolesMap),
                function (req) {
                    console.log("ok:" + req.responseText);
                }
            );
        });
    });

})();