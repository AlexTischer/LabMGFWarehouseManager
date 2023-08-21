//param formElement is optional and used when form data needs to be sent
//param data is optional and used when json data needs to be sent
//param responseType is optional and used when the response is not text
function makeCall(method, url, formElement = null, data = null, okcback, errcback = null) {
    const req = new XMLHttpRequest();

    req.onreadystatechange = function() {
        if (req.readyState == XMLHttpRequest.DONE) {
            if (req.status === 200) {
                okcback(req);
            } else if (errcback === undefined) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    const errorMessage = reader.result;
                    window.alert(errorMessage);
                };
            } else {
                errcback(req);
            }
        }
    };


    req.open(method, url);

    if (formElement != null) {
        let formData = new FormData(formElement);

        if(data !== null && data !== undefined) {
            let jsonData = JSON.parse(data);
            for (let key in jsonData) {
                formData.append(key, jsonData[key]);
            }
        }

        for(let pair of formData.entries()) {
            console.log(pair[0]+ ', '+ pair[1]);
        }

        req.send(formData);

    } else if (data != null) {
        req.setRequestHeader("Content-Type", "application/json");
        let jsonData = JSON.stringify(data);
        req.send(jsonData);

    } else {
        req.send();
    }
}