let requestedItems = [];
let requestStartDate = null;
let requestEndDate = null;

(function () {

    const reason = document.getElementById("reason_textArea");

    document.getElementById("submit-btn").addEventListener("click", function () {
        if(requestedItems.length === 0 || requestStartDate === null || requestEndDate === null || reason.value === ""){
            console.log("requested Items: " + requestedItems + "\n", "requested start Date: " + requestStartDate + "\n", requestEndDate, reason.value)
            openAlertPrompt("Error", "Please fill all the fields and select at least one item");
        } else {
            const requestMap = new Map();
            requestMap.set("items", requestedItems);
            requestMap.set("start", requestStartDate);
            requestMap.set("end", requestEndDate);
            requestMap.set("reason", reason.value);
            makeCall("POST", contextPath + "/User/CreateRequest", null, Array.from(requestMap),
                function (req) {
                            openSuccessPrompt("Request sent", "Your request has been sent successfully");
                        }
            );
        }
    });
})();