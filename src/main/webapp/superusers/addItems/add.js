(function () {
    document.getElementById("ItemTypeSelect").addEventListener("change", function () {
        if(this.value === "2"){
            createNewDocumentForm();
        } else {
            createNewItemForm();
        }
    });

    document.getElementById("ItemTypeSelect").dispatchEvent(new Event("change"));

})();





