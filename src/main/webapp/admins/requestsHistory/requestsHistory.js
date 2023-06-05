(function() {

    //todo: get the events from the server, display them in the calendar and in the table

    makeCall("GET", contextPath + "/Admin/GetPastRequests", null, null,
        function(req) {
                let calendar = document.getElementById("calendar");
                //const events = req.textContent = JSON.parse(req.responseText);
                calendar = new FullCalendar.Calendar(calendar, {
                    themeSystem: 'bootstrap5',
                    initialView: 'dayGridMonth',
                    selectable: true,
                    locale: 'en',
                    firstDay: 1,
                    headerToolbar: {
                        themeSystem: 'bootstrap5',
                        start: 'title',
                        center: 'prevYear prev today next nextYear',
                        end: 'dayGridMonth timeGridWeek'
                    },
                    //events: req.textContent
            });

            calendar.render();
        },
        function(req) {

        });

})();

