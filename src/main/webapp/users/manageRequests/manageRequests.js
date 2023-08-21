let lastExpandedRequest;

(function () {
    window.requestActionEvents = {
        'click .cancel': function (e, value, row, index) {
            cancelRequest(row.id);
        },
        'click .download': function (e, value, row, index) {
            downloadFiles(row.id);
        }
    }

    const jsonColumns = [
        {field: 'start', title: 'Start Date', sortable: true},
        {field: 'end', title: 'End Date', sortable: true},
        {field: 'status', title: 'Status', sortable: true},
        {field: 'actions', title: 'Actions', formatter: userRequestActionsFormatter, events: requestActionEvents}
    ];

    $('#requestsTable').bootstrapTable({
        classes: "table",
        columns: jsonColumns,
        search: true,
        uniqueId: "id",
        checkboxHeader: false,
        detailView: true,
        detailViewIcon: true,
        detailFormatter: "userRequestDetailFormatter",
        onExpandRow: function (index, row, $detail) {
            if(lastExpandedRequest !== undefined && lastExpandedRequest !== row){
                $('#requestsTable').bootstrapTable('collapseRowByUniqueId', lastExpandedRequest.id);
            }
            lastExpandedRequest = row;
        },
    });

    makeCall("GET", contextPath + "/User/GetUserRequests", null, null,
        function (req) {
                    const jsonData = JSON.parse(req.responseText);
                    $('#requestsTable').bootstrapTable('load', jsonData);
        },
        function (req) {
            console.log("Error getting user requests");
        }
    );

})();

function downloadFiles(requestId) {
    window.location.href = contextPath + "/User/GetRequestDocuments" + "?id=" + requestId;
}

function cancelRequest(requestId) {
    openConfirmPrompt("Are you sure you want to cancel this request?", function () {
        makeCall("POST", contextPath + "/User/CancelRequest" + "?id=" + requestId, null, null,
            function (req) {
                openSuccessPrompt("Request cancelled", "Your request has been cancelled successfully");
            },
            function (req) {
                console.log("Error cancelling request");
            }
        );
    });
}
