(function () {
    window.reportActionEvents = {
        'click .pending': function (e, value, row, index) {
            updateReportStatus(row.id, 0);
        },
        'click .confirmed': function (e, value, row, index) {
                                updateReportStatus(row.id, 1);
                            },
        'click .solved': function (e, value, row, index) {
                                updateReportStatus(row.id, 2);
                            },
        'click .canceled': function (e, value, row, index) {
                                updateReportStatus(row.id, 3);
                            }
    }

    const jsonColumns = [
        {field: 'date', title: 'Date', sortable: true},
        {field: 'subject', title: 'Subject', sortable: true},
        {field: 'status', title: 'Status', sortable: true},
        {field: 'reportingUser', title: 'Reporting User', formatter: userFormatter, sortable: true},
        {field: 'actions', title: 'Actions', formatter: reportActionFormatter, events: reportActionEvents}
    ];

    $('#reportsTable').bootstrapTable({
        columns: jsonColumns,
        detailView: true,
        detailFormatter: reportDetailFormatter
    });

    getReports();

})();

function getReports(){
    makeCall("GET", contextPath + "/SuperUser/GetReports", null, null,
        function (req) {
            const reports = JSON.parse(req.responseText);
            console.log(reports);
            $(function () {
                $('#reportsTable').bootstrapTable('load', reports);
            });
        },
        function (req) {
            console.error(req);
        }
    );
}

function updateReportStatus(reportId, statusId){
    makeCall("POST", contextPath + "/SuperUser/UpdateReport" + "?reportId=" + reportId + "&reportStatus=" + statusId, null, null,
        function (req) {
            getReports();
        },
        function (req) {
            console.error(req);
        }
    );
}
