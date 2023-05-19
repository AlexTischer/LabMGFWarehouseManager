(function (){

    var options = ['None', 'User', 'SuperUser', 'Admin'];

    makeCall("GET", contextPath + "/Admin/GetUsers", null, null,
        function (req) {
                    var jsonData = JSON.parse(req.responseText);
                    var jsonColumns = [
                        {field: 'name', title: 'Name', sortable: true},
                        {field: 'surname', title: 'Surname', sortable: true},
                        {field: 'email', title: 'Email', sortable: true},
                        {field: 'role', title: 'Role', formatter: roleFormatter, sortable: true}
                    ];
                    $(function(req) {
                            $('#usersTable').bootstrapTable({
                            data: jsonData,
                            columns: jsonColumns,
                            search: true,

                        });
                    });
            },
        function (req) {
            console.log("error");
        }
    );

    document.getElementById("setRoles_btn").addEventListener("click", function() {

        openConfirmPrompt("Are you sure you want to assign those roles?", function () {
            var selects = document.getElementById("usersTable").querySelectorAll('select');
            var rolesMap = new Map();
            for(let selection of Array.from(selects) ){
                let id = selection.id.split('_')[1];
                let role = selection.value;
                rolesMap.set(id, role);
            };
            console.log(rolesMap);
            makeCall("POST", contextPath + "/Admin/SetUsersRoles", null, Array.from(rolesMap),
                function (req) {
                    console.log("ok:" + req.responseText);
                },
                function (req) {
                    console.log("error" + req.responseText);
                }
            );
        });
    });



    function roleFormatter(value, row, index) {
        var selectId = 'select_' + row.id;
        var selectHtml = '<select id="' + selectId + '" class="form-select">';

        options.forEach(function(option) {
            var selected = (value === option) ? 'selected' : '';
            selectHtml += '<option value="' + options.indexOf(option) + '" ' + selected + '>' + option + '</option>';
        });

        selectHtml += '</select>';

        return selectHtml;
    }

})();