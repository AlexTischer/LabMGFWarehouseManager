function roleFormatter(value, row) {
    const roles = ['None', 'User', 'SuperUser', 'Admin'];

    const selectId = 'select_' + row.id;
    let selectHtml = '<select id="' + selectId + '" class="form-select">';

    roles.forEach(function(option) {
        const selected = (value === option) ? 'selected' : '';
        selectHtml += '<option value="' + roles.indexOf(option) + '" ' + selected + '>' + option + '</option>';
    });

    selectHtml += '</select>';

    return selectHtml;
}

function locationSelectFormatter() {
    const locations = ["Milano-Leonardo", "Pavia"];

    const locationSelect = document.createElement("select");
    locationSelect.setAttribute("class", "form-control");
    locationSelect.setAttribute("id", "addLocationSelect");
    locationSelect.setAttribute("name", "location");
    locationSelect.setAttribute("required", "required");

    for (let i = 0; i < locations.length; i++) {
        const option = document.createElement("option");
        option.setAttribute("value", i.toString());
        option.innerHTML = locations[i];
        locationSelect.appendChild(option);
    }

    return locationSelect;
}

function editActionFormatter(value, row) {
    return [
        '<a class="btn btn-outline-primary edit" href="javascript:void(0)" title="Edit">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">\n' +
        '  <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>\n' +
        '  <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"/>\n' +
        '</svg>',
        '</a>',
        '<a>    </a>',
        '<a class="btn btn-outline-warning maintenance" href="javascript:void(0)" title="Maintenance">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
            '<polygon points="7.86 2 16.14 2 22 7.86 22 16.14 16.14 22 7.86 22 2 16.14 2 7.86 7.86 2"></polygon>' +
            '<line x1="12" y1="8" x2="12" y2="12"></line>' +
            '<line x1="12" y1="16" x2="12.01" y2="16"></line>' +
        '</svg>',
        '</a>',
        '<a>    </a>',
        '<a class="btn btn-outline-danger remove" href="javascript:void(0)" title="Remove">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">\n' +
        '  <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6Z"/>\n' +
        '  <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1ZM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118ZM2.5 3h11V2h-11v1Z"/>\n' +
        '</svg>',
        '</a>'
    ].join('');
}

function imageFormatter(value, row) {

    const photo = document.createElement("img");

    photo.setAttribute("height", "auto");
    photo.setAttribute("width", "160");
    photo.setAttribute("alt", "Item image");
    photo.setAttribute('src', contextPath + "/GetFile" + "?path=" + encodeURIComponent(row.imagePath));

    return photo.outerHTML;


}

function itemTypeFormatter(value, row) {
    const types = ['Tool', 'Accessory'];

    if(value === 0) {
        return types[0];
    } else {
        return types[1];
    }
}

function userFormatter(value, row) {
    return value.name + " " + value.surname;
}

function reportDetailFormatter(index, row) {
    const reportedItemsTable = document.createElement("table");
    reportedItemsTable.setAttribute("class", "table table-striped table-bordered table-hover");
    reportedItemsTable.setAttribute("id", "reportedItemsTable");

    const jsonColumns = [
        {field: 'name', title: 'Name', sortable: true},
        {field: 'description', title: 'Description', sortable: true},
        {field: 'serialNumber', title: 'Serial Number', sortable: true},
        {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
        {field: 'location', title: 'Location', sortable: true},
    ];

    $(reportedItemsTable).bootstrapTable({
        columns: jsonColumns,
        search: false,
    });

    $(reportedItemsTable).bootstrapTable('load', row.reportedItems);

    return [
        '<h4>Subject: </h4>' + '<h6>' + row.subject + '</h6>',
        '<h4>Body: </h4>' + '<h6>' + row.body + '</h6>',
        '<h4>Reported Items:</h4>',
        reportedItemsTable.outerHTML

    ].join('');
}

function reportActionFormatter(value, row) {
    return [
        '<a class="btn btn-outline-warning pending m-1" href="javascript:void(0)" title="Mark as Pending">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
            '<circle cx="12" cy="12" r="10"></circle><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"></path>' +
            '<line x1="12" y1="17" x2="12.01" y2="17"></line>' +
        '</svg>',
        '</a>',
        '<a class="btn btn-outline-primary confirmed m-1" href="javascript:void(0)" title="Mark as Confirmed">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
            '<path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>' +
            '<polyline points="22 4 12 14.01 9 11.01"></polyline>' +
        '</svg>',
        '</a>',
        '<a class="btn btn-outline-success solved m-1" href="javascript:void(0)" title="Mark as Solved">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
            '<path d="M14 9V5a3 3 0 0 0-3-3l-4 9v11h11.28a2 2 0 0 0 2-1.7l1.38-9a2 2 0 0 0-2-2.3zM7 22H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2h3"></path>' +
        '</svg>',
        '</a>',
        '<a class="btn btn-outline-danger canceled m-1" href="javascript:void(0)" title="Mark as Canceled">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
            '<circle cx="12" cy="12" r="10"></circle>' +
            '<line x1="15" y1="9" x2="9" y2="15"></line>' +
            '<line x1="9" y1="9" x2="15" y2="15"></line>' +
        '</svg>',
        '</a>'
    ].join('');
}


function toolDetailFormatter(index, row, ) {
    const detailsDiv = document.createElement("div");
    detailsDiv.setAttribute("class", "container");
    detailsDiv.setAttribute("id", row.id + "_detailsDiv");

    const descriptionTitle = document.createElement("h4");
    descriptionTitle.innerHTML = "Description: ";
    const description = document.createElement("h6");
    description.innerHTML = row.description;
    const serialNumberTitle = document.createElement("h4");
    serialNumberTitle.innerHTML = "Serial Number: ";
    const serialNumber = document.createElement("h6");
    serialNumber.innerHTML = row.serialNumber;
    const inventoryNumberTitle = document.createElement("h4");
    inventoryNumberTitle.innerHTML = "Inventory Number: ";
    const inventoryNumber = document.createElement("h6");
    inventoryNumber.innerHTML = row.inventoryNumber;

    detailsDiv.appendChild(descriptionTitle);
    detailsDiv.appendChild(description);
    detailsDiv.appendChild(serialNumberTitle);
    detailsDiv.appendChild(serialNumber);
    detailsDiv.appendChild(inventoryNumberTitle);
    detailsDiv.appendChild(inventoryNumber);

    return detailsDiv.outerHTML;
}

function accessoryDetailFormatter(index, row) {
    return [
        '<h4>Description: </h4>' + '<h6>' + row.description + '</h6>',
        '<h4>Serial Number: </h4>' + '<h6>' + row.serialNumber + '</h6>',
        '<h4>Inventory Number: </h4>' + '<h6>' + row.inventoryNumber + '</h6>',
    ].join('');
}

function requestDetailFormatter(index, row) {
    //todo: add request details
}

function requestActionsFormatter(value, row) {

    const approve = [
        '<a class="btn btn-outline-success approve m-1" href="javascript:void(0)" title="Approve">',
            '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                '<path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>' +
                '<polyline points="22 4 12 14.01 9 11.01"></polyline>' +
            '</svg>',
        '</a>'
    ].join('');

    const decline = [
        '<a class="btn btn-outline-danger decline m-1" href="javascript:void(0)" title="Decline">',
            '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                '<circle cx="12" cy="12" r="10"></circle>' +
                '<line x1="15" y1="9" x2="9" y2="15"></line>' +
                '<line x1="9" y1="9" x2="15" y2="15"></line>' +
            '</svg>',
        '</a>'
    ].join('');

    const pending = [
        '<a class="btn btn-outline-warning pending m-1" href="javascript:void(0)" title="Mark as Pending">',
            '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                '<circle cx="12" cy="12" r="10"></circle><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"></path>' +
                '<line x1="12" y1="17" x2="12.01" y2="17"></line>' +
            '</svg>',
        '</a>'
    ].join('');

    if(row.status === "Pending") {
        return approve + decline;
    } else if(row.status === "Approved") {
        return pending + decline;
    } else if(row.status === "Declined") {
        return pending + approve;
    }

}