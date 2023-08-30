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
    const locations = ["Milano-Leonardo", "Piacenza"];

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
        '<a class="btn btn-outline-primary edit m-1" href="javascript:void(0)" title="Edit">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
            '<polygon points="14 2 18 6 7 17 3 17 3 13 14 2"></polygon>' +
            '<line x1="3" y1="22" x2="21" y2="22"></line>' +
        '</svg>',
        '</a>',
        '<a class="btn btn-outline-warning maintenance m-1" href="javascript:void(0)" title="Maintenance">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
            '<circle cx="12" cy="12" r="3"></circle>' +
            '<path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path>' +
        '</svg>',
        '</a>',
        '<a class="btn btn-outline-danger remove m-1" href="javascript:void(0)" title="Remove">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
            '<polyline points="3 6 5 6 21 6"></polyline>' +
            '<path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>' +
            '<line x1="10" y1="11" x2="10" y2="17"></line>' +
            '<line x1="14" y1="11" x2="14" y2="17"></line>' +
        '</svg>',
        '</a>'
    ].join('');
}
function editDocumentActionFormatter(value, row) {
    return [
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

    const jsonColumns = [
        {field: 'type', title: 'Type', sortable: true, formatter: "itemTypeFormatter"},
        {field: 'image', title: 'Image', sortable: false, formatter: "imageFormatter"},
        {field: 'name', title: 'Name', sortable: true},
        {field: 'location', title: 'Location', sortable: true},
        {field: 'description', title: 'Description', sortable: true},
        {field: 'serialNumber', title: 'Serial Number', sortable: true},
        {field: 'inventoryNumber', title: 'Inventory Number', sortable: true},
    ];

    let itemsTable = document.createElement("table");
    itemsTable.setAttribute("class", "table");
    itemsTable.setAttribute("id", row.id + "_itemsTable");

    $(itemsTable).bootstrapTable({
        columns: jsonColumns,
        data: row.requestedItems,
        detailView: false
    });

    const itemsTableHTML = itemsTable.outerHTML;

    return [
        '<h4>Reason: </h4>' + '<h6>' + row.reason + '</h6>',
        '<h4>Requested Items: </h4>' + itemsTableHTML,
    ].join('');
}

function adminRequestDetailFormatter(index, row) {
    return requestDetailFormatter(index, row);
}

function adminRequestActionsFormatter(value, row) {
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

    if(new Date(row.end) >= new Date()) {
        if(row.status === "Pending") {
            return approve + decline;
        } else if(row.status === "Approved") {
            return pending + decline;
        } else if(row.status === "Declined") {
            return pending + approve;
        }
    } else {
        return "";
    }
}

function userRequestDetailFormatter(index, row) {

    const adminNotesDiv = document.createElement("div");

    const adminNotesTitle = document.createElement("h4");
    adminNotesTitle.innerHTML = "Admin Notes: ";
    const adminNotes = document.createElement("h6");
    adminNotes.innerHTML = row.adminNotes;

    adminNotesDiv.appendChild(adminNotesTitle);
    adminNotesDiv.appendChild(adminNotes);

    return adminNotesDiv.outerHTML + requestDetailFormatter(index, row)
}

function userRequestActionsFormatter(value, row) {
    const cancel = [
        '<a class="btn btn-outline-danger cancel m-1" href="javascript:void(0)" title="Cancel">',
            '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"' +
                'fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                '<circle cx="12" cy="12" r="10"></circle>' +
                '<line x1="15" y1="9" x2="9" y2="15"></line>' +
                '<line x1="9" y1="9" x2="15" y2="15"></line>' +
            '</svg>',
        '</a>'
    ].join('');

    const download = [
        '<a class="btn btn-outline-primary download m-1" href="javascript:void(0)" title="Download">',
            '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" ' +
            'fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"> ' +
            '<circle cx="12" cy="12" r="10"/>' +
            '<path d="M16 12l-4 4-4-4M12 8v7"/>' +
            '</svg>',
        '</a>'
    ].join('');


    if(new Date(row.end) > new Date()) {
        if(row.status === "Pending") {
            return cancel;
        } else if(row.status === "Approved") {
            return cancel + download;
        } else if(row.status === "Declined") {
            return "";
        }
    } else {
        return "";
    }
}