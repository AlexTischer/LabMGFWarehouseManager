(function () {
    document.getElementById("ItemTypeSelect").addEventListener("change", function () {
        if(parseInt(this.value) === 2){
            editDocuments();
        } else {
            editItems(parseInt(this.value));
        }
    });

    document.getElementById("ItemTypeSelect").dispatchEvent(new Event("change"));

})();