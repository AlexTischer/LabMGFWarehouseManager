function updateCalendar(requestedTools = null, requestedAccessories = null){
    if(requestedTools.length === 0 && requestedAccessories.length === 0){
        //todo: enable full calendar
        return
    }
    else {
        makeCall("GET", contextPath + "/User/GetUnAvDates", null, null /*todo: add selected tools and accessories*/,
            function (req) {
                const data = JSON.parse(req.responseText);
                const unavailablePeriods = data.unavailablePeriods;

                console.log(data);
                console.log(unavailablePeriods);

                //todo: disable unavailable periods

            },
            function () {
                console.log("error");
            }
        );
    }
}