(function () {
    document.getElementById("actionSelect").addEventListener("change", function () {
        if (this.value === "add") {
            add();
            document.getElementById("addItemDiv").style.display = "block";
            document.getElementById("editItemDiv").style.display = "none";
            document.getElementById("editDocumentDiv").style.display = "none";
        } else if (this.value === "edit") {
            edit();
            document.getElementById("addItemDiv").style.display = "none";
        }
    });

    document.getElementById("actionSelect").dispatchEvent(new Event("change"));
})();