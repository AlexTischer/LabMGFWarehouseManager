function add() {
    let path;

    document.getElementById("ItemTypeSelect").addEventListener("change", function () {
        if(this.value !== "2") {
            document.getElementById("addToolOrAccessoryDiv").style.display = "block";
            document.getElementById("addDocumentDiv").style.display = "none";
            path = "/SuperUser/InsertItem";

        } else {
            document.getElementById("addToolOrAccessoryDiv").style.display = "none";
            document.getElementById("addDocumentDiv").style.display = "block";
            path = "/SuperUser/InsertDocument";
        }
    });

    document.getElementById("ItemTypeSelect").dispatchEvent(new Event("change"));

    document.getElementById("addItem_btn").addEventListener("click", function (event) {
        event.preventDefault();
        let form;
        let data;
        if(document.getElementById("ItemTypeSelect").value !== "2") {
            form = document.getElementById("addToolOrAccessoryForm")
            data = JSON.stringify({"type": document.getElementById("ItemTypeSelect").value});
        } else {
            form = document.getElementById("addDocumentForm")
        }
        makeCall("POST", contextPath + path, form, data,
            function (req) {
                        const item = JSON.parse(req.responseText);
                        const type = item.type;

                        console.log(item);

                        if(type === 0) {
                            linkTool(item);
                        } else if(type === 1) {
                            linkAccessory(item);
                        } else {
                            linkDocument(item);
                        }
                    },
            function () {
                        console.log("error");
                    }
        );
    });
}