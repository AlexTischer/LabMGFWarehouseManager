let calendar;
var dateRangeString = "";


(function (){
    calendar = document.getElementById("calendar");
    calendar = new FullCalendar.Calendar(calendar, {
        customButtons: {
            unselectButton: {
                text: 'Clear Selection',
                click: function() {
                    calendar.unselect()
                }
            }
        },
        themeSystem: 'bootstrap5',
        initialView: 'dayGridMonth',
        validRange: function(nowDate) {
            return {
                start: nowDate
            };
        },
        selectable: true,
        selectOverlap: false,
        unselectAuto: false,
        locale: 'en',
        firstDay: 1,
        headerToolbar: {
            themeSystem: 'bootstrap5',
            start: 'title',
            end: 'unselectButton',
            center: 'prevYear prev today next nextYear',
        },
        select: function(info) {
            requestStartDate =  info.startStr;
            requestEndDate = info.endStr;
            dateRangeString = "?start=" + requestStartDate + "&end=" + requestEndDate;
            getAvailableTools();
        },
        unselect: function(info) {
            requestStartDate = null;
            requestEndDate = null;
            dateRangeString = "";
            getAvailableTools();
        }
    });

    calendar.render();
})();

function updateCalendar(){


    makeCall("POST", contextPath + "/User/GetUnAvDates", null, Array.from(requestedItems),
        function (req) {
                    const jsonData = JSON.parse(req.responseText);

                    calendar.removeAllEvents();

                    for(let i = 0; i < jsonData.length; i++) {
                        calendar.addEvent({
                            start: jsonData[i].start,
                            end: jsonData[i].end,
                            color: 'red',
                            display: 'background',
                        });
                    }

                    calendar.render();
                },
        function () {
                    console.log("error");
                },
    );
}

