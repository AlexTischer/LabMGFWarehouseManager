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
        '<a class="btn btn-outline-warning edit" href="javascript:void(0)" title="Edit">',
        '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">\n' +
        '  <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>\n' +
        '  <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"/>\n' +
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